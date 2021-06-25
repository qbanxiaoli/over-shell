package com.service;


import org.springframework.web.socket.WebSocketSession;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/19 16:24
 */
public interface SSHService {

    // 初始化连接
    void initConnection(WebSocketSession session);

    // 处理客户端命令
    void handleCommand(String command, WebSocketSession session);

    // 发送命令执行结果到客户端
    void sendMessage(WebSocketSession session, byte[] bytes);

    // 关闭连接
    void close(WebSocketSession session);

}
