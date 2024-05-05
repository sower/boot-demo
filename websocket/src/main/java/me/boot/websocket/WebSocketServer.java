package me.boot.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * WebSocketServer
 *
 * @since 2023/06/28
 */
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 用来存放每个客户端对应的 WebSocketServer 对象
     */
    private static final ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收 userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSocketMap.remove(userId);
        webSocketMap.put(userId, this);
        log.info("用户连接:" + userId + ",当前在线人数为:" + webSocketMap.size());
        try {
            sendMessage("连接成功！");
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(userId);
        log.info("用户退出:" + userId + ",当前在线人数为:" + webSocketMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("用户消息:" + userId + ",报文:" + message);
        if (StringUtils.isNotBlank(message)) {
            try {
                switch (message) {
                    case "PING":
                        this.session.getBasicRemote().sendPong(ByteBuffer.wrap("PONG".getBytes()));
                        break;
                    case "PONG":
                        this.session.getBasicRemote().sendPing(ByteBuffer.allocate(0));
                        break;
                    default:
                        sendMessage("receive at " + LocalDateTime.now());
                }

//        JSONObject jsonObject = JSONObject.parseObject(message);
//        jsonObject.put("fromUserId", this.userId);
//        String toUserId = jsonObject.getString("toUserId");
//        if (!StringUtils.isEmpty(toUserId) && webSocketMap.containsKey(toUserId)) {
//          webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
//        } else {
//          log.error("请求的 userId:" + toUserId + "不在该服务器上");
//        }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session /
     * @param error   /
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }

}