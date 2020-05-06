package com.lbd.lgame.portal.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.lbd.lgame.common.constant.TencentCosParam;
import com.lbd.lgame.common.tools.TencentCosUtil;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.model.PubImage;
import com.lbd.lgame.portal.service.PubImageService;


@Controller
@RequestMapping(value="/portalUpload")
public class UploadCtrl {
	private static final Logger log = LoggerFactory.getLogger(UploadCtrl.class);
	 
	//上传文件限制大小 单位字节(10MB)
	private static final long ALLOWSIZE = 10L*1024*1024;
	@Autowired
	private PubImageService pubImageService;
	
	/**
	 * 上传文件到腾讯云
	 * @param fileType
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadImageToCos", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult<String> uploadToCos(@RequestParam("fileType") String fileType, @RequestParam("file") MultipartFile file) {
		log.info("-----------进入开始上传文件到腾讯云-----------------");
		log.info("文件类型：{}",fileType);
		// 校验上传文件参数
		if (StringUtils.isBlank(fileType)) {
			return new JsonResult<String>(false, "文件类型参数不能为空");
		}
		try {
			if (!file.isEmpty()) {
				// 获取上传文件后缀
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
				log.info("上传文件suffix：{}", suffix);
				// 上传文件大小 单位字节
				Long fileSize = file.getSize();
				log.info("上传文件大小：{}", fileSize);
				if (fileSize > ALLOWSIZE) {
					return new JsonResult<String>(false, "您上传的文件大小已经超出范围");
				}
				String url = TencentCosUtil.uploadFileTencetCosInputStram(file, fileType);
				log.info("上传文件到腾讯云服务器的地址：{}", url);
				if(StringUtils.isBlank(url)) {
					log.info("上传文件到腾讯云服务器的地址返回url为空,上传失败");
					return new JsonResult<String>(false, "上传文件失败，请联系管理员");
				}
				//上传图片成功图片信息入库
				PubImage pubImageInfo=new PubImage();
				pubImageInfo.setImageType(fileType);
				pubImageInfo.setImagePath(url);
				int count =pubImageService.savePubImage(pubImageInfo);
				if(count==0) {
					log.info("上传图片信息入库失败!");
					return new JsonResult<String>(false, "添加数据失败");
				}				
			} else {
				return new JsonResult<String>(false, "请选择上传文件");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.toString());
			return new JsonResult<String>(false, "上传文件出错,请联系管理员！");			
		}
		 return new JsonResult<String>(true, "上传文件成功！");
	}

	/**
	 * IOS上传返回临时秘钥
	 * 
	 * @param fileType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/acquireUploadToCosInfo", method = RequestMethod.POST)
	public JsonResult<Object> acquireUploadToCosInfo(@RequestParam("fileType") String fileType) {
		log.info("-----------进入IOS上传文件返回参数-----------------");
		log.info("文件类型：{}", fileType);
		// 校验上传文件参数
		if (StringUtils.isBlank(fileType)) {
			return new JsonResult<Object>(false).convtData("非法参数");
		}
		// 获取临时密钥
		JSONObject temp = TencentCosUtil.getTempKey();
		if (Utils.isEmpty(temp)) {
			return new JsonResult<Object>(false).convtData("获取临时秘钥异常,请联系管理员");
		}
		// 用户基本信息:解析临时密钥中的相关信息
		String tmpSecretId = temp.getJSONObject("credentials").getString("tmpSecretId");
		log.info("临时秘钥ID:{}", tmpSecretId);
		String tmpSecretKey = temp.getJSONObject("credentials").getString("tmpSecretKey");
		log.info("临时秘钥Key:{}", tmpSecretKey);
		String sessionToken = temp.getJSONObject("credentials").getString("sessionToken");
		log.info("临时秘钥Token:{}", sessionToken);
		BigInteger startTime = temp.getBigInteger("startTime");
		log.info("临时秘钥startTime:{}", startTime);
		BigInteger expiredTime = temp.getBigInteger("expiredTime");
		log.info("临时秘钥expiredTime:{}", expiredTime);
		// 储存桶名称
		String bucket = TencentCosParam.COS_BUCKET_NAME;
		log.info("储存桶名称:{}", bucket);
		String region = TencentCosParam.COS_REGION;
		log.info("区域:{}", region);
		String appId = TencentCosParam.COS_APPID;
		log.info("appId:{}", appId);
		String directory = TencentCosUtil.getFileTencetCosImagePath(fileType);
		log.info("路径:{}", directory);

		// IOS返回参数
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bucket", bucket);
		result.put("Region", region);
		result.put("AppId", appId);
		result.put("directory", directory);
		result.put("tmpSecretId", tmpSecretId);
		result.put("tmpSecretKey", tmpSecretKey);
		result.put("sessionToken", sessionToken);
		result.put("startTime", startTime);
		result.put("expiredTime", expiredTime);
		return new JsonResult<Object>(true,"查询成功").convtData(result);
	}
	
	/**
	 * IOS上传图片入库
	 * @param fileType
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadCosSaveInfo", method = RequestMethod.POST)
	public JsonResult<String> uploadCosSaveInfo(@RequestParam("fileType")String fileType,@RequestParam("url")String url) {
		log.info("----------进入IOS上传图片入库开始-----------------");
		log.info("URL：{}",url);
		log.info("图片类型：{}",fileType);	
		//校验发送短信参数
		if(StringUtils.isEmpty(fileType) || StringUtils.isEmpty(url)){
			return new JsonResult<String>(false, "非法请求参数");
		}		
		//上传图片信息入库
		PubImage pubImageInfo=new PubImage();
		pubImageInfo.setImageType(fileType);
		pubImageInfo.setImagePath(url);
		int count =pubImageService.savePubImage(pubImageInfo);
		if(count==0) {
			log.info("上传图片信息入库失败!");
			return new JsonResult<String>(false, "添加数据失败");
		}
		log.info("----------IOS上传图片入库结束-----------------");
		 return new JsonResult<String>(true, "添加成功");
	}		
}
