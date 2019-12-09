package com.ts.shap;

import com.ts.shap.core.ShapContext;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * ShapMqtt Util
 * @author : Owen Jia
 * @since : 2019/4/18 13:19
 */
public class ShapMqtt {

    private ShapContext shapContext = ShapContext.getInstance();
    private String username;
    private String password;
    private String serverUrl = "tcp://127.0.0.1:1883";
    private String clientId = "shap-client";
    private int scanTime;//单位秒

    public ShapMqtt(String serverUrl, String clientId, int scanTime) {
        this.serverUrl = serverUrl;
        this.clientId = clientId;
        this.scanTime = scanTime;

        this.initContent();
    }

    public ShapMqtt(String serverUrl, String clientId){
        this.serverUrl = serverUrl;
        this.clientId = clientId;
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
     * 发布，默认qos=0，retained=false
     * @param topic Topic
     * @param content 发送内容
     */
    public void publish(String topic, String content){
        this.publish(topic,0,content.getBytes(),false);
    }

    public void publish(String topic, int qos, String content){
        this.publish(topic,qos,content.getBytes(),false);
    }

    public void publish(String topic, int qos, String content, boolean retained){
        this.publish(topic,qos,content.getBytes(),retained);
    }

    /**
     * 发送消息
     * @param topic Topic
     * @param qos 消息级别
     * @param content 内容
     * @param retained 是否保留broker中, true|false
     */
    public void publish(String topic, int qos, byte[] content, boolean retained){
        try {
            shapContext.getMqttClient().publish(topic,content,qos,retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布
     * @param topic Topic
     * @param mqttMessage 消息内容
     */
    public void publish(String topic, MqttMessage mqttMessage){
        try {
            shapContext.getMqttClient().publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅
     * @param topic Topic
     * @param qos Qos
     * @param listener 自定义监听器 extend IShapListener
     */
    public void subscribe(String topic,int qos, IShapListener listener){
        if(listener.getClass().isInterface()){
            throw new RuntimeException("必须是对象！");
        }
        shapContext.registListener(topic, qos, listener);
    }

    /**
     * 订阅，默认qos=0
     * @param topic Topic
     * @param listener 自定义监听
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
