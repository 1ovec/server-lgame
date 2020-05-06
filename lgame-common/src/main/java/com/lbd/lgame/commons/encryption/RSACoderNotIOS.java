package com.lbd.lgame.commons.encryption;

import java.io.File;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.lbd.lgame.common.utils.Utils;



/**
 * 非IOS解密专用
 * 
 * @date 2013-4-23
 */
public class RSACoderNotIOS {
    private static final Logger log = LoggerFactory.getLogger(RSACoderNotIOS.class);

    private byte[] key;
    
    /**
     * 构造函数
     * @throws Exception
     */
    private RSACoderNotIOS() {
    	
    	
        try {
        	ClassPathResource classPathResource = new ClassPathResource("/static/key/private_key.dat");
        	Long len=classPathResource.getFile().length();
        	InputStream inputStream =classPathResource.getInputStream();
            this.key = Utils.readFileIO(inputStream,len);
        } catch (Exception e) {
            log.error("读取秘钥文件错误", e);
        }
    }
    
    /**
     * 解密过程
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public byte[] decrypt(byte[] cipherData) throws Exception {
        if (this.key == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSACoder.KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherData);
    }
    
    public byte[] encrypt(byte[] cipherData) throws Exception {
        if (this.key == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(RSACoder.KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(cipherData);
    }
    
    /**
     * Initialization on Demand Holder单例模式
     * @date 2014-10-20
     */
    private static class RSACoderNotIOSHolder{
        public final static RSACoderNotIOS instance = new RSACoderNotIOS();
    }

    /**
     * 获取实例
     * @return
     */
    public static synchronized RSACoderNotIOS getInstance() {
        return RSACoderNotIOSHolder.instance;
    }
}
