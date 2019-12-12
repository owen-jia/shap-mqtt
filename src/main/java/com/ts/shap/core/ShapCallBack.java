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
        log.error("mqtt connection lost at {}",cause.getMessage());

        MqttConnectOptions options = ShapContext.getInstance().getOptions();
        MqttClient mqttClient = ShapContext.getInstance().getMqttClient();
        try {
            mqttClient.connectWithResult(options);
            log.info("mqtt reconnect success.");
        } catch (MqttException e) {
            log.error("mqtt reconnect failure.{},{}",e.getMessage(),e.getCause().getMessage());

            try {
                mqttClient.close(true);
                mqttClient = null;
                mqttClient = new MqttClient(mqttClient.getServerURI(), mqttClient.getClientId());
                IMqttToken token = mqttClient.connectWithResult(options);
                log.info("mqtt reconnect again success,{}",token.isComplete());
            } catch (MqttException e1) {
                log.error("mqtt reconnect again failure,{}",e1.getMessage());
                ShapContext.getInstance().stopListener();
                throw new RuntimeException("mqtt connection is lost in 2 try times,please check your config data.");
            }
        }
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("message arrived,topic:{},id:{},qos:{}",topic,message.getId(),message.getQos());
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("message send complete,id:{}",token.getMessageId());
    }
}
