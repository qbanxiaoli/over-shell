package com.model;

import lombok.Data;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/19 20:04
 */
@Data
public class SSHData {

    private String operate;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String command;


}
