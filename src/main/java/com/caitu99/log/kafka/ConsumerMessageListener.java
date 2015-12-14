/*
 * Copyright (c) 2015-2020 by caitu99
 * All rights reserved.
 */
package com.caitu99.log.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caitu99.log.util.AnsMsg;
import com.caitu99.log.util.AppConfig;
import com.caitu99.log.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.kafka.core.KafkaMessage;
import org.springframework.integration.kafka.listener.MessageListener;
import org.springframework.integration.kafka.serializer.common.StringDecoder;
import org.springframework.integration.kafka.util.MessageUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka 消息消费者
 *
 * @author Hongbo Peng
 * @Description: (类职责详细描述, 可空)
 * @ClassName: ConsumerMessageListener
 * @date 2015年11月30日 下午6:00:19
 * @Copyright (c) 2015-2020 by caitu99
 */
@Service("consumerMessageListener")
public class ConsumerMessageListener implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppConfig appConfig;

    @Override
    public void onMessage(KafkaMessage message) {
        try {

            String msgStr = MessageUtils.decodePayload(message, new StringDecoder());
            logger.debug("Listen to the message is {}", msgStr);
            if (AnsMsg.sendOrNot(msgStr)) {
                String url1 = "https://oapi.dingtalk.com/gettoken?corpid=" + appConfig.corpid + "&corpsecret=" + appConfig.corpsecret;

                String str = HttpClientUtils.get(url1, "UTF-8");
                JSONObject json = JSON.parseObject(str);
                Integer code = json.getInteger("errcode");
                String access_token = json.getString("access_token");

                if (code == 0) {
                    url1 = "https://oapi.dingtalk.com/message/send?access_token=" + access_token;
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    TextContent textContent = new TextContent();
                    textContent.setContent(msgStr);
                    JSONbody jsonbody = new JSONbody();
                    if (!"".equals(appConfig.userId)) {
                        jsonbody.setTouser(appConfig.userId);
                    }
                    if (!"".equals(appConfig.partyId)) {
                        jsonbody.setToparty(appConfig.partyId);
                    }
                    jsonbody.setAgentid(appConfig.agentId);
                    jsonbody.setMsgtype("text");

                    jsonbody.setText(textContent);
                    String body = JSON.toJSONString(jsonbody);
                    String str2 = HttpClientUtils.postJsonAndHeaders(url1, body, "application/json", "UTF-8", headers, 10000, 10000);

                }
            }
            logger.info("接收到的消息：{}", msgStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("kafka消息消费发生异常：{}", e);
        }
    }
}




