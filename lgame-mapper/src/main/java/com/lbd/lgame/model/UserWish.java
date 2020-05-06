package com.lbd.lgame.model;

/**
 * @ClassName UserWish
 * @Description 个人心愿表实体类
 * @Author YUAN
 * @Date 2020/1/10 14:14
 **/
public class UserWish {
    //ID
    private String id;
    //用户ID
    private String userId;
    //心愿标题
    private String wishName;
    //心愿内容
    private String wishText;
    //图片路径
    private String wishPath;
    //价格
    private String price;
    //创建时间
    private String createTime;
    //修改时间
    private String editTime;
    //计划完成时间
    private String finishTime;
    //计划终止时间
    private String endTime;
    //点赞次数
    private String vote;
    //0-取消,1-初始化,2-进行中,3-暂停,4-进行中,5-已完成
    private String status;
    //编号
    private String wishNo;
    //用户手机号
    private String userTel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWishName() {
        return wishName;
    }

    public void setWishName(String wishName) {
        this.wishName = wishName;
    }

    public String getWishText() {
        return wishText;
    }

    public void setWishText(String wishText) {
        this.wishText = wishText;
    }

    public String getWishPath() {
        return wishPath;
    }

    public void setWishPath(String wishPath) {
        this.wishPath = wishPath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWishNo() {
        return wishNo;
    }

    public void setWishNo(String wishNo) {
        this.wishNo = wishNo;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }
}
