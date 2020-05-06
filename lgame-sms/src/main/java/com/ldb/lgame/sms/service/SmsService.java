package com.ldb.lgame.sms.service;

import java.util.List;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.StringUtils;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.mapper.PubButtonMapper;
import com.lbd.lgame.mapper.PubSmsMapper;
import com.lbd.lgame.mapper.PubSmsSetMapper;
import com.lbd.lgame.model.PubButton;
import com.lbd.lgame.model.PubSms;
import com.lbd.lgame.model.PubSmsSet;
import com.ldb.lgame.sms.model.resp.SendSms;
import com.ldb.lgame.sms.model.resp.SendSmsData;
import com.ldb.lgame.sms.model.resp.SendSmsRespData;
import com.ldb.lgame.sms.utils.SmsUtils;


@Service
public class SmsService {
	private final static Logger log = LoggerFactory.getLogger(SmsService.class);	
	@Autowired
	private PubButtonMapper buttonDao;
	@Autowired
	private PubSmsMapper smsDao;
	@Autowired
	private PubSmsSetMapper smsSetDao;
	@Autowired
	private SmsUtils smsUtils;
	@Autowired
	private FeignRedisService feignRedisService;
	/**
	 * 短信发送
	 * @param phoneNo
	 * @param smsType
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public JsonResult<String> sendSms(String phoneNo,String smsType)throws Exception {
		int count = 0;
		// 校验发送短信拦截的开关是否打开 03
		PubButton switchButton = buttonDao.queryButtonByDicCode("03","短信发送开关");
		// 发送短信开关不存在
		if (Utils.isEmpty(switchButton)) {
			log.info("--------短信发送拦截开关不存在--------");
			return new JsonResult<String>(false, "短信发送开关不存在，请联系管理员。");
			// 实名认证
		} else if (!"0".equals(switchButton.getStatus())) {
			log.info("--------短信发送拦截开关已打开---------");
			return new JsonResult<String>(false, switchButton.getRemark());
		}
		//根据发送短信的类型获取模板CODE
		PubSmsSet smsSetTemplate=smsSetDao.querySmsSetBySmsType(smsType);
		if (Utils.isEmpty(smsSetTemplate)) {
			log.info("--------短信模板配置为空--------");
			return new JsonResult<String>(false, "短信模板配置不存在，请联系管理员。");			
		}
		//获取随机六位验证码
		String verifyCode=StringUtils.generateRandomString(6);
		log.info("随机生成四位验证码：{}",verifyCode);
		//校验发送次数
		log.info("-------------校验手机号码累计发送次数----------------");
		log.info("验证码有效时间：{}",smsSetTemplate.getExpTime());
		JsonResult<String> redisJsonResult=feignRedisService.verifyPhoneTimes(phoneNo);	
		if(redisJsonResult==null) {
			log.info("调用redis程序接口校验短信发送次数返回失败:{}", redisJsonResult);
			return new JsonResult<String>(false, "校验发送次数获取结果失败。");			
		}else if(false==redisJsonResult.isSuccess()){
			log.info("手机号:{}发送短信次数超出限制,{}",phoneNo, redisJsonResult.getMsg());
			return new JsonResult<String>(false, redisJsonResult.getMsg());
		}
		//短信正文
		String smsContent=smsSetTemplate.getSmsTemplateContent().toString().replace("#code#", verifyCode);
		log.info("短信正文：{}",smsContent);
		//添加短信发送记录
		PubSms smsRecord=new PubSms();
		smsRecord.setIphone(phoneNo);
		smsRecord.setVerifyCode(verifyCode);
		smsRecord.setType(smsType);//短信类型
		smsRecord.setContent(smsContent);
		count+=smsDao.saveSms(smsRecord);		
		// 返回短信ID
		String smsId = smsRecord.getId();
		log.info("smsId:{}", smsId);
		log.info("------调用短信发送接口-------");
		SendSmsRespData<SendSmsData<List<SendSms>>>  messageRespData=smsUtils.sendVerifySms(phoneNo, smsSetTemplate.getSmsTemplate(), verifyCode, smsId);
		if (Utils.isEmpty(messageRespData)) {
			 //修改短信发送返回状态
			PubSms smsNone=new PubSms();
			smsNone.setId(smsId);
			smsNone.setResultStatus("3");
			count+=smsDao.updateSmsById(smsNone);	
			log.info("count:{}",count);
			log.info("发送短信获取接口返回结果失败:{}", messageRespData);
			return new JsonResult<String>(false, "发送短信获取接口返回结果失败。");
		}
		log.info("发送短信接口结果返回:{}", messageRespData);
		// 网关处理成功
		if ("10000".equals(messageRespData.getCode())) {
			log.info("------------发送短信网关处理成功-----------");
			// 认证成功
			if ("200".equals(messageRespData.getResponseData().getBizCode())) {
				log.info("------------发送短信认证成功-------");
				//手机号码发送短信状态
				String bizMsg=messageRespData.getResponseData().getData()!=null && messageRespData.getResponseData().getData().size()>0? messageRespData.getResponseData().getData().get(0).getDetail():"发送成功";
				log.info("手机号码发送短信状态:{}",bizMsg);
				 // 更新短信表状态
                 PubSms smsSuc =new PubSms();			
                 smsSuc.setId(smsId);
                 //返回成功
                 smsSuc.setResultStatus("2");
                 smsSuc.setResultTxt(messageRespData.getResponseData().getBizCode()+"/"+bizMsg);
                 //已发送
                 smsSuc.setStatus("2");
                 //更新状态
                 count+=smsDao.updateSmsById(smsSuc);	
     			 log.info("count:{}",count);     			 
     			 //发送短信成功 设置验证码超时时间 累计短信发送次数
     			 JsonResult<String> redisIncrResult=feignRedisService.smsIncr(phoneNo, smsType, smsSetTemplate.getExpTime(),verifyCode);
				 // 短信发送成功     			 
     			  return new JsonResult<String>(true, bizMsg);
			} else {
				log.info("-------------发送短信失败------------------");
				// 更新短信表状态				
                PubSms smsErr =new PubSms();			
                smsErr.setId(smsId);
                //返回失败
                smsErr.setResultStatus("3");
                smsErr.setResultTxt(messageRespData.getResponseData().getBizCode()+"/"+messageRespData.getResponseData().getBizMsg());
                //更新状态
                count+=smsDao.updateSmsById(smsErr);	
    			 log.info("count:{}",count);
				// 返回短信发送失败信息
				return new JsonResult<String>(false, messageRespData.getResponseData().getBizMsg());
			}
		} else {
			log.info("------------短信发送网关处理失败---------------");
			// 更新短信表状态				
            PubSms smsWrong =new PubSms();			
            smsWrong.setId(smsId);
            //返回失败
            smsWrong.setResultStatus("3");
            smsWrong.setResultTxt(messageRespData.getCode()+"/"+messageRespData.getMsg());
            //更新状态
            count+=smsDao.updateSmsById(smsWrong);	
			 log.info("count:{}",count);
			// 返回实名认证失败信息
			return new JsonResult<String>(false, messageRespData.getMsg());
		}
	}
}
