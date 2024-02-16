package com.lrpcn.quickdev.manager;

import cn.hutool.core.util.ObjUtil;
import com.lrpcn.quickdev.common.ErrorCodeEnum;
import com.lrpcn.quickdev.exception.ThrowUtil;
import com.lrpcn.quickdev.utils.ExcelUtils;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.exception.SparkException;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import io.github.briqt.spark4j.model.response.SparkTextUsage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能:
 * 作者: lrpcn
 * 日期: 2024/2/12 16:38
 */
@Slf4j
@Service
public class AiManager {

    // AI模型id
    private static final Long BI_MODEL_ID = 1659171950288818178L;

    @Resource
    private YuCongMingClient yuCongMingClient;

    @Resource
    private SparkClient sparkClient;

    public static final String PRECONDITION = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
            "分析需求：\n" +
            "{数据分析的需求或者目标}\n" +
            "原始数据：\n" +
            "{csv格式的原始数据，用,作为分隔符}\n" +
            "请根据这两部分内容，严格按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
            "【【【【【\n" +
            "{前端 Echarts V5 的 option 配置对象 JSON 代码, 不要生成任何多余的内容，比如注释和代码块标记}\n" +
            "【【【【【\n" +
            "{明确的数据分析结论、越详细越好，不要生成多余的注释} \n" +
            "最终格式是:  【【【【【 前端代码 【【【【【 分析结论 \n";

    public String doChat(String message) {
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(PRECONDITION + message));
        SparkRequest sparkRequest = SparkRequest.builder()
                .messages(messages)
                .maxTokens(2048)
                .temperature(0.2)
                .apiVersion(SparkApiVersion.V3_0)
                .build();
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        return chatResponse.getContent();
    }


    private static final String AIinit = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容: \n" +
            "分析需求：\n" +
            "{原始数据的需求分析或目标}\n" +
            "原始数据：\n" +
            "{csv格式的原始数据，用,作为分隔符}\n" +
            "请根据这两部分内容，严格按照以下指定格式生成内容(此外不要输出任何多余的开头、结尾、注释)\n" +
            "前端代码上一行用【【【【【分割开\\n" +
            "{前端 Echarts V5 的 option 配置对象 JS 代码，前端代码形式如：{\n" +
            "  \"title\"：{\n" +
            "    \"text\": \"用户增长趋势\"\n" +
            "  },\n" +
            "  \"xAxis\"：{\n" +
            "    \"type\": \"category\",\n" +
            "    \"data\": [\"1号\", \"2号\", \"3号\"]\n" +
            "  },\n" +
            "  \"yAxis\"：{\n" +
            "    \"type\": \"value\"\n" +
            "  },\n" +
            "  \"series\"：[{\n" +
            "    \"data\": [10 ,20, 30],\n" +
            "    \"type\": \"line\"\n" +
            "  }\n" +
            "}不要生成任何多余的注释，比如注释和代码标记}\n" +
            "前端代码结束后下一行用【【【【【分隔，再下一行就是分析结论\n" +
            "{详细明确的数据分析结论，不要生成其他的注释} \n";


//    public String doChat(String goal, String cvsData, String chartType) {
//        String sb = AIinit +
//                "分析需求：\n" + goal +
//                "\n生成图标的类型是: " + chartType +
//                "\n原始数据：\n" + cvsData;
//        List<SparkMessage> messages = new ArrayList<>();
//        messages.add(SparkMessage.userContent(sb));
//        SparkRequest sparkRequest = SparkRequest.builder()
//                .messages(messages)
//                .maxTokens(2048)
//                .temperature(0.2)
//                .apiVersion(SparkApiVersion.V3_0)
//                .build();
//        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
//        String content = chatResponse.getContent();
//        log.info("\n Ai的回答 {}", content);
//        return content;
//    }


    /**
     *
     * @param goal
     * @param cvsData
     * @param chartType
     * @return
     */
    public String doChat(String goal, String cvsData, String chartType) {
        //根据用户上传的数据，压缩ai提问语
        StringBuilder res = new StringBuilder();
        res.append("你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：");
        res.append("\n").append("分析需求：").append("\n").append("{").append(goal).append("}").append("\n");
        res.append("原始数据:").append("\n").append(cvsData);
        res.append("请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n【【【【【\n先输出上面原始数据的分析结果：\n然后输出【【【【【\n{前端 Echarts V5 的 option 配置对象JSON代码，生成");
        res.append(chartType);
        res.append("合理地将数据进行可视化，不要生成任何多余的内容，不要注释}");
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent(res.toString()));
        SparkRequest sparkRequest = SparkRequest.builder()
                .messages(messages)
                .maxTokens(2048)
                .temperature(0.2)
                .apiVersion(SparkApiVersion.V3_0)
                .build();
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        String content = chatResponse.getContent();
        log.info("AI 生成的信息: {}", content);
        return content;
    }


//    public String doChat(String goal, String cvsData, String chartType) {
//        String message = PRECONDITION + "分析需求 " + goal + " \n原始数据如下: " + cvsData + "\n生成图标的类型是: " + chartType;
//        log.error("\n" + message);
//        List<SparkMessage> messages = new ArrayList<>();
//        messages.add(SparkMessage.userContent(message));
//        SparkRequest sparkRequest = SparkRequest.builder()
//                .messages(messages)
//                .maxTokens(2048)
//                .temperature(0.2)
//                .apiVersion(SparkApiVersion.V3_0)
//                .build();
//        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
//        String content = chatResponse.getContent();
//        log.info("AI 生成的信息: {}", content);
//        return content;
//    }

//    /**
//     * AI对话
//     *
//     * @param message
//     * @return
//     */
//    public String doChat(String message) {
//        DevChatRequest devChatRequest = new DevChatRequest();
//        // 设置ai模型id
////        devChatRequest.setModelId(1709156902984093697L);
//        devChatRequest.setModelId(BI_MODEL_ID);
//        devChatRequest.setMessage(message);
//        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
//        ThrowUtil.throwIfCustomException(ObjUtil.isNull(response), ErrorCodeEnum.SYSTEM_ERROR);
//        return response.getData().getContent();
//    }
}
