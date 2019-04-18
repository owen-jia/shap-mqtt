package com.ts.shap.core;

import com.ts.shap.IShapListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Shap 生命周期上下文
 * @author: Owen Jia
 * @time: 2019/4/16 17:34
 */
public class ShapContext {
    private final static Logger log = LoggerFactory.getLogger(ShapContext.class);

    static ShapContext context = null;
    static Map<Class, IShapListener> shapListenerMap = new HashMap<>();
    static ExecutorService pool = Executors.newCachedThreadPool();

    private MqttClient mqttClient;
    private MqttConnectOptions options;

    private String serverUrl = "tcp://172.20.10.187:1883";
    private String clientId = "shap-client";
    private int scanTime = 3;// 监听时间，单位秒，默认5s

    private ShapContext(){
    }

    public static ShapContext getInstance(){
        if(context == null){
            context = new ShapContext();
            context.start();
        }

        return context;
    }

    private void init() throws MqttException {
        options = new MqttConnectOptions();
        options.setKeepAliveInterval(20);
        options.setConnectionTimeout(10);
        options.setCleanSession(false);

        mqttClient = new MqttClient(serverUrl, clientId, new MemoryPersistence());
        mqttClient.setCallback(new ShapCallBack());
        IMqttToken mqttToken = mqttClient.connectWithResult(options);

        log.debug("MqttClient connect is {}", mqttToken.isComplete() ? "success" : "error");
    }

    public void start() {
        try {
            this.init();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void scanListener() throws ClassNotFoundException {
        Class listener = Class.forName("com.ts.shap.IShapListener");

    }

    public void registListener(String topic, IShapListener listener){
        this.registListener(topic,0, listener);
    }

    public void registListener(String topic,int qos, IShapListener listener){
        shapListenerMap.put(listener.getClass(), listener);
        ListenerThread thread = new ListenerThread(topic, qos, listener);
        pool.execute(thread);
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public void setMqttClient(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public void setOptions(MqttConnectOptions options) {
        this.options = options;
    }

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime;
    }
}
