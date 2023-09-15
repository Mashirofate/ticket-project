package com.tickets.socket;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


/**
 * @author zhengkai.blog.csdn.net
 * 根据活动的id获取当天的入场时间和信息
 */
@ServerEndpoint("/RealTimeOtherServer/{vaId}")
@Component
public class RealTimeOtherServer {

    /**
     * 保存活动的Id
     */
    public static List<String> vaIds = new ArrayList<>();
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    public static ConcurrentHashMap<String, RealTimeOtherServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 活动id
     */
    private String vaId;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("vaId") String vaId) {
        this.session = session;
        this.vaId = vaId;
        vaIds.add(vaId);
        webSocketMap.remove(vaId);
        webSocketMap.put(vaId, this);
    }


    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(vaId)) {
            webSocketMap.remove(vaId);
            vaIds.remove(vaId);
        }
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("用户错误:" + this.vaId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(String vaId, String message) throws IOException {
        if (!StringUtils.isEmpty(vaId) && webSocketMap.containsKey(vaId)) {
            webSocketMap.get(vaId).sendMessage(message);
        } else {
            System.out.println("实时（出入 ，人数）开始推送 客户端" + vaId + ",不在线！");
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //addEndpoint表示添加了一个/socket端点，客户端就可以通过这个端点来进行连接。
        //withSockJS()的作用是开启SockJS支持(指定使用SockJS协议)
        registry.addEndpoint("/socket").setAllowedOrigins("*").withSockJS();
    }

}
