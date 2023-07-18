package com.example.treehole.Util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class TreeholeUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密，不能解密，但相同内容加密结果相同
    // 因此在加密时，在原本密码后加上一个随机串再加密增加复杂性，此处的key即每个用户的密码加上salt
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
