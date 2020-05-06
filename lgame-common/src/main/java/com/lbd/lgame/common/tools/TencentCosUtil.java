package com.lbd.lgame.common.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.lbd.lgame.common.constant.TencentCosParam;
import com.lbd.lgame.commons.encryption.MD5Coder;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;

public class TencentCosUtil {
	private static Logger log = LoggerFactory.getLogger(TencentCosUtil.class);
	// 储存桶名称
	private static final String BUCKET_NAME=TencentCosParam.COS_BUCKET_NAME;
	// 区域北京则 beijing
	private static final String REGION=TencentCosParam.COS_REGION;
	// APPID
	private static final String APPID=TencentCosParam.COS_APPID;
	// 调用者身份
	private static final String SECRETID=TencentCosParam.COS_SECRETID;
	// 秘钥
	private static final String SECRETKEY=TencentCosParam.COS_SECRETKEY;	
	// 上传项目名称
    private static final String PROJECT_NAME=TencentCosParam.COS_PROJECT_NAME;
    //腾讯云的allowPrefix(允许上传的路径)
    private static final String ALLOW_PREFIX=TencentCosParam.COS_ALLOW_PREFIX;
    //腾讯云的临时密钥时长(单位秒)
    private static final String DURATIONSECONDS=TencentCosParam.COS_DURATIONSECONDS;
	
