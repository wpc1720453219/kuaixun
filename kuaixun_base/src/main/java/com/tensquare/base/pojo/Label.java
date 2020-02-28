package com.tensquare.base.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 实体类
 * @author Administrator
 *
 */
@Entity
@Table(name="tb_label")
public class Label implements Serializable{

	@Id
	private String id;//标签ID


	
	private String labelname;//标签名称
	private String state;//状态
	private Long count;//使用数量
	private String recommend;//是否推荐
	private Long fans;//粉丝数
	private String pid;//标签PID

	
	public String getId() {		
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getLabelname() {		
		return labelname;
	}
	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}

	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public Long getCount() {		
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}

	public String getRecommend() {		
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public Long getFans() {		
		return fans;
	}
	public void setFans(Long fans) {
		this.fans = fans;
	}

	public String getPid() {		
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}


	
}
