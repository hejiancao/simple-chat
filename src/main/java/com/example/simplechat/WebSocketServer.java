package com.example.simplechat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/ws")
@Component
public class WebSocketServer {

    /**
     * 记录在线链接数
     */
    private static AtomicInteger onLineNum = new AtomicInteger(0);

    /**
     * concurrent包的线程安全set，存放每个客户端连接对应的webSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();


    /**
     * 与某个客户端的会话，通过会话给客户端传递消息
     */
    private Session session;

    /**
     * 开启一个客户端对话连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        sendMsgToAll("欢迎【" + session.getId() + "】加入聊天室");
    }

    /**
     * 当连接断开时调用的方法
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);
        subOnlineCount();
        System.out.println("有人走了");
    }

    /**
     * 当有来自客户端的消息时
     *
     * @param msg
     * @param session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        for (WebSocketServer ws : webSocketSet) {
            try {
                ws.session.getBasicRemote().sendText("来自访客" + session.getId() + "的消息:" + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {

        try {
            session.getBasicRemote().sendText(error.getMessage());
        } catch (IOException e) {
            error.printStackTrace();
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg, Session session) {

        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMsgToAll(String msg) {

        try {
            for (WebSocketServer ws : webSocketSet) {
                ws.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getOnlineCount() {
        return onLineNum.get();
    }

    public static void addOnlineCount() {
        onLineNum.incrementAndGet();
    }

    public static synchronized void subOnlineCount() {
        onLineNum.decrementAndGet();
    }
}