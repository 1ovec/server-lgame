package com.lbd.lgame.commons.encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;



/**
 * IOS解密专用
 * 
 * @date 2013-4-23
 */
public class RSACoderByIOS {
    private static final Logger log = LoggerFactory.getLogger(RSACoderByIOS.class);

    private RSAPrivateKey privateKey;
    
    /**
     * 构造函数
     * @throws IOException 
     * @throws Exception
     */
    private RSACoderByIOS()  {
    	
//        String privateKeyFilePath = "/static/key/private_key.pem";
//        String privateKeyFilePath = "key/rsa/pkcs8_private_key.pem";
        try {
        	ClassPathResource classPathResource = new ClassPathResource("/static/key/private_key.pem");
        	InputStream inputStream =classPathResource.getInputStream();
            loadPrivateKeyByFile(inputStream);
        } catch (Exception e) {
            log.error("读取秘钥文件错误", e);
        }
    }
    
    /**
     * 解析私钥文件
     * @param privateKey
     * @throws Exception
     */
    private void loadPrivateKey(String privateKey) throws Exception {
        try {
            byte[] buffer = Base64Coder.decodeStr(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSACoder.KEY_ALGORITHM);
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 从文件中输入流中加载私钥
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    private void loadPrivateKeyByFile(InputStream inputStream) throws Exception {
        BufferedReader br = null;
        String readLine = null;
        StringBuilder sb = null;


        try {
          //  in = RSACoderByIOS.class.getClassLoader().getResourceAsStream(filePath);
            br = new BufferedReader(new InputStreamReader(inputStream));
            
            sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        } finally {
            if (null != inputStream) {
            	inputStream.close();
            }
        }
    }

    /**
     * 解密过程
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public byte[] decrypt(byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        
        byte[] output = null;
        Cipher cipher = null;
        
        try {
            cipher = Cipher.getInstance(RSACoder.KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            output = cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            log.error("数据填充错误");
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
        
        return output;
    }
    
    /**
     * Initialization on Demand Holder单例模式
     * @date 2014-10-20
     */
    private static class RSACoderByIOSHolder {
        public final static RSACoderByIOS instance = new RSACoderByIOS();
    }
    
    /**
     * 获取实例
     * @return
     */
    public static synchronized RSACoderByIOS getInstance() {
        return RSACoderByIOSHolder.instance;
    }
}
