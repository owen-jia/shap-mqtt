package com.ts.shap;

import com.ts.shap.core.ShapContext;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author: Owen Jia
 * @time: 2019/4/18 13:19
 */
public class ShapMqtt {

    ShapContext shapContext = ShapContext.getInstance();

    /**
     * 发布
     */
    public void publish(String topic, MqttMessage mqttMessage){
        try {
            shapContext.getMqttClient().publish(topic,mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅
     */
    public void subscribe(String topic,int qos, IShapListener listener){
        if(listener.getClass().isInterface()){
            throw new RuntimeException("必须是对象！");
        }

        shapContext.registListener(topic, qos, listener);
    }

    /**
     * 订阅
     */
    public void subscribe(String topic, IShapListener listener){
        if(listener.getClass().isInterface()){
            throw new RuntimeException("必须是对象！");
        }

        shapContext.registListener(topic, listener);
    }

}
