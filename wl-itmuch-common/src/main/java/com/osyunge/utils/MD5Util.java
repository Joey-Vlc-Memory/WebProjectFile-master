package com.osyunge.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 用于密码加密
 */

public class MD5Util {

    public static String EncodeByMD5(String pwd){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(pwd.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    private static String toHex(byte[] bytes){
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            sb.append(HEX_DIGITS[(aByte >> 4 & 0x0f)]);
            sb.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return sb.toString();
    }

}
