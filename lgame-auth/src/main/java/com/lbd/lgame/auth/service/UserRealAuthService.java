package com.lbd.lgame.auth.service;

import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.google.gson.reflect.TypeToken;
import com.lbd.lgame.auth.model.resp.Credit;
import com.lbd.lgame.auth.model.resp.CreditData;
import com.lbd.lgame.auth.model.resp.CreditRespData;
import com.lbd.lgame.auth.utils.CreditUtils;
import com.lbd.lgame.common.tools.JsonUtil;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.commons.encryption.DESCoder;
import com.lbd.lgame.mapper.PubButtonMapper;
import com.lbd.lgame.mapper.UserMapper;
import com.lbd.lgame.mapper.UserRealLogMapper;
import com.lbd.lgame.mapper.UserRealMapper;
import com.lbd.lgame.model.PubButton;
import com.lbd.lgame.model.UserReal;
import com.lbd.lgame.model.UserRealLog;

@Service
public class UserRealAuthService {

	private static final Logger log = LoggerFactory.getLogger(UserRealAuthService.class);

	@Autowired
	private UserMapper userDao;
	@Autowired
	private UserRealMapper userRealDao;
	@Autowired
	private UserRealLogMapper userRealLogDao;
	@Autowired
	private PubButtonMapper buttonDao;
	@Autowired
	private CreditUtils creditUtils;

