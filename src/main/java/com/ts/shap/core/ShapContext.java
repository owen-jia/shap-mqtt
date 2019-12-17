package com.ts.shap.core;

import com.ts.shap.IShapListener;
import com.ts.shap.ShapMqtt;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Shap 生命周期上下文
 * @author : Owen Jia
 * @since : 2019/4/16 17:34
 */
public class ShapContext {
    private final static Logger log = LoggerFactory.getLogger(ShapContext.class);

    static ShapContext context = null;
    static Map<Class, IShapListener> shapListenerMap = new HashMap<>();
    static ExecutorService pool = new ThreadPoolExecutor(5, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            Executors.defaultThreadFactory(),new ThreadPoolExecutor.CallerRunsPolicy());

    private MqttClient mqttClient;
    private MqttConnectOptions options;

    private int scanTime = 2;// 监听时间，单位秒

    private ShapContext(){
    }

    public synchronized static ShapContext getInstance(){
        if(context == null){
            context = new ShapContext();
        }
        return context;
    }

    public void init(ShapMqtt shapMqtt) throws MqttException {
        options = new MqttConnectOptions();
        options.setKeepAliveInterval(20);
        options.setConnectionTimeout(10);
        options.setCleanSession(false);

        if(shapMqtt.getUsername() != null)
            options.setUserName(shapMqtt.getUsername());
        if(shapMqtt.getPassword() != null)
            options.setPassword(shapMqtt.getPassword().toCharArray());

        if(shapMqtt.getScanTime() > this.getScanTime())
            this.setScanTime(shapMqtt.getScanTime());

        mqttClient = new MqttClient(shapMqtt.getServerUrl(), shapMqtt.getClientId(), new MemoryPersistence());
        mqttClient.setCallback(new ShapCallBack());
        IMqttToken mqttToken = mqttClient.connectWithResult(options);

        log.debug("MqttClient connect is {}", mqttToken.isComplete() ? "success" : "error");
    }

    /**
     * 停止所有的listener
     */
    protected void stopListener(){
        List<?> stopedListener = pool.shutdownNow();

        stopedListener.forEach( a -> {
            if(a instanceof ListenerThread) {
                ListenerThread thread = (ListenerThread) a;
                IShapListener listener = thread.getListener();
                log.warn(listener.getClass().getName() + " is waitting to stop.");
            }
            log.debug("what's the " + a.getClass().getName());
        });
    }

    public void registListener(String topic, IShapListener listener){
        this.registListener(topic,0, listener);
    }

    public void registListener(String topic,int qos, IShapListener listener){
        shapListenerMap.put(listener.getClass(), listener);
        ListenerThread thread = new ListenerThread(topic, qos, listener);
        pool.submit(thread);
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public int getScanTime() {
        return scanTime;
    }

    public void setScanTime(int scanTime) {
        this.scanTime = scanTime;
    }

}
