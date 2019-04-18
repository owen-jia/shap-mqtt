package com.ts.shap.core;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MqttClient连接回调
 * @author: Owen Jia
 * @time: 2018/12/27 10:15
 */
public class ShapCallBack implements MqttCallback {

    private final static Logger log = LoggerFactory.getLogger(ShapCallBack.class);

    MqttClient mqttClient;

    public ShapCallBack() {
        this.mqttClient = ShapContext.getInstance().getMqttClient();
    }

    public void connectionLost(Throwable cause) {
        log.warn("mqtt连接断开,{}",cause.getMessage());
        MqttConnectOptions options = ShapContext.getInstance().getOptions();
        try {
            mqttClient.connectWithResult(options);
            log.warn("已经重新连接mqtt");
        } catch (MqttException e) {
            log.warn("mqtt重新连接失败,{}",e.getMessage());
            try {
                mqttClient.close(true);
                mqttClient = null;
                mqttClient = new MqttClient(mqttClient.getServerURI(),mqttClient.getClientId());
                IMqttToken token = mqttClient.connectWithResult(options);
                log.warn("再次尝试重新连接mqtt,{}",token.isComplete());
            } catch (MqttException e1) {
                log.warn("再次尝试重新连接mqtt失败,{}",e1.getMessage());
            }
        }
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("消息到达,topic:{},id:{},qos:{}",topic,message.getId(),message.getQos());
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("消息发送完成,id:{}",token.getMessageId());

//        try {
//            String msg = new String(token.getMessage().getPayload());
//            log.debug(msg);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
    }
}
