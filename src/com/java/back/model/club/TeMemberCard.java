package com.java.back.model.club;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.java.back.model.BaseBean;

/**
 * 会员,会员卡关系表
 */
@Entity
@Table(name = "te_member_card")
public class TeMemberCard extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String id;
	/**
	 * 卡类id
	 */
	private long cardid;
	/**
	 * 卡类名称
	 */
	private String cardType;
	/**
	 * 会员id
	 */
	private long memberid;
	/**
	 * 剩余次数
	 */
	private int residuetimes;
	
	/**
	 * 购买时会员卡价钱
	 */
	private int price;
	/**
	 * 状态(1:未使用,2:已使用,3:已用完,4:已作废)
	 */
	private int ifuseless;
	
	/**
	 * 到期时间
	 */
	private String expiredate;
	
	/**
	 * 办理时间
	 */
	private String createtime;
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getExpiredate() {
		return expiredate;
	}
	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getCardid() {
		return cardid;
	}
	public void setCardid(long cardid) {
		this.cardid = cardid;
	}
	public long getMemberid() {
		return memberid;
	}
	public void setMemberid(long memberid) {
		this.memberid = memberid;
	}
	public int getResiduetimes() {
		return residuetimes;
	}
	public void setResiduetimes(int residuetimes) {
		this.residuetimes = residuetimes;
	}
	public int getIfuseless() {
		return ifuseless;
	}
	public void setIfuseless(int ifuseless) {
		this.ifuseless = ifuseless;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TeMemberCard() {
		super();
	}
	
}