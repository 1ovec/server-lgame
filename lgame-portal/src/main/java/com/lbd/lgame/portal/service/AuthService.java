package com.lbd.lgame.portal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.commons.encryption.DESCoder;
import com.lbd.lgame.mapper.PubButtonMapper;
import com.lbd.lgame.mapper.UserMapper;
import com.lbd.lgame.mapper.UserRealMapper;
import com.lbd.lgame.model.PubButton;
import com.lbd.lgame.model.UserReal;

/**
 * @author zhouhua 2020-01-13 I am not responsible for this code。
 */
@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private UserRealMapper userRealMapper;

	@Autowired
	private FeignAuthService feignAuthService;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PubButtonMapper pubButtonMapper;

	/**
	 * 实名认证2要素
	 * 
	 * @param userTel
	 * @param userId
	 * @param cId
	 * @param userName
	 * @return
	 */
	@Transactional
	public JsonResult<String> verifyIcCard(String userTel, String userId, String cID, String userName) {
		log.info("-----实名认证2要素----");
		log.info("userTel:{},userName:{}", userTel, userName);
		//查询开关
		PubButton pubButton = pubButtonMapper.getPubButtonInfo("02");
		if(StringUtils.isEmpty(pubButton)) {
			if("1".equals(pubButton.getStatus())) {
				return new JsonResult<String>(false,pubButton.getRemark());
			}
		}
		// 身份证解密
		String cId = DESCoder.getInstance().dencrypt(cID);
		log.info("cId: " + cId);
		// 判断该用户是否有实名认证记录
		int userCount = userRealMapper.queryUserCount(userId);
		log.info("userCount: " + userCount);
		if (userCount == 0) {
			// 没有记录则插入
			UserReal userReal = new UserReal();
			userReal.setUserId(userId);
			userReal.setUserName(userName);
			userReal.setcId(cID);
			userReal.setRealTel("3");
			int saveUserCount = userRealMapper.saveUserReal(userReal);
			log.info("saveUserCount: " + saveUserCount);
			if (saveUserCount < 1) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-01!");
			}
			// 修改用户表实名认证状态
			int userUpdate = userMapper.updateRealStatusByUserId("3", userId);
			if (userUpdate < 1) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-02!");
			}
		}
		JsonResult<String> authJr = feignAuthService.verifyIcCard(userId, cId, userName);
		// 失败，修改认证状态
		if (!authJr.isSuccess()) {
			String errMsg = authJr.getMsg();
			log.info("userTel:{},errMsg:{}", userTel, errMsg);
			UserReal userRealUpdate = new UserReal();
			userRealUpdate.setUserId(userId);
			userRealUpdate.setErrMsg(errMsg);
			userRealUpdate.setRealStatus("4");
			// 修改实名认证表为认证失败
			int updateCount = userRealMapper.updateUserRealByUserId(userRealUpdate);
			log.info("userTel:{},updateCount:{}", userTel, updateCount);
			if (updateCount < 1) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-03!");
			}
			// 修改用户信息表实名认证状态
			int userUpdate = userMapper.updateRealStatusByUserId("4", userId);
			log.info("修改用户表认证状态为失败，userTel:{},userUpdate:{}", userTel, userUpdate);
			if (userUpdate < 1) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-04!");
			}
			return new JsonResult<String>(false, errMsg);
		} else {
			// 认证为成功时,修改用户表，实名认证表状态
			int userUpdateCount = userMapper.updateRealStatusByUserId("2", userId);
			if (userUpdateCount < 1) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-05!");
			}
			// 修改用户信息表实名认证状态
			int userUpdate = userMapper.updateRealStatusByUserId("2", userId);
			log.info("修改用户表认证状态为成功，userTel:{},userUpdate:{}", userTel, userUpdate);
			if (userUpdate < 1) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-06!");
			}
			return new JsonResult<String>(true, "实名认证提交成功!");
		}
	}

}
