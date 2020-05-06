package com.lbd.lgame.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbd.lgame.common.utils.Cipher;
import com.lbd.lgame.common.utils.Client;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.commons.encryption.Base64Coder;
import com.lbd.lgame.commons.encryption.RSACoder;
import com.lbd.lgame.mapper.UserMapper;
import com.lbd.lgame.portal.dao.TestMapper;

@Service
public class TestService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TestMapper testMapper;
	
	public int testSQL() {
		return userMapper.testUserCount();
	}
	
	public String testInfo() {
		return testMapper.testInfo();
	}
	
	
	
	public static void main(String [] args) throws Exception{
		Client clientType = Client.UI;
		String pwd = "123456";
		byte[] bs = RSACoder.encryptByPublicKey(pwd.getBytes(),
				Utils.readFile("/Users/zhouhua/key/public_key.dat"));
		String encrptPwd = Base64Coder.encodeBytes(bs);
		System.out.println("bbbbb: "+Cipher.decryptCipher(encrptPwd, clientType));
		System.out.println("aaaa: "+encrptPwd);
	}

}
