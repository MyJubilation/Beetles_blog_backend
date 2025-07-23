package com.beetles.ws;

import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {

    // 存储客户端ID到WebSocket会话的映射
    private static ConcurrentHashMap<String, Session> clientMap = new ConcurrentHashMap<>();

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        log.info("[WebSocket] 有新的连接，总数：{}", webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        // 从映射中移除
        clientMap.entrySet().removeIf(entry -> entry.getValue().equals(this.session));
        log.info("[WebSocket] 连接断开，总数：{}", webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("[WebSocket] 收到客户端消息：{}", message);

        try {
            // 解析消息格式: {"type":"register","userId":"3","friendId":"122","context":""} 或 {"type":"message","userId":"3","friendId":"122","context":""}
            JSONObject jsonObject = JSONObject.parseObject(message);
            String type = jsonObject.getString("type");
            String content = jsonObject.getString("content");
            int userId = jsonObject.getInteger("userId");
            int friendId = jsonObject.getInteger("friendId");

            if ("register".equals(type)) {
                // 注册客户端
                clientMap.put(String.valueOf(userId), this.session);
                log.info("客户端 {} 已注册", userId);
            } else if ("message".equals(type)) {
                if(content == null){
                    log.error("消息为空");
                    throw new RuntimeException("消息为空");
                }
                if(String.valueOf(friendId) == null){
                    log.error("目标用户ID为空");
                    throw new RuntimeException("目标用户ID为空");
                }
                // 将消息发送给目标客户端
                // 发送定向消息
                Session targetSession = clientMap.get(String.valueOf(friendId));
                if (targetSession != null && targetSession.isOpen()) {
                    Message msg = new Message("message", String.valueOf(userId), String.valueOf(userId),content);
                    targetSession.getBasicRemote().sendText(JSONObject.toJSONString(msg));
                    log.info("消息已发送给 {}", friendId);
                } else {
                    log.warn("目标客户端 {} 不存在或已断开", friendId);
                }
            }
        } catch (Exception e) {
            log.error("消息处理异常: {}", e.getMessage());
        }
    }

    // 添加发送给指定客户端的方法
//    public static void sendMessageTo(String clientId, String message) {
//        Session session = clientMap.get(clientId);
//        if (session != null && session.isOpen()) {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                log.error("发送消息失败: {}", e.getMessage());
//            }
//        }
//    }

    // 添加广播方法
//    public static void broadcast(String message) {
//        clientMap.forEach((id, session) -> {
//            if (session.isOpen()) {
//                try {
//                    session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    log.error("广播消息失败: {}", e.getMessage());
//                }
//            }
//        });
//    }

    // 消息格式类
    public static class Message {
        private String type; // register, request, response
        private String id;   // 客户端ID
        private String targetId; // 目标客户端ID
        private String content;  // 消息内容

        public Message(String type, String id, String targetId, String content) {
            this.type = type;
            this.id = id;
            this.targetId = targetId;
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
