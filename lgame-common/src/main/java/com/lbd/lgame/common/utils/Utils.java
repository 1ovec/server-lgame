package com.lbd.lgame.common.utils;



import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbd.lgame.commons.encryption.MD5Coder;


public class Utils {
	final static private Logger log = LoggerFactory.getLogger(Utils.class);

	/**
	 * 获取src根目录
	 * 
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-4-11
	 * @version 1.0.0
	 */
	public static String getSrcPath() {
		return Utils.class.getResource("/").getPath().replaceAll("%20", " ");
	}

	/**
	 * 读取配置文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-3-17
	 * @version 1.0.0
	 */
	public static Properties getProperty(String filePath) {
		InputStream in = null;
		Properties properties = null;

		try {
			in = Utils.class.getClassLoader().getResourceAsStream(filePath);

			if (in == null) {
				log.error("[" + filePath + "] --> not exist");
			} else {
				properties = new Properties();
				properties.load(in);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally { // 关闭流
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return properties;
	}

	/**
	 * 获取classes下的配置文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param key
	 * @return
	 * @author junqing.cao
	 * @version 2012-4-23
	 */
	public static String getPropertyVal(String filePath, String key) {
		String value = "";
		Properties properties = getProperty(filePath);
		if (properties != null) {
			value = properties.getProperty(key);
		}
		return value;
	}

	/**
	 * 写入数据(key,value)到classes下的配置文件
	 * 
	 * @param field
	 * @param key
	 * @param value
	 * @author wei.duan
	 * @version 2012-4-23
	 */
	public static void setPropertyVal(String field, String key, String value) {
		InputStream in = null;
		OutputStream out = null;
		Properties properties = null;

		try {
			// in = MyTools.class.getClassLoader().getResourceAsStream(field);
			String path = Utils.class.getClassLoader().getResource("")
					.getPath().substring(1);
			path = path + field;
			in = new FileInputStream(path);
			out = new FileOutputStream(path);

			properties = new Properties();
			properties.load(in);
			properties.setProperty(key, value);
			properties.store(out, "Update'" + key + "'value");

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally { // 关闭流
			try {
				if (properties != null) {
					properties.clear();
				}
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filename
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(String filename) throws IOException {
		File file = null;
		Long len = 0l;
		byte[] bytes = null;
		BufferedInputStream bufferedInputStream = null;

		try {
			if (filename == null || filename.equals("")) {
				throw new NullPointerException("无效的文件路径");
			}
			file = new File(filename);

			len = file.length();
			bytes = new byte[len.intValue()];

			bufferedInputStream = new BufferedInputStream(new FileInputStream(
					file));
			int r = bufferedInputStream.read(bytes);
			if (r != len) {
				throw new IOException("读取文件不正确");
			}
		} catch (Exception e) {
			log.error("读取文件错误", e);
		} finally {
			bufferedInputStream.close();
		}

		return bytes;
	}
	
	/**
	 * 读取文件内容
	 * 
	 * @param filename
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileIO(InputStream inputStream,Long len) throws IOException {		
		byte[] bytes = null;		
		BufferedInputStream bufferedInputStream = null;
		try {
			//字节长度
			bytes = new byte[len.intValue()];
			bufferedInputStream = new BufferedInputStream(inputStream);
			//读取输入流
			int r = bufferedInputStream.read(bytes);
			if (r != len) {
				throw new IOException("读取文件不正确");
			}
		} catch (Exception e) {
			log.error("读取文件错误", e);
		} finally {
			bufferedInputStream.close();
		}		
		return bytes;		
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(Object s) {
		return !(s != null && !"".equals(s) && !"null".equals(s));
	}

	/**
	 * 字符串转换为字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @param splitRegex
	 *            分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(splitRegex);
	}

	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String[] str2StrArray(String str) {
		return str2StrArray(str, ",\\s*");
	}

	/**
	 * 验证邮箱格式
	 */
	public static boolean checkEmail(String email) {
		Pattern pattern = Pattern
				.compile("^[a-zA-Z0-9_\\.]+@[a-zA-Z0-9-]+[\\.a-zA-Z]+$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * 验证输入信息是否为中文
	 */
	public static boolean checkInputChinese(String content) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}
	
	
	/**
	 * 验证输入的日期格式
	 * YYYY-MM-DD格式
	 * @param content
	 * @return
	 */
	public static boolean checkImputDate(String content){
		String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pattern = Pattern.compile(rexp);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}
	
	
	/**
	 * 产生随机码
	 * 
	 * @param num
	 *            多少位随机码
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-10-14
	 * @version 1.0.0
	 */
	public static String getRandom(int num) {
		Random random = new Random();
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < num; i++) {
			str.append(Math.abs(random.nextInt() % 10));
		}

		return str.toString();
	}

	/**
	 * 验证密码长度、特殊字符
	 * 
	 * @param pwd
	 *            要验证的密码
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-10-15
	 * @version 1.0.0
	 */
	public static boolean checkChiper(String pwd) {
		Pattern pattern = Pattern.compile("[\\w|_|.]{8,20}");
		Matcher matcher = pattern.matcher(pwd);
		boolean flag = matcher.matches();
		return flag;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean createField(String path) {
		boolean flag = true;
		File f = new File(path);
		log.info("文件夹路径 --> " + path);

		// 判断文件是否存在
		if (!f.exists()) {
			flag = f.mkdir();
		}

		log.info("判断文件夹 --> " + flag);
		return flag;
	}

	/**
	 * 二进制转字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	/**
	 * 获取当前时间
	 * 
	 * @param pattern
	 *            时间格式化
	 * @return
	 */
	public static String getNowDate(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(Calendar.getInstance().getTime());
	}

	/**
	 * 将String类型的时间YYYYMMDD进行格式化
	 * 
	 * @param date
	 * @return
	 * @author yuhui.yi
	 */
	public static String getDateString(String date) {
		StringBuffer dateStr = new StringBuffer();
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6, 8);
		return dateStr.append(year).append("年").append(month).append("月")
				.append(day).append("日").toString();
	}

	/**
	 * 获取差值天数记录
	 * 
	 * @param pattern
	 * @param diffDays
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-11-24
	 * @version 1.0.0
	 */
	public static String getDateByDiff(String pattern, int diffDays) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, diffDays);
		return sdf.format(c.getTime());
	}

	/**
	 * 获取差值分钟记录
	 * 
	 * @param pattern
	 * @param diffMinutes
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-12
	 * @version 1.0.0
	 */
	public static String getDateByDiffMinutes(String pattern, int diffMinutes) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, diffMinutes);
		return sdf.format(c.getTime());
	}

	private static Random randGen = null;
	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
			.toCharArray();

	/**
	 * 获取随机字符串
	 * 
	 * @param length
	 *            随机字符串的长度
	 * @return
	 */
	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
					.toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 获取UUID
	 * 
	 * @return
	 */
	public static final String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 根据指定长度对数字进行补零
	 */
	public static final String addZero(int num, int length) {
		/*
		 * 0 代表补0 10 代表长度 d 代表参数为正数型
		 */
		// String.format("%010d", num);

		NumberFormat nf = NumberFormat.getInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(length);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(length);
		return nf.format(num);
	}

	/**
	 * 根据概率随机生成指定数字
	 * 
	 * @param list
	 *            0:数字,1:概率
	 * @return int
	 */
	public static int getMoney(List<int[]> list) {
		int length = 0;
		for (int i = 0; i < list.size(); i++) {
			length += list.get(i)[1];
		}
		int num[] = new int[length];
		int k = 0;
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i)[1]; j++) {
				num[k] = list.get(i)[0];
				k++;
			}
		}
		int numnew[] = new int[num.length];
		for (int i = 0; i < numnew.length; i++) {
			numnew[i] = num[new Random().nextInt(numnew.length)];
		}
		int n = new Random().nextInt(numnew.length);
		return numnew[n];
	}

