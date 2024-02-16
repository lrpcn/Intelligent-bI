package com.lrpcn.quickdev.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/12 16:31
 */
@Slf4j
public class ExcelUtils {

    public static String excelToCsv(MultipartFile file) {
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(file.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误", e);
        }
        // 数据为空
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 获取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        List<String> headerList = new ArrayList<>();
        Collection<String> values = headerMap.values();
        for (String value : values) {
            if (ObjectUtils.isNotEmpty(value)) {
                headerList.add(value);
            }
        }
        stringBuilder.append(StringUtils.join(headerList, ","));
        // 获取行数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            values = dataMap.values();
            List<String> dataList = new ArrayList<>();
            for (String value : values) {
                if (ObjectUtils.isNotEmpty(value)) {
                    dataList.add(value);
                }
            }
            stringBuilder.append(StringUtils.join(dataList, ",")).append("\n");
        }
        return stringBuilder.toString();
    }
}
