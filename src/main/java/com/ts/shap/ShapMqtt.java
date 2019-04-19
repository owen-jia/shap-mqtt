package com.ts.shap;

import com.ts.shap.core.ShapContext;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * ShapMqtt Util
 * @author: Owen Jia<owen-jia@outlook.com>
 * @time: 2019/4/18 13:19
 */
public class ShapMqtt {

    private ShapContext shapContext = ShapContext.getInstance();

    private String serverUrl = "tcp://127.0.0.1:1883";
    private String clientId = "shap-client";
    private int scanTime;//单位秒，最小3s

    public ShapMqtt(String serverUrl, String clientId, int scanTime) {
        this.serverUrl = serverUrl;
        this.clientId = clientId;
        this.scanTime = scanTime;

        this.initContent();
    }

    public ShapMqtt(String serverUrl) {
        this.serverUrl = serverUrl;

        this.initContent();
    }

    private void initContent(){
        try {
            shapContext.init(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

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

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime;
    }
}