	/**
	 * 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用 将本地文件上传到cos
	 * 
	 * @param key
	 * @param filepath
	 * @return
	 */
	public static String SimpleUploadFileFromLocal(String key, File localFile) {
		// 调用者的身份
		 COSCredentials cred = new BasicCOSCredentials(SECRETID, SECRETKEY);
		// 设置储存桶区域
		 ClientConfig clientConfig = new ClientConfig(new Region(REGION));
		// 3 生成cos客户端
		COSClient cosClient = new COSClient(cred, clientConfig);
		// 判断文件目录及文件是否存在
		if (!localFile.exists()) {
			log.error("上传到cos的文件为空!");
			return null;
		}
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, localFile);
		// 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
		putObjectRequest.setStorageClass(StorageClass.Standard);
		try {
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
			// 成功：putobjectResult 会返回文件的 etag
			String etag = putObjectResult.getETag();
			if (!StringUtils.isEmpty(etag)) {
				// 返回url
				return "https://" + BUCKET_NAME + ".cos." + REGION + ".myqcloud.com/" + key;
			}
			log.info("获取返回文件的地址失败 ,etag:{}", etag);
		} catch (CosServiceException e) {
			log.error("从输入流读取并上传到cos异常：{}", e);
		} catch (CosClientException e) {
			log.error("从输入流读取并上传到cos异常：{}", e);
		} finally {
			// 关闭客户端
			cosClient.shutdown();
		}
		return null;
	}

	/**
	 * 从输入流读取并上传到cos
	 * 
	 * @param key
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static String SimpleUploadFileFromStream(String key, InputStream inputStream, String contentType)
			throws IOException {
		// 调用者的身份
		COSCredentials cred = new BasicCOSCredentials(SECRETID, SECRETKEY);
		// 设置储存桶区域
		ClientConfig clientConfig = new ClientConfig(new Region(REGION));
		// 生成cos客户端
		COSClient cosClient = new COSClient(cred, clientConfig);
		// bucket名需包含appid
		ObjectMetadata objectMetadata = new ObjectMetadata();
		// 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
		objectMetadata.setContentLength(Integer.valueOf(inputStream.available()).longValue());
		// 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
		objectMetadata.setContentType(contentType);
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, inputStream, objectMetadata);
		// 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
		putObjectRequest.setStorageClass(StorageClass.Standard);
		try {
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
			// 成功：putobjectResult 会返回文件的 etag
			String etag = putObjectResult.getETag();
			if (!StringUtils.isEmpty(etag)) {
				// 返回url
				return "https://" + BUCKET_NAME + ".cos." + REGION + ".myqcloud.com/" + key;
			}
			log.info("获取返回文件的地址失败 ,etag:{}", etag);
		} catch (CosServiceException e) {
			log.error("从输入流读取并上传到cos异常：{}", e);
		} catch (CosClientException e) {
			log.error("从输入流读取并上传到cos异常：{}", e);
		} finally {
			// 关闭客户端(关闭后台线程)
			cosClient.shutdown();
		}
		return null;
	}

	// 删除cos上指定的对象
	public static String delCos(String key) {
		// 调用者的身份
		COSCredentials cred = new BasicCOSCredentials(SECRETID, SECRETKEY);
		// 设置储存桶区域
		ClientConfig clientConfig = new ClientConfig(new Region(REGION));
		// 生成cos客户端
		COSClient cosClient = new COSClient(cred, clientConfig);
	
		// Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
		cosClient.deleteObject(BUCKET_NAME, key);
		// 关闭客户端(关闭后台线程)
		return "SUCCESS";
	}

	/**
	 * 判断cos文件上传时的contentType
	 * 
	 * @param FilenameExtension
	 * @return
	 */
	public static String contentType(String FilenameExtension) {
		if (FilenameExtension.equals("BMP") || FilenameExtension.equals("bmp")) {
			return "image/bmp";
		}
		if (FilenameExtension.equals("GIF") || FilenameExtension.equals("gif")) {
			return "image/gif";
		}
		if (FilenameExtension.equals("JPEG") || FilenameExtension.equals("jpeg") || FilenameExtension.equals("JPG")
				|| FilenameExtension.equals("jpg") || FilenameExtension.equals("PNG")
				|| FilenameExtension.equals("png")) {
			return "image/jpeg";
		}
		if (FilenameExtension.equals("HTML") || FilenameExtension.equals("html")) {
			return "text/html";
		}
		if (FilenameExtension.equals("TXT") || FilenameExtension.equals("txt")) {
			return "text/plain";
		}
		if (FilenameExtension.equals("VSD") || FilenameExtension.equals("vsd")) {
			return "application/vnd.visio";
		}
		if (FilenameExtension.equals("PPTX") || FilenameExtension.equals("pptx") || FilenameExtension.equals("PPT")
				|| FilenameExtension.equals("ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (FilenameExtension.equals("DOCX") || FilenameExtension.equals("docx") || FilenameExtension.equals("DOC")
				|| FilenameExtension.equals("doc")) {
			return "application/msword";
		}
		if (FilenameExtension.equals("XML") || FilenameExtension.equals("xml")) {
			return "text/xml";
		}
		return "text/html";
	}

	/**
	 * 生成本地临时文件再上传文件到腾讯云Cos
	 * 
	 * @param request
	 * @param file
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public static String uploadFileToTencentCos(HttpServletRequest request, MultipartFile file, String fileType)
			throws Exception {
		log.info("-----------------后台上传文件到腾讯云cos开始------------------------");
		// 当前时间戳
		String curTime = DateUtils.formatDate(new Date(), "yyyymmddhhmmss");
		// 文件file md5加密
		String imageName = MD5Coder.encrypt(file.getName() + curTime);

		String fix = file.getOriginalFilename();
		// 文件后缀名
		String prefix = fix.substring(fix.lastIndexOf(".") + 1);
		log.info("-----文件后缀名----" + prefix);

		log.info("加密之后的的文件名：" + imageName);
		imageName = imageName + "." + prefix;
		log.info("拼接之后的文件名称：" + imageName);

		String dateStr = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		// 获取项目根文件夹拼接操作 根据request对象获取项目的根路径
		String rootSrc = request.getServletContext().getRealPath("/") + "uploadfile" + File.separator + "temp"
				+ File.separator + dateStr;

		log.info("根文件夹路径：" + rootSrc);
		//
		File fileDir = new File(rootSrc);
		if (!fileDir.isDirectory() && !fileDir.exists()) {
			// 多级创建文件夹
			fileDir.mkdirs();
		}
		// 拼接文件绝对路径
		String fileName = rootSrc + File.separator + imageName;
		File srcFile = new File(fileName);
		// 临时保存文件
		file.transferTo(srcFile);
		String key = PROJECT_NAME + "/" + fileType + "/" + dateStr + "/" + srcFile.getName();
		String url = SimpleUploadFileFromLocal(key, srcFile);
		// 上传之后删除本地文件文件
		srcFile.delete();

		log.info("----------------文件上传腾讯云结束返回url------------------");
		// 返回上传成功的文件路径
		return url;
	}

	/**
	 * 
	 * 输入流上传文件到腾讯云
	 * @param file
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public static String uploadFileTencetCosInputStram(MultipartFile file, String fileType)
			throws Exception {
		log.info("-----------------后台上传文件到腾讯云cos开始------------------------");
		// 当前时间戳
		String curTime = DateUtils.formatDate(new Date(), "yyyymmddhhmmss");
		// 文件file md5加密
		String imageName = MD5Coder.encrypt(file.getName() + curTime);
		
		String fix = file.getOriginalFilename();
		// 文件后缀名
		String prefix = fix.substring(fix.lastIndexOf(".") + 1);
		log.info("-----文件后缀名----" + prefix);

		log.info("加密之后的的文件名：" + imageName);
		imageName = imageName + "." + prefix;
		log.info("拼接之后的文件名称：" + imageName);

		String dateStr = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		// 设置contentType
		String contentType = contentType(prefix);
		log.info("***********contentType:{}", contentType);
		// 文件上传腾讯云
		String key = PROJECT_NAME + "/" + fileType + "/" + dateStr + "/" + imageName;
		String url =SimpleUploadFileFromStream(key, file.getInputStream(), contentType);
		log.info("----------------文件上传腾讯云结束返回url------------------");
		// 返回上传成功的文件路径
		return url;
	}
	
	
	/**
	 * 返回文件上传相对路径
	 * @param fileType
	 * @return
	 */
	public static String getFileTencetCosImagePath(String fileType) {
		String dateStr = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		// 返回文件上传相对路径
		String key = PROJECT_NAME + "/" + fileType + "/" + dateStr + "/";
		return key;
	}
	  
	
	/**
	 * 生成临时秘钥
	 * @return
	 */
	public static JSONObject getTempKey() {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {//使用永久密钥生成临时密钥
            config.put("SecretId", SECRETID);
            config.put("SecretKey", SECRETKEY);
            config.put("durationSeconds", Integer.parseInt(DURATIONSECONDS));
            config.put("bucket", BUCKET_NAME); 
            config.put("region", REGION);
            config.put("allowPrefix", ALLOW_PREFIX);
            //密钥的权限列表，其他权限列表请看
            //https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分片上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            JSONObject credential = CosStsClient.getCredential(config);
            //成功返回临时密钥信息，如下打印密钥信息
            return credential;
        } catch (Exception e) {
            //失败抛出异常
        	log.info("生成临时秘钥异常：{}",e);           
        }      
        return null;
    }
	

	public static void main(String[] args) throws FileNotFoundException, IOException {
				
		System.out.println(getTempKey());
	
	}
}
