package com.lbd.lgame.auth.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.lbd.lgame.common.tools.DateUtils;
import com.lbd.lgame.common.tools.Sha256Util;


@Component
public class CreditUtils {
	
	private static final Logger log = LoggerFactory.getLogger(CreditUtils.class);
	
	//路径 
	@Value("${creditParam.creditUrl}")
	private String creditUrl;
	
	//合作方ID
	@Value("${creditParam.creditPartnerId}")
	private String partnerId;
	
	//征信应用编号
	@Value("${creditParam.creditAppId}")
	private String appId;
	
	//征信连接秘钥
	@Value("${creditParam.creditAccesskeySecret}")
	private  String accesskeySecret;
	
	//身份证查询接口
	@Value("${creditParam.creditVerifyIdCardUrl}")
	private String verifyIdCardUrl;
	
	//银行卡2要素验证
	@Value("${creditParam.creditBankVerifyUrl}")
	private  String bankVerifyUrl;
	
	//银行卡4要素认证
	@Value("${creditParam.creditBankVerifyFourUrl}")
	private String bankVerifyFourUrl;
	
	// 格式
	public static final String CREDIT_FORMAT = "json";
	// 字符编码
	public static final String CREDIT_CHARSET = "UTF-8";
	// 标签类型
	public static final String CREDIT_SIGNTYPE = "SHA256";
	// 版本
	public static final String CREDIT_VERSION = "V1.0";
	
	@Autowired
	private  RestTemplate restTemplate;
	

