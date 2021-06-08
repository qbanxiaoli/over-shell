package com.util;

import java.util.UUID;

/**
 * @author Q版小李
 * @description
 * @create 2021/3/19 17:00
 */
public class UUIDUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
