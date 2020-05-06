package com.ldb.lgame.sms.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.lbd.lgame.common.tools.DateUtils;
import com.lbd.lgame.common.tools.JsonUtil;
import com.lbd.lgame.common.tools.Sha256Util;
import com.ldb.lgame.sms.model.resp.SendSms;
import com.ldb.lgame.sms.model.resp.SendSmsData;
import com.ldb.lgame.sms.model.resp.SendSmsRespData;


@Component
public class SmsUtils {
	private static final Logger log = LoggerFactory.getLogger(SmsUtils.class);
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 短信发送测试IRL
	 */
	@Value("${smsParam.smsUrl}")
	public String smsUrl;

	/**
	 * 短信发送合作方ID
	 */
	@Value("${smsParam.smsPartnerId}")
	public String smsPartnerId;

	/**
	 * 短信发送应用编号
	 */
	@Value("${smsParam.smsAppId}")
	public String smsAppId;

	/**
	 * 短信发送链接秘钥
	 */
	@Value("${smsParam.smsAccesskeysecret}")
	public String smsAccesskeysecret;

	/**
	 * 测试环境跳过短信验证码发送
	 */
	@Value("${smsParam.smsAllowsendpm}")
	public Boolean smsAllowsendpm;
	
	// 格式
	public static final String SMS_FORMAT = "json";
	// 字符编码
	public static final String SMS_CHARSET = "UTF-8";
	// 标签类型
	public static final String SMS_SIGNTYPE = "SHA256";
	// 版本
	public static final String SMS_VERSION = "V1.0";
	// 手机号码的国际区号
	public static final String SMS_PHONE_CODE = "86";	
	
	/**
	 * 短信发送Util
	 * @param phoneNo
	 * @param templateCode
	 * @param verifyCode
	 * @param waterId
	 * @return
	 */
	public  SendSmsRespData<SendSmsData<List<SendSms>>> sendVerifySms(String phoneNo,String templateCode,String verifyCode,String waterId){		
		log.info("-------------进入短信发送开始---------------");
		log.info("发送手机号:{},模板代码:{},验证码:{},流水号:{}",phoneNo ,templateCode,verifyCode,waterId);
		//测试环境跳过短信验证码发送
		if(smsAllowsendpm){
			log.info("------------------测试环境跳过短信验证码发送----------------");
			String 	testResult="{\"msg\":\"success\",\"responseData\":{\"data\":[{\"detail\":\"发送成功\",\"phone\":\"18570414883\",\"waterId\":\"9AE322C7F33E1002E0536432A8C0783F\",\"code\":\"1\"}],\"bizCode\":\"200\",\"bizMsg\":\"提交成功\"},\"code\":\"10000\"}";
			//转换
			Type type = new TypeToken<SendSmsRespData<SendSmsData<List<SendSms>>>>() {
			}.getType();
			SendSmsRespData<SendSmsData<List<SendSms>>> testRespData = JsonUtil.fromJson(testResult, type);
			return testRespData;
		}
		
		// 中台具体的业务参数
		JSONObject object = new JSONObject();
		// 合作方ID
		object.put("partnerId", smsPartnerId);
		///模板代码
    	object.put("templateCode",templateCode);
    	//定义模板内容及短信验证码
    	Map<String, String> templateContent=new HashMap<String, String>();
    	templateContent.put("#code#", verifyCode);   	
    	//模板内容 
    	object.put("templateContent",templateContent);
    	
    	//定义短信内容
    	Map<String, String> contexts=new HashMap<String, String>(); 
    	contexts.put("phonecode",SMS_PHONE_CODE);
    	contexts.put("phone", phoneNo);
    	contexts.put("waterId",waterId);    	
    	List<Map<String, String>> contextsList=new ArrayList<Map<String, String>>();
    	contextsList.add(contexts);    	
    	//短信内容
    	object.put("contexts",contextsList);
		  	
		String bizContent = object.toJSONString();
		// 获取14位系统当前时间
		String timestamp = DateUtils.getSysTime14();

		// 中台的公共请求参数(系统-用户参数)
		Map<String, String> params = new HashMap<String, String>();
		// 短信发送应用编号
		params.put("appId", smsAppId);
		params.put("format", SMS_FORMAT);
		params.put("charset", SMS_CHARSET);
		params.put("signType", SMS_SIGNTYPE);
		params.put("timestamp", timestamp);
		params.put("version", SMS_VERSION);
		params.put("bizContent", bizContent);
		log.info("短信发送排序前参数:{}", JSONObject.toJSONString(params));
		
		try {
			// 拼接参数排序
			StringBuffer sbf = orderKey(params);
			// 拼接短信发送连接秘钥
			sbf.append(smsAccesskeysecret);
			// 请求头加密
			log.info("短信发送加密前字符串:{}", sbf);
			String signStr = Sha256Util.getSHA256(sbf.toString());
			log.info("短信发送加密后字符串:{}", signStr);
			// post请求
			String result = httpPost(smsUrl, signStr, params);
			log.info("短信发送接口json返回:{}",result);
			//转换
			Type type = new TypeToken<SendSmsRespData<SendSmsData<List<SendSms>>>>() {
			}.getType();
			SendSmsRespData<SendSmsData<List<SendSms>>> messageRespData = JsonUtil.fromJson(result, type);
			//返回
			return messageRespData;		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("短信发送异常：{}",e);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			log.error("短信发送异常：{}",e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error("短信发送异常：{}",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("短信发送异常：{}",e);
		}
		return null;
	}

	 /**
     *  @MethodName: orderKey
     * @Description: 排序
     * @Param:
     * @Return:
     **/
    private static StringBuffer orderKey(Map<String, String> object) throws Exception {
        //对参数排序
        StringBuffer sbf = new StringBuffer();
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(object.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for (Map.Entry<String, String> model : list) {
            sbf.append(model.getKey() + "=" + model.getValue() + "&");
        }
        return sbf;
    }
    
    /**
     * @param url 请求地址
     * @param signStr 密文
     * @param map 参数对象
     * @throws Exception
     */
    private  String httpPost(String url,String signStr,Map<String,String> map)throws Exception{
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("appId", map.get("appId"));
        postParameters.add("format", map.get("format"));
        postParameters.add("charset", map.get("charset"));
        postParameters.add("signType", map.get("signType"));
        postParameters.add("timestamp", map.get("timestamp"));
        postParameters.add("version", map.get("version"));
        postParameters.add("bizContent", map.get("bizContent"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", signStr);
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);      
        String result = restTemplate.postForObject(url, r, String.class);
        return result;
    } 
}
