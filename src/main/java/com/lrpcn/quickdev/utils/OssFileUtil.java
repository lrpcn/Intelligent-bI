package com.lrpcn.quickdev.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.config.AliOOSConfig;
import com.lrpcn.quickdev.exception.CustomException;
import com.lrpcn.quickdev.exception.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/9 19:40
 */
@Slf4j
@Component
public class OssFileUtil {

    @Resource
    private AliOOSConfig aliOOSConfig;

    public String uploadPictureUrl(String pictureUrl) {
        try {
            URL fileurl = new URL(pictureUrl);
            InputStream inputStream = fileurl.openStream();
            return uploadFileToOss(inputStream);
        } catch (Exception e) {
            throw new CustomException(ErrorCodeEnum.SYSTEM_ERROR, "文件上传异常");
        }
    }

    private static final long ONE_MB = 1024 * 1024L;

    private static final List<String> validPictureFileSuffixList = Arrays.asList("png", "jpg", "webp", "jpeg");

    private static final List<String> validExcelFileSuffixList = Arrays.asList("xlsx", "xls");

    /**
     * 上传图片到OSS
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadPicture(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(originalFilename);
        long size = file.getSize();
        ThrowUtil.throwIfCustomException(size > ONE_MB, ErrorCodeEnum.PARAMETER_ERROR, "文件太大了");
        ThrowUtil.throwIfCustomException(!CollUtil.contains(validPictureFileSuffixList, suffix), ErrorCodeEnum.PARAMETER_ERROR);
        return uploadFileToOss(file.getInputStream());
    }

    /**
     * 获取OOS连接
     *
     * @return OSS
     */
    private OSS getClient() {
        return new OSSClientBuilder()
                .build(aliOOSConfig.getEndpoint()
                        , aliOOSConfig.getAccessKeyId()
                        , aliOOSConfig.getAccessKeySecret());
    }


    private String uploadFileToOss(MultipartFile file) throws IOException {
        OSS ossClient = null;
        String url;
        try (InputStream inputStream = file.getInputStream()) {
            String suffix = FileUtil.getSuffix(file.getOriginalFilename());
            String bucketName = aliOOSConfig.getBucketName();
            ossClient = getClient();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            String fileName = IdUtil.fastSimpleUUID();
            fileName = "file/" + date + "/" + fileName + "." + suffix;
            ossClient.putObject(bucketName, fileName, inputStream);
            String endpoint = aliOOSConfig.getEndpoint();
            //文件访问路径
            url = "Https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (ObjUtil.isNotNull(ossClient)) {
                ossClient.shutdown();
            }
        }
        return url;
    }

    private String uploadFileToOss(InputStream inputStream) throws IOException {
        OSS ossClient = null;
        String url = null;
        try {
            String bucketName = aliOOSConfig.getBucketName();
            ossClient = getClient();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());
            String fileName = IdUtil.fastSimpleUUID();
            fileName = "image/" + date + "/" + fileName;
            ossClient.putObject(bucketName, fileName, inputStream);
            String endpoint = aliOOSConfig.getEndpoint();
            //文件访问路径
            url = "Https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (OSSException | ClientException e) {
            throw new CustomException(ErrorCodeEnum.SYSTEM_ERROR, "文件上传异常");
        } finally {
            if (ObjUtil.isNotNull(ossClient)) {
                ossClient.shutdown();
            }
            inputStream.close();
        }
        return url;
    }

}
