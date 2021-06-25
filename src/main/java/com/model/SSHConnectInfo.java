package com.model;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/19 16:37
 */
@Data
public class SSHConnectInfo {

    private WebSocketSession webSocketSession;

    private JSch jSch;

    private Channel channel;

}
