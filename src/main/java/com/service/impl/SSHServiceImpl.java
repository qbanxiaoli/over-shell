package com.service.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.model.SSHConnectInfo;
import com.model.SSHData;
import com.service.SSHService;
import com.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/19 16:24
 */
@Slf4j
@Service
@AllArgsConstructor
public class SSHServiceImpl implements SSHService {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // 存放ssh连接信息的map
    private static Map<String, Object> map = new ConcurrentHashMap<>();

    @Override
    public void initConnection(WebSocketSession session) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setJSch(jSch);
        sshConnectInfo.setWebSocketSession(session);
        String uuid = String.valueOf(session.getAttributes().get("uuid"));
        //将这个ssh连接信息放入map中
        map.put(uuid, sshConnectInfo);
    }

    @Override
    public void handleCommand(String content, WebSocketSession session) {
        SSHData sshData = JsonUtil.praseObject(content, SSHData.class);
        String userId = String.valueOf(session.getAttributes().get("uuid"));
        if ("connect".equals(sshData.getOperate())) {
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) map.get(userId);
            //启动线程异步处理
            SSHData finalWebSSHData = new SSHData();
            threadPoolTaskExecutor.execute(() -> {
                try {
                    connectToSSH(sshConnectInfo, finalWebSSHData, session);
                } catch (JSchException | IOException e) {
                    log.error("webssh连接异常");
                    log.error("异常信息:{}", e.getMessage());
                    close(session);
                }
            });
        } else if ("command".equals(sshData.getOperate())) {
            String command = sshData.getCommand();
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) map.get(userId);
            if (sshConnectInfo != null) {
                try {
                    this.transToSSH(sshConnectInfo.getChannel(), command);
                } catch (IOException e) {
                    log.error("webssh连接异常");
                    log.error("异常信息:{}", e.getMessage());
                    close(session);
                }
            }
        } else {
            log.error("不支持的操作");
            close(session);
        }
    }

    @Override
    public void sendMessage(WebSocketSession session, byte[] bytes) {
//        session.send(new TextMessage(buffer));
    }

    @Override
    public void close(WebSocketSession session) {
        String userId = String.valueOf(session.getAttributes().get("uuid"));
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) map.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) sshConnectInfo.getChannel().disconnect();
            //map中移除
            map.remove(userId);
        }
    }

    private void connectToSSH(SSHConnectInfo sshConnectInfo, SSHData webSSHData, WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session;
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        //获取jsch的会话
        session = sshConnectInfo.getJSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        session.setConfig(config);
        //设置密码
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(30000);

        //开启shell通道
        Channel channel = session.openChannel("shell");

        //通道连接 超时时间3s
        channel.connect(3000);

        //设置channel
        sshConnectInfo.setChannel(channel);

        //转发消息
        transToSSH(channel, "\r");

        //读取终端返回的信息流
        InputStream inputStream = channel.getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }

        } finally {
            //断开连接后关闭会话
            session.disconnect();
            channel.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }

}
