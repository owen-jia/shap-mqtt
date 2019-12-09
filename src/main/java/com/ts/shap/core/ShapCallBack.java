package com.ts.shap.core;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MqttClient 连接回调监控类
 * @author : Owen Jia
 * @since : 2018/12/27 10:15
 */
public class ShapCallBack implements MqttCallback {

    private final static Logger log = LoggerFactory.getLogger(ShapCallBack.class);

    public void connectionLost(Throwable cause) {
        log.error("mqtt连接断开,{}",cause.getMessage());

        MqttConnectOptions options = ShapContext.getInstance().getOptions();
        MqttClient mqttClient = ShapContext.getInstance().getMqttClient();
        try {
            mqttClient.connectWithResult(options);
            log.info("mqtt重新连接成功");
        } catch (MqttException e) {
            log.error("mqtt重新连接失败,{}",e.getMessage());

            try {
                mqttClient.close(true);
                mqttClient = null;
                mqttClient = new MqttClient(mqttClient.getServerURI(), mqttClient.getClientId());
                IMqttToken token = mqttClient.connectWithResult(options);
                log.info("mqtt再次重新连接成功,{}",token.isComplete());
            } catch (MqttException e1) {
                log.error("mqtt再次重新连接失败,{}",e1.getMessage());
                ShapContext.getInstance().stopListener();
                throw new RuntimeException("Mqtt连接断开，尝试2次重连失败，请检测环境");
            }
        }
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("消息到达,topic:{},id:{},qos:{}",topic,message.getId(),message.getQos());
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("消息发送完成,id:{}",token.getMessageId());
    }
}
