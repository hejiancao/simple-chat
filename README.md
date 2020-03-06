# 简单的聊天系统
使用springboot和websocket，前端使用原生html


# 使用
1.添加maven依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
    <version>1.5.12.RELEASE</version>
</dependency>
```
2.创建配置类
````java
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
````
3.编写websocket服务端代码
```java

@ServerEndpoint(value = "/ws")
@Component
public class WebSocketServer {

    @OnOpen
    public void onOpen() {//连接时调用
    }

    @OnMessage
    public void onMessage() {//接收消息时调用
    }

    @OnError
    public void onError() {//连接错误时调用
    }

    @OnClose
    public void onClose() {//连接关闭时调用
    }
    
}
```