	/**
	 * 实名认证校验
	 * @param usernm  姓名
	 * @param certseq 身份证号
	 * @return
	 */
	public  String checkSid(String usernm, String certseq) {
		log.info("---------------实名认证校验开始-----------------");
		log.info("姓名：{}", usernm);
		log.info("身份证号：{}", certseq);
		// 中台具体的业务参数
		JSONObject object = new JSONObject();
		// 合作方ID
		object.put("partnerId", partnerId);
		// 姓名及身份证号
		object.put("name", usernm);
		object.put("idcard", certseq);
		String bizContent = object.toJSONString();
		// 获取14位系统当前时间
		String timestamp = DateUtils.getSysTime14();

		// 中台的公共请求参数(系统-用户参数)
		Map<String, String> params = new HashMap<String, String>();
		// 征信应用编号
		params.put("appId", appId);
		params.put("format", CREDIT_FORMAT);
		params.put("charset", CREDIT_CHARSET);
		params.put("signType", CREDIT_SIGNTYPE);
		params.put("timestamp", timestamp);
		params.put("version", CREDIT_VERSION);
		params.put("bizContent", bizContent);
		log.info("排序前参数:{}", JSONObject.toJSONString(params));

		try {
			// 拼接参数排序
			StringBuffer sbf = orderKey(params);
			// 拼接征信连接秘钥
			sbf.append(accesskeySecret);
			// 请求头加密
			log.info("加密前字符串:{}", sbf);
			String signStr = Sha256Util.getSHA256(sbf.toString());
			log.info("加密前字符串:{}", signStr);
			// post请求
			String result = httpPost(verifyIdCardUrl, signStr, params);
			log.info("实名认证校验返回json:{}", result);
			//如果实体类为空则替换为 data:{} 
			if (result.indexOf("\"data\":\"\"") != -1) {				
				String newResult = result.replace("\"data\":\"\"", "\"data\":{}");
				//返回替换自后的字符串
				return newResult;
			}
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("实名认证校验异常：{}",e);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			log.error("实名认证校验异常：{}",e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error("实名认证校验异常：{}",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("实名认证校验异常：{}",e);
		}
		return null;
	}

	/**
	 * 银行卡2要素校验
	 * 
	 * @param realName
	 *            姓名
	 * @param accountCode
	 *            卡号
	 * @return
	 */
	public  String checkBankCard(String realName, String accountCode) {
		log.info("-------------银行卡两要素认证开始---------------");
		log.info("姓名：{}", realName);
		log.info("入账卡号：{}", accountCode);
		// 中台具体的业务参数
		JSONObject object = new JSONObject();
		// 合作方ID
		object.put("partnerId", partnerId);
		// 姓名及银行卡号
		object.put("name", realName);
		object.put("acctno", accountCode);
		String bizContent = object.toJSONString();
		// 获取14位系统当前时间
		String timestamp = DateUtils.getSysTime14();
		
		// 中台的公共请求参数(系统-用户参数)
		Map<String, String> params = new HashMap<String, String>();
		// 征信应用编号
		params.put("appId", appId);
		params.put("format", CREDIT_FORMAT);
		params.put("charset", CREDIT_CHARSET);
		params.put("signType", CREDIT_SIGNTYPE);
		params.put("timestamp", timestamp);
		params.put("version", CREDIT_VERSION);
		params.put("bizContent", bizContent);
		log.info("排序前参数:{}", JSONObject.toJSONString(params));

		try {
			// 拼接参数排序
			StringBuffer sbf = orderKey(params);
			// 拼接征信连接秘钥
			sbf.append(accesskeySecret);
			// 请求头加密
			log.info("加密前字符串:{}", sbf);
			String signStr = Sha256Util.getSHA256(sbf.toString());
			log.info("加密前字符串:{}", signStr);
			// post请求
			String result = httpPost(bankVerifyUrl, signStr, params);
			log.info("银行卡2要素校验返回json:{}", result);
			//如果实体类为空则替换为 data:{} 
			if (result.indexOf("\"data\":\"\"") != -1) {				
				String newResult = result.replace("\"data\":\"\"", "\"data\":{}");
				//返回替换自后的字符串
				return newResult;
			}
			return result;
		} catch (IOException e) {
			log.error("银行卡2要素校验异常：{}",e);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			log.error("银行卡2要素校验异常：{}",e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error("银行卡2要素校验异常：{}",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("银行卡2要素校验异常：{}",e);
		}
		return null;
	}
	
	/**
	 * 银行卡4要素认证
	 * @param realName  姓名
	 * @param idCard  身份证
	 * @param accountCode  卡号
	 * @param userTel  电话
	 * @return
	 */
	public  String bankVerifyFour(String name,String idCard,String acctno,String userTel){
		log.info("-------------进入银行卡44444444要素认证开始---------------");
		log.info("姓名：{}",name);
		log.info("信用卡卡号：{}",acctno);
		log.info("身份证号：{}", idCard);
		log.info("预留电话:{}", userTel);
		// 中台具体的业务参数
		JSONObject object = new JSONObject();
		// 合作方ID
		object.put("partnerId", partnerId);
		// 姓名、手机号码、身份证号码、
		object.put("name", name);
		object.put("phoneno", userTel);
		object.put("idcard", idCard);
		object.put("acctno", acctno);
		String bizContent = object.toJSONString();
		// 获取14位系统当前时间
		String timestamp = DateUtils.getSysTime14();

		// 中台的公共请求参数(系统-用户参数)
		Map<String, String> params = new HashMap<String, String>();
		// 征信应用编号
		params.put("appId", appId);
		params.put("format", CREDIT_FORMAT);
		params.put("charset", CREDIT_CHARSET);
		params.put("signType", CREDIT_SIGNTYPE);
		params.put("timestamp", timestamp);
		params.put("version", CREDIT_VERSION);
		params.put("bizContent", bizContent);
		log.info("排序前参数:{}", JSONObject.toJSONString(params));
		
		try {
			// 拼接参数排序
			StringBuffer sbf = orderKey(params);
			// 拼接征信连接秘钥
			sbf.append(accesskeySecret);
			// 请求头加密
			log.info("加密前字符串:{}", sbf);
			String signStr = Sha256Util.getSHA256(sbf.toString());
			log.info("加密前字符串:{}", signStr);
			// post请求
			String result = httpPost(bankVerifyFourUrl, signStr, params);
			log.info("银行卡4要素校验json返回:{}",result);
			//如果json中data类为空则替换为 data:{} 
			if (result.indexOf("\"data\":\"\"") != -1) {				
				String newResult = result.replace("\"data\":\"\"", "\"data\":{}");
				//返回替换后的字符串
				return newResult;
			}
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("银行卡4要素校验异常：{}",e);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			log.error("银行卡4要素校验异常：{}",e);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error("银行卡4要素校验异常：{}",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("银行卡4要素校验异常：{}",e);
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
