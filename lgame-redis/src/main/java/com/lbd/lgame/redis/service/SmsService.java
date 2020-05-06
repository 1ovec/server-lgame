package com.lbd.lgame.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lbd.lgame.common.tools.DateUtils;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.mapper.PubBaseDicMapper;
import com.lbd.lgame.redis.tools.RedisService;



@Service
public class SmsService {
	private static final Logger log = LoggerFactory.getLogger(SmsService.class);
	@Autowired
	private RedisService redisService;
	@Autowired
	private PubBaseDicMapper baseDicDao;
	//短信发送的次数
	 @Value("${smsRedis.sendCountKeyPrifix}")
	private String sendCountKeyPrifix;
	//redis 验证码保存前缀KEY
	 @Value("${smsRedis.verificationCodeKeyPrefix}")
    private String verificationCodeKeyPrefix; 
	
	 
	/**
	 * 短信校验发送次数
	 * @param phone
	 * @param num
	 * @param smsTypem
	 * @param TempletTime
	 * @return
	 */
	public JsonResult<String> verifyPhoneTimes(String phone) {	
		//拼接次数key前缀
    	String countKey = sendCountKeyPrifix + phone;   	
    	//获取短信发送的次数
    	Long count = redisService.getIncrValue(countKey);
    	log.info("{}短信发送次数为：{}",phone,count);
    	if(count!=null) {  
    		//一天内同一手机号最大发送次数
    		int num=baseDicDao.queryBaseDicSmsCount();
    		if(count >= num){    			
    			log.error("{}短信发送次数超限：{}",phone,count);
    			return new JsonResult<String>(false,"短信发送次数超限");
            }
    	}	    	
    	 return new JsonResult<String>(true,"验证成功");
	}
	
	
	/**
	 * 设置验证码超时时间 累计短信发送次数
	 * @param phone
	 * @param smsTypem
	 * @param templetTime
	 * @param verifyCode
	 * @return
	 */
	public JsonResult<String> smsIncr(String phone,String smsTypem,Integer templetTime,String verifyCode) {				    		
		//插入redis :设置过期时间,过期时间存放在模板表里
    	Integer time = templetTime;
    	redisService.set(verificationCodeKeyPrefix + smsTypem + phone, verifyCode, time * 60);    	    	
    	//redis 计数
    	redisService.incr(sendCountKeyPrifix + phone, 1);
    	//是否设置过过期时间
    	if(redisService.getExpire(sendCountKeyPrifix + phone) == 0 || redisService.getExpire(sendCountKeyPrifix + phone) == -1) {
    		redisService.expire(sendCountKeyPrifix + phone, DateUtils.getDayEndTime());
    	}    
    	 return new JsonResult<String>(true,"验证成功");
	}

    /**
     * 验证码判断
     * @param 
     * @param  
     * @param  
     * @return
     */
    public JsonResult<String> checkSmsVerifyCode(String phone,String verifyCode,String smsType){
    		log.info("================校验短信验证码开始================");
    		log.info("手机号:{},验证码:{},短信类型:{}",phone,verifyCode,smsType);   	
    		boolean success = true;
    		success = smsCheckVerificationCode(phone,verifyCode,smsType);
		if(success) {
			log.info("<<<<<<<<<<<<<<验证码校验成功>>>>>>>>>>>>>>");
			return new JsonResult<String>(true,  "校验成功!");
		}else{
			return new JsonResult<String>(false,  "验证码无效");
		}
    }
	
	

    /**
     * 短信验证码校验
     * @param phone
     * @param verifyCode
     * @param smsTypeme
     * @return
     */
    public boolean smsCheckVerificationCode(String phone,String verifyCode,String smsType) {
    	//去redis查询验证码  key:REDIS_VERIFICATION_CODE_KEY_PREFIX+businessType+phone   value:验证码
    	String code = (String)redisService.get(verificationCodeKeyPrefix + smsType + phone);
    	if(code==null || StringUtils.isEmpty(verifyCode) || !verifyCode.trim().equals(code)) {
    		return false;
    	}
    	return true;
    }
}
