package com.ts.shap;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Shap 测试
 * @author: Owen Jia
 * @time: 2019/4/17 13:35
 */
public class ShapTest {

    ShapMqtt shapMqtt = new ShapMqtt();

    public void subTest() {
        shapMqtt.subscribe("hello/1",new NcSubscribe());

        shapMqtt.subscribe("hello/2",new NcSubscribe());

        shapMqtt.subscribe("hello/3",new NcSubscribe());

        shapMqtt.subscribe("hello/#",new NcSubscribe());
    }

    public void pubTest() {
        MqttMessage message = new MqttMessage();
        message.setQos(2);
        message.setPayload("hello world!".getBytes());

        shapMqtt.publish("hello/1", message);
    }

    public static void main(String[] args){
        new ShapTest().subTest();
    }

}