	/**
	 * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
	 * 
	 * @throws IOException
	 */
	public static byte[] readFileToBytes(File file) throws IOException {
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;

		try {
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("关闭FileInputStream错误", e);
				}
			}

			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					log.error("关闭ByteArrayOutputStream错误", e);
				}
			}
		}

		return buffer;
	}

	/**
	 * 验证是否为手机号
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-10-17
	 * @version 1.0.0
	 */
	public static boolean checkPhoneNo(String phoneNo) {
		Pattern pattern = Pattern.compile("^1[\\d]{10}");
		Matcher matcher = pattern.matcher(phoneNo);
		return matcher.matches();
	}

	/**
	 * 手机号转默认昵称,手机前3为+'*号'+手机号后3位
	 * 
	 * @param phoneNo
	 *            手机号
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-10-17
	 * @version 1.0.0
	 */
	public static String phoneToNikename(String phoneNo) {
		return phoneNo.substring(0, 3) + "*****" + phoneNo.substring(8);
	}

	/**
	 * 根据手机号和终端类型生成TOKEN
	 * 
	 * @param phoneNo
	 *            手机号
	 * @param client
	 *            <Client> 客户端类型
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-10-17
	 * @version 1.0.0
	 */
	public static String generateToken(String phoneNo, Client client) {
		Calendar c = Calendar.getInstance();
		return MD5Coder.encrypt(phoneNo + client.toString()
				+ c.getTimeInMillis());
	}

	/**
	 * str非null && str非"" 返回true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean assertNotNull(String str) {
		return str != null && !str.isEmpty();
	}

	/**
	 * str非null && str非"" 返回false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean assertNull(String str) {
		return str == null || str.isEmpty();
	}

	/**
	 * 字节数组生成文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-11-15
	 * @version 1.0.0
	 */
	public static File writeFuleByBytes(byte[] b, String filePath) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(filePath);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			log.error("helper:get file from byte process error!");
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					log.error("helper:get file from byte process error!");
				}
			}
		}
		return ret;
	}

	public static long getTimes() {
		return Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * 判断是否上午
	 * 
	 * @return true:上午,false:下午
	 * 
	 * @author Joakim
	 * @date 2014-11-24
	 * @version 1.0.0
	 */
	public static boolean isAM() {
		// 获取当前上午还是下午,0-上午；1-下午
		// int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
		// return am_pm == 0 ? true : false;

		// 永远现实下午
		return false;
	}

	/**
	 * 配送时间转换为日期
	 * 
	 * @param sendTime
	 *            配送时间yyyyMMddHHmm
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-5
	 * @version 1.0.0
	 */
	public static String convertSendTimeToDate(String sendTime) {
		String rs = "";
		if (sendTime != null && sendTime.matches("\\d{12}")) {
			String today = Utils.getNowDate("yyyyMMdd");
			if (sendTime.startsWith(today)) {
				rs = "今天";
			} else {
				rs = sendTime.substring(0, 4) + "年" + sendTime.substring(4, 6)
						+ "月" + sendTime.substring(6, 8) + "日";
			}
		}
		return rs;
	}

	/**
	 * 配送时间转换为日期
	 * 
	 * @param sendTime
	 *            配送时间yyyyMMddHHmm
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-5
	 * @version 1.0.0
	 */
	public static String convertSendTimeToDate2(String sendTime) {
		String rs = "";
		if (sendTime != null && sendTime.matches("\\d{12}")) {
			String today = Utils.getNowDate("yyyyMMdd");
			if (sendTime.startsWith(today)) {
				rs = "今天";
			} else {
				rs = sendTime.substring(0, 4) + "/" + sendTime.substring(4, 6)
						+ "/" + sendTime.substring(6, 8);
			}
		}
		return rs;
	}

	/*
	 * 将时间格式改为yyyy.mm.dd
	 * 
	 * @author Harry date 2015-05-15
	 */
	public static String convertDate(String date) {
		StringBuffer dateStr = new StringBuffer();
		String year = date.substring(0, 4);
		String moth = date.substring(5, 7);
		String day = date.substring(8, 10);
		return dateStr.append(year).append(".").append(moth).append(".")
				.append(day).toString();
	}

	/**
	 * 
	 * @param sendTime
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-5
	 * @version 1.0.0
	 */
	public static String convertSendTimeToTime(String sendTime) {
		String rs = "";
		int add = 30;
		if (sendTime != null && sendTime.matches("\\d{12}")) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Calendar c = Calendar.getInstance();
			// 设置起始时间
			c.set(Calendar.HOUR_OF_DAY,
					Integer.parseInt(sendTime.substring(8, 10)));
			c.set(Calendar.MINUTE, Integer.parseInt(sendTime.substring(10, 12)));
			rs = sdf.format(c.getTime());
			// 增加半小时
			c.add(Calendar.MINUTE, add);
			rs += "-" + sdf.format(c.getTime());
		}
		return rs;
	}

	/**
	 * 读取输入流
	 * 
	 * @param inputStream
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-12
	 * @version 1.0.0
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static String readInportStream(InputStream inputStream)
			throws UnsupportedEncodingException, IOException {
		StringBuffer info = new StringBuffer("");
		BufferedInputStream buf = null;
		byte[] buffer = null;
		int iRead = -1;

		try {

			buf = new BufferedInputStream(inputStream);
			buffer = new byte[1024];
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead, "UTF-8"));
			}
		} finally {
			try {
				if (null != buf) {
					buf.close();
				}
			} catch (IOException e) {
				log.error("关闭流错误", e);
			}
		}

		return info.toString();
	}

	/**
	 * 转换XML为实体类
	 * 
	 * @param xmlStr
	 *            xml字符串
	 * @param clz
	 *            Class
	 * @return
	 * @throws JAXBException
	 * 
	 * @author Joakim
	 * @date 2014-12-12
	 * @version 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convtXmlToClz(String xmlStr, Class<T> clz)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		T cr = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
		return cr;
	}

	/**
	 * 根据点评份数计算等级
	 * 
	 * @param score
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-15
	 * @version 1.0.0
	 */
	public static int level(int score) {
		int level = 0;

		if (score >= 6131) {
			level = 25;
		} else if (score >= 5178) {
			level = 24;
		} else if (score >= 4337) {
			level = 23;
		} else if (score >= 3608) {
			level = 22;
		} else if (score >= 2991) {
			level = 21;
		} else if (score >= 2486) {
			level = 20;
		} else if (score >= 2037) {
			level = 19;
		} else if (score >= 1644) {
			level = 18;
		} else if (score >= 1307) {
			level = 17;
		} else if (score >= 1026) {
			level = 16;
		} else if (score >= 829) {
			level = 15;
		} else if (score >= 656) {
			level = 14;
		} else if (score >= 508) {
			level = 13;
		} else if (score >= 385) {
			level = 12;
		} else if (score >= 286) {
			level = 11;
		} else if (score >= 211) {
			level = 10;
		} else if (score >= 147) {
			level = 9;
		} else if (score >= 94) {
			level = 8;
		} else if (score >= 58) {
			level = 7;
		} else if (score >= 31) {
			level = 6;
		} else if (score >= 15) {
			level = 5;
		} else if (score >= 9) {
			level = 4;
		} else if (score >= 4) {
			level = 3;
		} else if (score >= 2) {
			level = 2;
		} else if (score == 1) {
			level = 1;
		}

		return level;
	}

	/**
	 * 验证身份证号码
	 * 
	 * @param cardId
	 *            身份证号码
	 * @return boolean
	 */
	public static JsonResult<Boolean> checkCardId(String cardId) {
		String mes = new String();
		boolean result = true;
		if (cardId.length() == 15) {
			if (!isValidDate("19" + cardId.substring(6, 8),
					cardId.substring(8, 10), cardId.substring(10, 12))) {
				mes = "false";
				result = false;
			}
		} else if (cardId.length() == 18) {
			if (!isValidDate(cardId.substring(6, 10), cardId.substring(10, 12),
					cardId.substring(12, 14))) {
				mes = "false";
				result = false;
			}
		} else {
			mes = "输入的身份证号码必须为15位或者18位";
			result = false;
		}

		if (cardId.length() == 18) {
			if (!Pattern.compile("\\d{17}").matcher(cardId.substring(0, 17))
					.find()) {
				mes = "身份证号码错误,前17位不能含有英文字母!";
				result = false;
			} else {
				int a, b;
				String cStr = null;
				int c = -1;
				a = Integer.parseInt((cardId.substring(0, 1))) * 7
						+ Integer.parseInt(cardId.substring(1, 2)) * 9
						+ Integer.parseInt(cardId.substring(2, 3)) * 10;
				a = a + Integer.parseInt(cardId.substring(3, 4)) * 5
						+ Integer.parseInt(cardId.substring(4, 5)) * 8
						+ Integer.parseInt(cardId.substring(5, 6)) * 4;
				a = a + Integer.parseInt(cardId.substring(6, 7)) * 2
						+ Integer.parseInt(cardId.substring(7, 8)) * 1
						+ Integer.parseInt(cardId.substring(8, 9)) * 6;
				a = a + Integer.parseInt(cardId.substring(9, 10)) * 3
						+ Integer.parseInt(cardId.substring(10, 11)) * 7
						+ Integer.parseInt(cardId.substring(11, 12)) * 9;
				a = a + Integer.parseInt(cardId.substring(12, 13)) * 10
						+ Integer.parseInt(cardId.substring(13, 14)) * 5
						+ Integer.parseInt(cardId.substring(14, 15)) * 8;
				a = a + Integer.parseInt(cardId.substring(15, 16)) * 4
						+ Integer.parseInt(cardId.substring(16, 17)) * 2;
				b = a % 11;
				if (b == 2) { // 最后一位为校验位
					cStr = cardId.substring(17, 18).toUpperCase(); // 转为大写X
				} else {
					if (Pattern.compile("[0-9]")
							.matcher(cardId.substring(17, 18)).find()) {
						c = Integer.parseInt(cardId.substring(17, 18));
					} else if (Pattern.compile("[a-zA-Z]")
							.matcher(cardId.substring(17, 18)).find()) {
						cStr = cardId.substring(17, 18);
					}
				}
				switch (b) {
				case 0:
					if (c != 1) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 1:
					if (c != 0) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 2:
					if (!"X".equals(cStr)) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 3:
					if (c != 9) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 4:
					if (c != 8) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 5:
					if (c != 7) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 6:
					if (c != 6) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 7:
					if (c != 5) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 8:
					if (c != 4) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 9:
					if (c != 3) {
						mes = "身份证号码校验位错";
						result = false;
					}
					break;
				case 10:
					if (c != 2) {
						mes = "身份证号码校验位错";
						result = false;
					}
				}
				if (result) {
					if (Integer.parseInt(cardId.substring(16, 17)) % 2 == 0) {
						mes = "F";
					} else {
						mes = "M";
					}
				}
			}
		} else {// 15位身份证号
			if (!Pattern.compile("\\d{15}").matcher(cardId).find()) {
				mes = "身份证号码错误,前15位不能含有英文字母！";
				result = false;
			}
			if (result) {
				if (Integer.parseInt(cardId.substring(14)) % 2 == 0) {
					mes = "F";
				} else {
					mes = "M";
				}
			}
		}
		if (result) {
			return new JsonResult<Boolean>(true, mes).convtData(result);
		} else {
			return new JsonResult<Boolean>(false, mes).convtData(result);
		}
	}

	private static boolean isValidDate(String year, String month, String day) {
		try {
			if (Integer.parseInt(year) > 2200 || Integer.parseInt(year) < 1900) {
				return false;
			}
			if (Integer.parseInt(month) > 12 || Integer.parseInt(month) <= 0) {
				return false;
			}
			if (Integer.parseInt(day) > 31 || Integer.parseInt(day) <= 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取文件名小数点之前
	 * 
	 * @param fileName
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-16
	 * @version 1.0.0
	 */
	public static String getPrifix(String fileName) {
		int len = 0;
		if ((len = fileName.indexOf(".")) >= 0) {
			return fileName.substring(0, len);
		}
		return fileName;
	}

	/**
	 * 转换图片路劲为宽度40像素的小图
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-23
	 * @version 1.0.0
	 */
	public static String to40pxImagePath(String imgPath) {
		return toSmallImagePath(imgPath, 40);
	}

	/**
	 * 转换图片路劲为宽度80像素的小图
	 * 
	 * @param imgPath
	 *            图片路径
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-23
	 * @version 1.0.0
	 */
	public static String to80pxImagePath(String imgPath) {
		return toSmallImagePath(imgPath, 40);
	}

	/**
	 * 转换图片路劲为小图
	 * 
	 * @param imgPath
	 *            图片路径
	 * @param width
	 *            宽度
	 * @return
	 * 
	 * @author Joakim
	 * @date 2014-12-23
	 * @version 1.0.0
	 */
	private static String toSmallImagePath(String imgPath, int width) {
		int len = imgPath.lastIndexOf(".");
		return imgPath.substring(0, len) + "_" + width + imgPath.substring(len);
	}

	/**
	 * 获取访问全路径
	 * 
	 * @param request
	 * @return
	 * 
	 * @author Joakim
	 * @date 2015-1-13
	 * @version 1.0.0
	 */
	public static String getUrl(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		log.debug("RequestURL:" + url);
		String para = request.getQueryString();
		log.debug("QueryString:" + para);

		if (para != null) {
			url.append("?");
			url.append(para);
		}
		log.debug("URL:" + url);
		return url.toString();
	}

	/**
	 * 如果一个double类型的值，小数部分为0，则作为一个String类型只返回它的整数部分
	 * 
	 * @param money
	 * @return
	 */
	public static String remove0(String money) {
		try {
			if (money.split("\\.").length > 1) {
				if ("0".equals(money.split("\\.")[1])
						|| "00".equals(money.split("\\.")[1])) {
					money = money.split("\\.")[0];
				}
			}
		} catch (Exception e) {
			log.debug(e.toString());
			return money;
		}

		return money;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return 天数
	 * @author yongchang.jiang
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算退款的钱数
	 * 
	 * @param startDate
	 *            出行日期
	 * @param money
	 *            订单金额
	 * @return
	 * @date 2015-4-16
	 * @throws Exception
	 * @author yongchang.jiang
	 */
	public static Double returnMoney(String startDate, Double money)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse(startDate);
		Date date2 = new Date();
		int day = daysBetween(date1, date2);
		Double returnMoney;
		if (day >= 15) {
			returnMoney = Arith.sub(money, Arith.mul(money, 0.025));
		} else if (day <= 14 && day > 7) {
			returnMoney = Arith.sub(money, Arith.mul(money, 0.5));
		} else {
			returnMoney = 0.0;
		}
		return returnMoney;
	}

	/**
	 * 计算2个时间之间相差的小时数
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 小时数
	 * @author yongchang.jiang
	 */
	public static int interValHours(String beginTime, String endTime)
			throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		long to = df.parse(beginTime).getTime();
		long from = df.parse(endTime).getTime();
		// long a = (to-from)/(1000*60*60);
		int hours = (int) ((from - to) / (1000 * 60 * 60));
		// int mm = (int)(((to-from)/1000-hours*3600)/60);
		// System.out.println("小时："+hours);
		// System.out.println("分钟："+mm);
		// System.out.println(a);
		return hours;
	}
	

}
