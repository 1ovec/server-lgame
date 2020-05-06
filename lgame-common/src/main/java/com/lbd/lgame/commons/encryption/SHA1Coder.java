/**
 * @title SHA1Coder.java
 * @package commons.encryption
 * @description TODO
 * @date 2015-1-12 上午10:53:24
 * @version 1.0.0
 * @company Live By Touch
 */
package com.lbd.lgame.commons.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;


public class SHA1Coder {
    
    /**
     * SHA1加密
     * @param str
     * @return
     *
     * @author Joakim
     * @date 2015-1-12
     * @version 1.0.0
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     */
    /*public static String encodeStr(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
        mdTemp.update(str.getBytes(Params.CHARSET));

        byte[] md = mdTemp.digest();
        int j = md.length;
        char buf[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
            buf[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(buf);
    }*/
    
    /**
     * SHA1加密
     * @param str
     * @return
     *
     * @author Joakim
     * @date 2015-1-12
     * @version 1.0.0
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     */
    public static String encode(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String signature = "";
        
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(str.getBytes("UTF-8"));
        signature = byteToHex(crypt.digest());
        return signature;
    }
    
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
