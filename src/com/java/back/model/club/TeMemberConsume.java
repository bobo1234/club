package com.java.back.model.club;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.java.back.model.BaseBean;

/**
 * 会员,消费会员卡历史表
 */
@Entity
@Table(name = "te_member_consume")
public class TeMemberConsume extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String id;
	/**
	 * 办理的会员卡id
	 */
	private String membercardid;
	/**
	 * 会员id
	 */
	private long memberid;
	/**
	 * 消费次数
	 */
	private int ctimes;
	/**
	 * 消费时间
	 */
	private String createtime;
	
	public int getCtimes() {
		return ctimes;
	}
	public void setCtimes(int ctimes) {
		this.ctimes = ctimes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMembercardid() {
		return membercardid;
	}
	public void setMembercardid(String membercardid) {
		this.membercardid = membercardid;
	}
	public long getMemberid() {
		return memberid;
	}
	public void setMemberid(long memberid) {
		this.memberid = memberid;
	}
	
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public TeMemberConsume() {
		super();
	}
	
}