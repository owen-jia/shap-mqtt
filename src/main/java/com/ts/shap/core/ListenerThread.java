package com.ts.shap.core;

import com.ts.shap.IShapListener;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.LocalDateTime;

/**
 * @author: Owen Jia
 * @time: 2019/4/18 15:21
 */
public class ListenerThread extends CoreThread{

    ShapContext context = ShapContext.getInstance();

    IShapListener listener;
    String topic;
    int qos;

    public ListenerThread(String topic, int qos, IShapListener listener) {
        this.topic = topic;
        this.qos = qos;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            System.out.println("this is run!");
            while (!this.stoped){
                System.out.println("now is " + LocalDateTime.now().toString());

                context.getMqttClient().subscribe(topic, qos, listener);

                sleep(context.getScanTime() * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
