package com.java.back.model.club;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 会员卡类型
 * 
 * @author gyb
 * 
 */
@Entity
@Table(name = "te_card")
public class TeCard implements Serializable {

	private static final long serialVersionUID = 1L;
	// @GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	/**
	 * 类型名称
	 */
	private String typename;
	/**
	 * 有效期(单位:月)
	 */
	private int validtime;
	/**
	 * 会员卡价钱
	 */
	private int price;
	/**
	 * 创建时间
	 */
	@Column(updatable = false)
	private String createtime;
	/**
	 * 类型下会员卡数量
	 */
	@Transient
	private long cardamount;
	/**
	 * 总次数
	 */
	private int usertimes;
	/**
	 * 描述
	 */
	private String description;

	public long getCardamount() {
		return cardamount;
	}

	public void setCardamount(long cardamount) {
		this.cardamount = cardamount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public int getValidtime() {
		return validtime;
	}

	public void setValidtime(int validtime) {
		this.validtime = validtime;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getUsertimes() {
		return usertimes;
	}

	public void setUsertimes(int usertimes) {
		this.usertimes = usertimes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TeCard() {
		super();
	}

}