	/**
	 * 用户实名认证(四要素)
	 * 
	 * @param user
	 * @param cId
	 * @param cardNo
	 * @param realTel
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public JsonResult<String> bankVerifyFour(String userId, String cId, String cardNo, String realTel, String userName)
			throws Exception {
		boolean flag = false;
		int count = 0;
		// 校验实名认证的开关是否打开
		PubButton switchButton = buttonDao.queryButtonByDicCode("02", "实名认证开关");
		// 实名认证开关不存在
		if (StringUtils.isEmpty(switchButton)) {
			log.info("--------实名认证开关不存在--------");
			return new JsonResult<String>(false, "实名认证开关不存在，请联系管理员。");
			// 实名认证
		} else if (!"0".equals(switchButton.getStatus())) {
			log.info("--------实名认证拦截开关已打开---------");
			return new JsonResult<String>(false, switchButton.getRemark());
		}
		// 校验实名认证手机号码
		flag = Utils.checkPhoneNo(realTel);
		if (!flag) {
			log.info("实名认证[{}]不合法", realTel);
			return new JsonResult<String>(false, "您注册的手机号码不合法，请确认。");
		}
		// 加密
		String cIdEncrypt = DESCoder.getInstance().encrypt(cId);
		String cardNoEncrypt = DESCoder.getInstance().encrypt(cardNo);
		log.info("cIdEncrypt:{}", cIdEncrypt);
		log.info("cardNoEncrypt:{}", cardNoEncrypt);

		// 用户ID
		log.info("userId:{}", userId);
		// 更新app用户表实名认证状态为认证中 1-未认证,2-认证成功,3-认证中,4-认证失败
		count += userDao.updateRealStatusByUserId("3", userId);

		UserReal userReal = new UserReal();
		userReal.setCardNo(cardNoEncrypt);
		userReal.setcId(cIdEncrypt);
		userReal.setRealTel(realTel);
		userReal.setUserName(userName);
		userReal.setUserId(userId);
		// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
		userReal.setRealStatus("3");
		// 更新app认证信息表的信息
		count += userRealDao.updateUserRealByUserId(userReal);

		UserRealLog userRealLog = new UserRealLog();
		userRealLog.setCardNo(cardNoEncrypt);
		userRealLog.setcId(cIdEncrypt);
		userRealLog.setRealTel(realTel);
		// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
		userRealLog.setStatus("3");
		userRealLog.setUserId(userId);
		userRealLog.setUserName(userName);
		// 实名认证类型：1.银行卡四要素认证 2.银行卡两要素认证 3.身份证实名认证';
		userRealLog.setRealAuthType("1");
		// 添加数据到实名认证历史信息表
		count += userRealLogDao.saveUserRealLog(userRealLog);

		// 返回实名认证历史ID
		String realLogId = userRealLog.getId();
		log.info("realLogId:{}", realLogId);

		log.info("调用四要素认证接口userId:{}", userId);
		String result = creditUtils.bankVerifyFour(userName, cardNo, cId, realTel);
		if (Utils.isEmpty(result)) {
			log.info("四要素认证获取返回结果失败:{}", result);
			// 更新app_user表四要素认证状态
			count += userDao.updateRealStatusByUserId("4", userId);

			UserReal userRealErr = new UserReal();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			userRealErr.setRealStatus("4");
			userRealErr.setUserId(userId);
			userRealErr.setErrMsg("获取认证结果失败");
			// 更新用户认证信息表
			count += userRealDao.updateUserRealByUserId(userRealErr);

			UserRealLog realLogErr = new UserRealLog();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			realLogErr.setStatus("4");
			realLogErr.setErrMsg("获取认证结果失败");
			realLogErr.setId(realLogId);
			// 更新app用户认证记录表
			count += userRealLogDao.updateUserRealLogById(realLogErr);
			log.info("count:{}", count);
			return new JsonResult<String>(false, "四要素认证获取返回结果失败。");
		}

		log.info("认证结果返回:{}", result);
		Type type = new TypeToken<CreditRespData<CreditData<Credit>>>() {
		}.getType();
		CreditRespData<CreditData<Credit>> CreditRespData = JsonUtil.fromJson(result, type);
		// 网关处理成功
		if ("10000".equals(CreditRespData.getCode())) {
			log.info("...四要素认证网关处理成功...userID:{}", userId);
			// 认证成功
			if ("60000".equals(CreditRespData.getResponseData().getBizCode())) {
				log.info("...四要素认证认证成功...userID:{}", userId);
				// 更新app_user表四要素认证状态
				count += userDao.updateRealStatusByUserId("2", userId);

				UserReal userRealAuthSuc = new UserReal();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				userRealAuthSuc.setRealStatus("2");
				userRealAuthSuc.setUserId(userId);
				// 更新用户认证信息表
				count += userRealDao.updateUserRealByUserId(userRealAuthSuc);

				UserRealLog realLogAuthSuc = new UserRealLog();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				realLogAuthSuc.setStatus("2");
				realLogAuthSuc.setRealCode(CreditRespData.getResponseData().getBizCode());
				realLogAuthSuc.setId(realLogId);
				// 更新app认证信息记录表
				count += userRealLogDao.updateUserRealLogById(realLogAuthSuc);
				log.info("count:{}", count);
				// 实名认证成功
				return new JsonResult<String>(true, CreditRespData.getResponseData().getBizMsg());
			} else {
				log.info("...四要素认证认证失败...userID:{}", userId);
				// 更新app_user表四要素认证状态
				count += userDao.updateRealStatusByUserId("4", userId);

				UserReal userRealAuth = new UserReal();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				userRealAuth.setRealStatus("4");
				userRealAuth.setUserId(userId);
				userRealAuth.setErrMsg(CreditRespData.getResponseData().getBizMsg());
				// 更新用户认证信息表
				count += userRealDao.updateUserRealByUserId(userRealAuth);

				UserRealLog realLogAuth = new UserRealLog();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				realLogAuth.setStatus("4");
				realLogAuth.setRealCode(CreditRespData.getResponseData().getBizCode());
				realLogAuth.setErrMsg(CreditRespData.getResponseData().getBizMsg());
				realLogAuth.setId(realLogId);
				// 更新app用户认证记录表
				count += userRealLogDao.updateUserRealLogById(realLogAuth);
				log.info("count:{}", count);
				// 返回实名认证失败信息
				return new JsonResult<String>(false, CreditRespData.getResponseData().getBizMsg());
			}
		} else {
			log.info("...四要素认证网关处理失败...userID:{}", userId);
			// 更新app_user表四要素认证状态
			count += userDao.updateRealStatusByUserId("4", userId);

			UserReal userRealErr = new UserReal();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			userRealErr.setRealStatus("4");
			userRealErr.setUserId(userId);
			userRealErr.setErrMsg(CreditRespData.getMsg());
			// 更新用户认证信息表
			count += userRealDao.updateUserRealByUserId(userRealErr);

			UserRealLog realLogErr = new UserRealLog();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			realLogErr.setStatus("4");
			realLogErr.setRealCode(CreditRespData.getCode());
			realLogErr.setErrMsg(CreditRespData.getMsg());
			realLogErr.setId(realLogId);
			// 更新app用户认证记录表
			count += userRealLogDao.updateUserRealLogById(realLogErr);
			log.info("count:{}", count);
			// 返回实名认证失败信息
			return new JsonResult<String>(false, CreditRespData.getMsg());
		}
	}

	/**
	 * 用户身份实名认证
	 * 
	 * @param userId
	 * @param cId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public JsonResult<String> verifyIcCard(String userId, String cId, String userName) throws Exception {
		int count = 0;
		// 加密
		String cIdEncrypt = DESCoder.getInstance().encrypt(cId);
		log.info("cIdEncrypt:{}", cIdEncrypt);
		// 用户ID
		log.info("userId:{}", userId);

		UserRealLog userRealLog = new UserRealLog();
		userRealLog.setcId(cIdEncrypt);
		// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
		userRealLog.setStatus("3");
		userRealLog.setUserId(userId);
		userRealLog.setUserName(userName);
		// 实名认证类型：1.银行卡四要素认证 2.银行卡两要素认证 3.身份证实名认证';
		userRealLog.setRealAuthType("3");
		// 添加数据到实名认证历史信息表
		count += userRealLogDao.saveUserRealLog(userRealLog);

		// 返回实名认证历史ID
		String realLogId = userRealLog.getId();
		log.info("realLogId:{}", realLogId);

		log.info("调用实名认证校验接口userId:{}", userId);
		String result = creditUtils.checkSid(userName, cId);
		if (Utils.isEmpty(result)) {
			log.info("实名认证校验获取返回结果失败:{}", result);

			UserRealLog realLogErr = new UserRealLog();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			realLogErr.setStatus("4");
			realLogErr.setErrMsg("获取认证结果失败");
			realLogErr.setId(realLogId);
			// 更新app用户认证记录表
			count += userRealLogDao.updateUserRealLogById(realLogErr);
			log.info("count:{}", count);
			return new JsonResult<String>(false, "系统繁忙，请稍后再试-09");
		}

		log.info("认证结果返回:{}", result);
		Type type = new TypeToken<CreditRespData<CreditData<Credit>>>() {
		}.getType();
		CreditRespData<CreditData<Credit>> CreditRespData = JsonUtil.fromJson(result, type);
		// 网关处理成功
		if ("10000".equals(CreditRespData.getCode())) {
			log.info("...实名认证校验网关处理成功...userID:{}", userId);
			// 认证成功
			if ("60000".equals(CreditRespData.getResponseData().getBizCode())) {
				log.info("...实名认证成功...userID:{}", userId);

				UserRealLog realLogAuthSuc = new UserRealLog();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				realLogAuthSuc.setStatus("2");
				realLogAuthSuc.setRealCode(CreditRespData.getResponseData().getBizCode());
				realLogAuthSuc.setId(realLogId);
				// 更新app认证信息记录表
				count += userRealLogDao.updateUserRealLogById(realLogAuthSuc);
				log.info("count:{}", count);
				// 实名认证成功
				return new JsonResult<String>(true, CreditRespData.getResponseData().getBizMsg());
			} else {
				log.info("...实名认证校验失败...userID:{}", userId);
				UserRealLog realLogAuth = new UserRealLog();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				realLogAuth.setStatus("4");
				realLogAuth.setRealCode(CreditRespData.getResponseData().getBizCode());
				realLogAuth.setErrMsg(CreditRespData.getResponseData().getBizMsg());
				realLogAuth.setId(realLogId);
				// 更新app用户认证记录表
				count += userRealLogDao.updateUserRealLogById(realLogAuth);
				log.info("count:{}", count);
				// 返回实名认证失败信息
				return new JsonResult<String>(false, CreditRespData.getResponseData().getBizMsg());
			}
		} else {
			log.info("...实名认证校验网关处理失败...userID:{}", userId);
			UserRealLog realLogErr = new UserRealLog();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			realLogErr.setStatus("4");
			realLogErr.setRealCode(CreditRespData.getCode());
			realLogErr.setErrMsg(CreditRespData.getMsg());
			realLogErr.setId(realLogId);
			// 更新app用户认证记录表
			count += userRealLogDao.updateUserRealLogById(realLogErr);
			log.info("count:{}", count);
			// 返回实名认证失败信息
			return new JsonResult<String>(false, CreditRespData.getMsg());
		}
	}

	/**
	 * 
	 * 银行卡两要素认证
	 * 
	 * @param userId
	 * @param cardNo
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public JsonResult<String> verifyBankCard(String userId, String cardNo, String userName) throws Exception {
		int count = 0;
		// 加密
		String cardNoEncrypt = DESCoder.getInstance().encrypt(cardNo);
		log.info("cardNoEncrypt:{}", cardNoEncrypt);
		// 用户ID
		log.info("userId:{}", userId);

		UserRealLog userRealLog = new UserRealLog();
		userRealLog.setCardNo(cardNoEncrypt);
		// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
		userRealLog.setStatus("3");
		userRealLog.setUserId(userId);
		userRealLog.setUserName(userName);
		// 实名认证类型：1.银行卡四要素认证 2.银行卡两要素认证 3.身份证实名认证';
		userRealLog.setRealAuthType("2");
		// 添加数据到实名认证历史信息表
		count += userRealLogDao.saveUserRealLog(userRealLog);

		// 返回实名认证历史ID
		String realLogId = userRealLog.getId();
		log.info("realLogId:{}", realLogId);

		log.info("调用银行卡两要素认证接口userId:{}", userId);
		String result = creditUtils.checkBankCard(userName, cardNo);
		if (Utils.isEmpty(result)) {
			log.info("银行卡两要素认证获取返回结果失败:{}", result);
			UserRealLog realLogErr = new UserRealLog();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			realLogErr.setStatus("4");
			realLogErr.setErrMsg("获取认证结果失败");
			realLogErr.setId(realLogId);
			// 更新app用户认证记录表
			count += userRealLogDao.updateUserRealLogById(realLogErr);
			log.info("count:{}", count);
			return new JsonResult<String>(false, "银行卡两要素认证获取返回结果失败。");
		}

		log.info("银行卡两要素认证结果返回:{}", result);
		Type type = new TypeToken<CreditRespData<CreditData<Credit>>>() {
		}.getType();
		CreditRespData<CreditData<Credit>> CreditRespData = JsonUtil.fromJson(result, type);
		// 网关处理成功
		if ("10000".equals(CreditRespData.getCode())) {
			log.info("...银行卡两要素认证网关处理成功...userID:{}", userId);
			// 认证成功
			if ("60000".equals(CreditRespData.getResponseData().getBizCode())) {
				log.info("...银行卡两要素认证成功...userID:{}", userId);

				UserRealLog realLogAuthSuc = new UserRealLog();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				realLogAuthSuc.setStatus("2");
				realLogAuthSuc.setRealCode(CreditRespData.getResponseData().getBizCode());
				realLogAuthSuc.setId(realLogId);
				// 更新app认证信息记录表
				count += userRealLogDao.updateUserRealLogById(realLogAuthSuc);
				log.info("count:{}", count);
				// 实名认证成功
				return new JsonResult<String>(true, CreditRespData.getResponseData().getBizMsg());
			} else {
				log.info("...银行卡两要素认证失败...userID:{}", userId);
				UserRealLog realLogAuth = new UserRealLog();
				// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
				realLogAuth.setStatus("4");
				realLogAuth.setRealCode(CreditRespData.getResponseData().getBizCode());
				realLogAuth.setErrMsg(CreditRespData.getResponseData().getBizMsg());
				realLogAuth.setId(realLogId);
				// 更新app用户认证记录表
				count += userRealLogDao.updateUserRealLogById(realLogAuth);
				log.info("count:{}", count);
				// 返回实名认证失败信息
				return new JsonResult<String>(false, CreditRespData.getResponseData().getBizMsg());
			}
		} else {
			log.info("...银行卡两要素认证网关处理失败...userID:{}", userId);
			UserRealLog realLogErr = new UserRealLog();
			// 认证状态 1-未认证,2-认证成功,3-认证中,4-认证失败
			realLogErr.setStatus("4");
			realLogErr.setRealCode(CreditRespData.getCode());
			realLogErr.setErrMsg(CreditRespData.getMsg());
			realLogErr.setId(realLogId);
			// 更新app用户认证记录表
			count += userRealLogDao.updateUserRealLogById(realLogErr);
			log.info("count:{}", count);
			// 返回实名认证失败信息
			return new JsonResult<String>(false, CreditRespData.getMsg());
		}
	}
}
