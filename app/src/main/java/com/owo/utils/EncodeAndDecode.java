package com.owo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密解密
 */
public class EncodeAndDecode {
    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    public static String URLencode(String data) {
        try {
            data = URLEncoder.encode(data, "utf-8");
            data = data.replaceAll("\u0025", "\u005f");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String URLdecode(String data) {
        try {
            data = data.replaceAll("\u005f", "\u0025");
            data = URLDecoder.decode(data.replaceAll("%(?![0-9a-fA-F]{2})", "%25") ,"UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
//
//    /*
//  * 16位MD5加密
//  */
//    public static String getMD5Str(String str) {
//        MessageDigest messageDigest = null;
//
//        try {
//            messageDigest = MessageDigest.getInstance("MD5");
//
//            messageDigest.reset();
//
//            messageDigest.update(str.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            System.out.println("NoSuchAlgorithmException caught!");
//            System.exit(-1);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        byte[] byteArray = messageDigest.digest();
//
//        StringBuffer md5StrBuff = new StringBuffer();
//
//        for (int i = 0; i < byteArray.length; i++) {
//            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
//                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
//            else
//                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//        }
//        //16位加密，从第9位到25位
//        return md5StrBuff.substring(8, 24).toString().toUpperCase();
//    }
//
//    public static String code32To16(String code){
//        return code.substring(8, 24).toString().toUpperCase();
//    }
 /*
	* 32位MD5加密
	*/
public static String getMD5Str(String str) {
    String slat="&*yuwiyuw%%%3i4shefshejkefh?lsjkkshkshjsehfjdtjsafsjwjsdsffdg";
    String base=str+"/"+slat;
    String md5= md5DigestAsHex(base.getBytes());
    return md5;
}
    public static String md5DigestAsHex(byte[] bytes)
    {
        return digestAsHexString("MD5", bytes);
    }
    private static String digestAsHexString(String algorithm, byte[] bytes) {
        char[] hexDigest = digestAsHexChars(algorithm, bytes);
        return new String(hexDigest);
    }

    private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
        byte[] digest = digest(algorithm, bytes);
        return encodeHex(digest);
    }
    private static byte[] digest(String algorithm, byte[] bytes) {
        return getDigest(algorithm).digest(bytes);
    }
    private static MessageDigest getDigest(String algorithm)
    {
        try
        {
            return MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", ex);
        }

    }

    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[32];
        for (int i = 0; i < chars.length; i += 2) {
            byte b = bytes[(i / 2)];
            chars[i] = HEX_CHARS[(b >>> 4 & 0xF)];
            chars[(i + 1)] = HEX_CHARS[(b & 0xF)];
        }
        return chars;
    }

}
