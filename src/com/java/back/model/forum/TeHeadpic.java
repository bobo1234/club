package com.java.back.model.forum;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.java.back.model.BaseBean;

/**
 * 首页图片实体类
 * @author JYD
 *
 */
@Entity
@Table(name = "te_headpic")
public class TeHeadpic extends BaseBean {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	private long id;
	/**
	 * 标题说明
	 */
	private String title;
	/**
	 * 图片的URL
	 */
	private String picurl;
	/**
	 * 顺序
	 */
	private int porder;
	/**
	 * 是否可用
	 */
	private int ifuseless;
	/**
	 * 创建时间
	 */
	@Column(updatable = false)  
	private Date createtime;
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public int getIfuseless() {
		return ifuseless;
	}
	public void setIfuseless(int ifuseless) {
		this.ifuseless = ifuseless;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public int getPorder() {
		return porder;
	}
	public void setPorder(int porder) {
		this.porder = porder;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TeHeadpic() {
		super();
	}

}