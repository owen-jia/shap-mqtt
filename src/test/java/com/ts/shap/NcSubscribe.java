package com.ts.shap;

import com.ts.shap.IShapListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 订阅测试
 * @author: Owen Jia
 * @time: 2019/4/16 17:20
 */
public class NcSubscribe implements IShapListener {

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic:"+topic);
        System.out.println("id:"+message.getId());
        System.out.println("qos:"+message.getQos());
        System.out.println("context:"+message.getPayload());
    }
}
