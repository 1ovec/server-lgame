package com.lbd.lgame.auth.task;

import com.lbd.lgame.auth.service.IdCardCheckService;
import com.lbd.lgame.auth.service.OcrService;
import com.lbd.lgame.common.tools.JsonUtil;
import com.lbd.lgame.common.utils.ImageUtils;
import com.lbd.lgame.commons.encryption.DESCoder;
import com.lbd.lgame.mapper.UserMapper;
import com.lbd.lgame.mapper.UserRealMapper;
import com.lbd.lgame.model.UserReal;
import com.lbd.lgame.model.UserRealLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lichuang 2020-02-04
 * 锻炼自己的线程能力，在项目中实际使用线程池的地方
 */
@Service
public class AutoVerifyTask {

    private static final Logger log = LoggerFactory.getLogger(AutoVerifyTask.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");

    @Autowired
    private OcrService ocrService;

    @Autowired
    private IdCardCheckService cidService;
    
    @Autowired
    private UserRealMapper userRealMapper;
    
    @Autowired
    private UserMapper userMapper;


    /**
     * 使用定时器，给未认证的user进行认证操作
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void AutoVerifyCard() {
        // 1.获取未认证的user信息,并将数据修改成认证中的状态
        List<UserRealLog> unverifiedUsers = ocrService.getUnverifiedUsers();
        int allUserSize = unverifiedUsers.size();
        log.info("查询到的身份证未认证的用户数：{}", allUserSize);
        // 2.将获取到的数据，修改为认证中...
        // 2.1获取到数据的时候进行操作，没数据的时候结束任务
        if (unverifiedUsers != null && allUserSize > 0) {
            //将所有的数据平均分成5等份
            List<List<UserRealLog>> averageUserList = averageAssign(unverifiedUsers, 5);
            // 创建线程池，为每一个user单独开一个线程进行认证操作
            ExecutorService executor = new ThreadPoolExecutor(5, 5, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(5));
            // 遍历每一个切割好的集合
            for (List<UserRealLog> userRealLogs : averageUserList) {
                //将每一个平均切割好的集合，放入到一个线程中去进行处理
                executor.execute(() -> {

                    log.info("当前线程正在处理的用户数：averageUserList.size():{}", userRealLogs.size());
                    //对集合中的每一个用户进行处理
                    for (UserRealLog unverifiedUser : userRealLogs) {
                        log.info("当前线程正在处理的用户：averageUserList.getId():{},userID:{}", unverifiedUser.getId(),unverifiedUser.getUserId());
                        try {
                            if (unverifiedUser.getFronPath() != null && unverifiedUser.getReversePath() != null) {
                                MultipartFile[] files = getMultipartFile(unverifiedUser.getFronPath(), unverifiedUser.getReversePath(), unverifiedUser.getId());
                                // ocrService.checkCidImage(files, "0");
                                // 调用图片识别的接口，识别身份证正面图片
                              //  String fronResult = ocrService.ocrCidApi(files[0], "0");
                               String fronResult = "{\"sys_req_sn\":\"6B6CFAD8AC9B5D73154C822045AF4F92\",\"operation_time\":\"2020-01-16 20:16:45\",\"data\":{\"name\":\"徐俊全\",\"certid\":\"362502199306085436\"},\"code\":0,\"info\":\"解析成功\"}";
                               log.info("fronResult: "+fronResult);
                               Map<String, Object> fronMap = JsonUtil.fromJson(fronResult, Map.class);
                                log.info("图像识别完成:处理完图像的id：{}", unverifiedUser.getId() + "处理结果，正面图片：" + fronMap);
                                //3.将识别的结果进行对比 4.将对比后的结果回填到db，将状态修改成对应的状态
                                resultProcessing(fronMap, files[1], unverifiedUser);
                            } else {
                                unverifiedUser.setStatus("4");
                                unverifiedUser.setErrMsg("未查询到身份证照片");
                                ocrService.updateUserRealLogById(unverifiedUser);
                                String userID= unverifiedUser.getUserId();
                                UserReal userReal = new UserReal();
                                userReal.setUserId(userID);
                                userReal.setRealStatus("4");
                                userReal.setErrMsg("未查询到身份证照片");
                                userRealMapper.updateUserRealByUserId(userReal);
                                userMapper.updateRealStatusByUserId("4",userID);
                                log.info("当前用户id：{}:未输入身份证照片", unverifiedUser.getId());
                            }
                        } catch (Exception e) {
                            log.error("当前发生异常的用户id：{}", unverifiedUser.getId());
                            log.error("e:", e);
                        }
                    }

                });
            }
            executor.shutdown();
        }
    }

    /**
     * 处理正面图片识别后的事情
     *
     * @param fronMap        正面图片识别后的结果
     * @param unverifiedUser 当前用户信息
     */
    private void resultProcessing(Map<String, Object> fronMap, MultipartFile file, UserRealLog unverifiedUser) {
        //正面结果处理
        UserReal userReal = new UserReal();
        userReal.setUserId(unverifiedUser.getUserId());
        userReal.setRealStatus("4");
        userReal.setRealTime(sdf.format(new Date()));
        unverifiedUser.setRealTime(sdf.format(new Date()));
        //1.1正面识别失败：
        if (Double.valueOf(fronMap.get("code").toString()).intValue() < 0) {
            unverifiedUser.setStatus("4");
            unverifiedUser.setErrMsg("身份证正面图片有误，识别失败");//fronMap.get("info").toString()
            ocrService.updateUserRealLogById(unverifiedUser);
            userReal.setErrMsg("身份证正面图片有误，识别失败");//fronMap.get("info").toString()
            ocrService.updateUserRealById(userReal);
            userMapper.updateRealStatusByUserId("4",unverifiedUser.getUserId());
            log.info("图像识别完成:当前用户的 id：{}，正面图片识别失败，此用户认证的实名操作结束", unverifiedUser.getId());
            return;
        } else {
            //1.2正面识别成功
            String decodeCardNo = DESCoder.getInstance().dencrypt(unverifiedUser.getcId());//获取当前用户填写的身份证信息
            //身份证号码匹配：
            Map<String, Object> fronData = (Map<String, Object>) fronMap.get("data");
            //检验获取到的数据是否正确：
            boolean checkCardInfoFlag = checkCardInfo(fronData.get("name").toString(), fronData.get("certid").toString());
            if (!checkCardInfoFlag){
                unverifiedUser.setStatus("4");
                unverifiedUser.setErrMsg("身份证号码和姓名不匹配，认证失败");//fronMap.get("info").toString()
                ocrService.updateUserRealLogById(unverifiedUser);
                userReal.setErrMsg("身份证号码和姓名不匹配，认证失败");//fronMap.get("info").toString()
                ocrService.updateUserRealById(userReal);
                userMapper.updateRealStatusByUserId("4",unverifiedUser.getUserId());
                log.info("图像识别完成:当前用户的 id：{}，图片上的身份证号码与姓名不匹配，认证失败", unverifiedUser.getId());
                return;
            }
            //获取身份证和性别对比的结果
            boolean cardNoFlag = fronData.get("certid").toString().equals(decodeCardNo);
            boolean cardNameFlag = fronData.get("name").toString().equals(unverifiedUser.getUserName());//unverifiedUser.getUserName()
            if (cardNoFlag && cardNameFlag) {
                //2.1 身份证号码匹配：
                unverifiedUser.setStatus("2");
                unverifiedUser.setErrMsg("");
                /**
                 * 处理身份证背面识别情况
                 * 正面身份证识别结果 与 数据库数据 比对成功之后，进行身份证背面图片识别
                 */
                //String reverseResult = ocrService.ocrCidApi(file, "1");
                String reverseResult = "{\"sys_req_sn\":\"6B6CFAD8AC9B5D73154C822045AF4F92\",\"operation_time\":\"2020-01-16 20:16:45\",\"code\":0,\"info\":\"解析成功\"}";
                log.info("reverseResult: "+reverseResult);
                //将识别结果转换成Map
                Map<String, Object> reverseMap = JsonUtil.fromJson(reverseResult, Map.class);
                if (Double.valueOf(reverseMap.get("code").toString()).intValue() < 0) {
                    unverifiedUser.setStatus("4");
                    unverifiedUser.setErrMsg(reverseMap.get("info").toString());
                    ocrService.updateUserRealLogById(unverifiedUser);
                    userReal.setErrMsg(reverseMap.get("info").toString());
                    ocrService.updateUserRealById(userReal);
                    log.info("当前用户的 id：{}，背面图片识别失败，此用户的实名认证操作结束", unverifiedUser.getId());
                    return;
                }
                ocrService.updateUserRealLogById(unverifiedUser);
                userReal.setRealStatus("2");
                userReal.setErrMsg("");
                ocrService.updateUserRealById(userReal);
                userMapper.updateRealStatusByUserId("2",unverifiedUser.getUserId());
                log.info("当前用户的 id: {}: 图像识别完成，此用户成功完成实名认证的操作", unverifiedUser.getId());
            } else {
                //2.2 身份证号码不匹配
                unverifiedUser.setStatus("4");
                if (cardNameFlag == cardNoFlag) {
                    unverifiedUser.setErrMsg("身份证号码和姓名都不匹配");
                    userReal.setErrMsg("身份证号码和姓名都不匹配");
                } else if (!cardNoFlag) {
                    unverifiedUser.setErrMsg("身份证号码不匹配");
                    userReal.setErrMsg("身份证号码不匹配");
                } else {
                    unverifiedUser.setErrMsg("身份证姓名不匹配");
                    userReal.setErrMsg("身份证姓名不匹配");
                }
                ocrService.updateUserRealLogById(unverifiedUser);
                ocrService.updateUserRealById(userReal);
                userMapper.updateRealStatusByUserId("4",unverifiedUser.getUserId());
                log.info("当前用户的 id: {}: 图像识别完成，但数据不匹配，此用户认证的实名操作结束", unverifiedUser.getId());

            }
        }
    }


    /**
     * 通过url地址获取图片，并将图片转换成MultipartFile格式的图片
     * @param fronUrl 正面图片的url地址
     * @param reverseUrl 背面图片的rul地址
     * @param fileName 生成的临时用的文件名
     * @return MultipartFile返回新格式的正反面图片数组
     * @throws Exception 异常
     */
    private MultipartFile[] getMultipartFile(String fronUrl, String reverseUrl, String fileName) throws Exception {
        FileInputStream input = null;
        FileInputStream input2 = null;
        MultipartFile[] list = new MultipartFile[2];
        File tempPic = null;
        File tempPic2 = null;
        //获取图片，将图片转换成
        try {
            //获取正面图片
            tempPic = ImageUtils.ImageRequest(fileName + "1", fronUrl);
            input = new FileInputStream(tempPic);
            MultipartFile fronFile = new MockMultipartFile("file", tempPic.getName(), "text/plain", input);
            list[0] = fronFile;
            //获取背面图片
            tempPic2 = ImageUtils.ImageRequest(fileName + "2", reverseUrl);
            input2 = new FileInputStream(tempPic);
            MultipartFile reverseFile = new MockMultipartFile("file", tempPic2.getName(), "text/plain", input2);
            list[1] = reverseFile;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("e:", e);
            throw new Exception();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (input2 != null) {
                    input2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempPic.delete();
            tempPic2.delete();
        }
    }


    /**
     * @param list 将lists等分成几个list
     * @return list list
     */
    private static <T> List<List<T>> averageAssign(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        int remaider = list.size() % n;  //(先计算出余数)
        int number = list.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 判断识别到的身份证号和姓名是否对应
     * @param cardName 身份证姓名
     * @param cardNo 身份证号
     * @return
     */
    private boolean checkCardInfo(String cardName,String cardNo){
        String result = cidService.IdCardCertificationApi(cardName, cardNo);
        //将识别结果转换成Map
        Map<String, Object> reverseMap = JsonUtil.fromJson(result, Map.class);
        //如果状态码为负数则表明身份证号码和姓名不匹配
        if (Double.valueOf(reverseMap.get("code").toString()).intValue() < 0){
            return false;
        }else{
            return true;
        }
    }




}
