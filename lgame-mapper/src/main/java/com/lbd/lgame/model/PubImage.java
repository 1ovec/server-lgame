package com.lbd.lgame.model;

/**
 * @author Administrator
 *
 */
public class PubImage {
	/**
	 * 图片
	 *
	 */
	//ID  
	private String id;
	//图片类型,见字典表T0007
	private String imageType;
	//图片路径
	private String imagePath;
	//0-无效,1-有效
	private String status;
	//创建时间
	private String createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}	
}
