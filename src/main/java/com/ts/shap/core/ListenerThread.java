package com.ts.shap.core;

import com.ts.shap.IShapListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 基础程序
 * @author : Owen Jia
 * @since : 2019/4/18 15:21
 */
public class ListenerThread extends CoreThread{
    private final static Logger log = LoggerFactory.getLogger(ListenerThread.class);
    ShapContext context = ShapContext.getInstance();

    private IShapListener listener;
    private String topic;
    private int qos;

    public ListenerThread(String topic, int qos, IShapListener listener) {
        this.topic = topic;
        this.qos = qos;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            log.debug("("+topic + ") is listening from " + LocalDateTime.now().toString());
            while (!this.stoped){
                context.getMqttClient().subscribe(topic, qos, listener);
                sleep(context.getScanTime() * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public IShapListener getListener() {
        return listener;
    }

}
