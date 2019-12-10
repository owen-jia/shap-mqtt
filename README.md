# shap-mqtt
Shap是针对Java体系研发的一款mqtt连接client，简单、小巧、稳定、低耗的开源工具。

> @author: owen jia

## 最新版本

```xml
<!-- https://repo.maven.apache.org/maven2/com/github/owen-jia/shap-mqtt/ -->
<dependency>
    <groupId>com.github.owen-jia</groupId>
    <artifactId>shap-mqtt</artifactId>
    <version>0.0.2</version>
</dependency>
```

## Sample

> 在测试模块包中有ShapTest类有详细的测试用例。

订阅，需要继承`IShapListener`接口
```java
public class NcSubscribe implements IShapListener {
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("topic:"+topic);
        System.out.println("id:"+message.getId());
        System.out.println("qos:"+message.getQos());
        System.out.println("context:"+message.getPayload());
    }
}
```

发布，使用类`ShapMqtt.publish(...)`
```java
class Test {
    public static void main(String[] args){
        ShapMqtt shapMqtt = new ShapMqtt("tcp://127.0.0.1:1883");
        shapMqtt.subscribe("hello/+",new NcSubscribe());
        shapMqtt.publish("hello/one",1,"hello world!".getBytes(),false);
    }
}
```
