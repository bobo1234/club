package com.java.back.model.club;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.java.back.model.BaseBean;

/**
 * 俱乐部会员表
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "te_member")
public class TeMember extends BaseBean {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	/**
	 * 微信账号
	 */
	private String wxaccount;
	/**
	 * 真实姓名
	 */
	private String realname;
	/**
	 * 姓名的首字母
	 */
	private String namecode;
	/**
	 * 状态(默认为0,)
	 */
	private Integer status;
	/**
	 * 总消费金额
	 */
	private Integer consumeMoney;
	/**
	 * 年龄
	 */
	@Transient
	private int age;
	/**
	 * 创建时间
	 */
	@Column(updatable=false)
	private String createtime;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 出生年月
	 */
	private String birthday;
	/**
	 * 性别(女:1,男:2)
	 */
	private int sex;
	/**
	 * 头像
	 */
	private String headimgurl;
	/**
	 * 握拍方式(直板:1,横板:2)
	 */
	private int griptype;

	/**
	 * 会员等级
	 */
	@Transient
	private String rank;
	/**
	 * 备注
	 */
	private String remark;
	

	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getConsumeMoney() {
		return consumeMoney;
	}


	public void setConsumeMoney(Integer consumeMoney) {
		this.consumeMoney = consumeMoney;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNamecode() {
		return namecode;
	}

	public void setNamecode(String namecode) {
		this.namecode = namecode;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getWxaccount() {
		return wxaccount;
	}

	public void setWxaccount(String wxaccount) {
		this.wxaccount = wxaccount;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCreatetime() {
		return createtime;
	}
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getGriptype() {
		return griptype;
	}

	public void setGriptype(int griptype) {
		this.griptype = griptype;
	}

	public TeMember() {
		super();
	}

	public TeMember(String realname, String phone, String birthday, int sex,
			int griptype, String remark) {
		super();
		this.realname = realname;
		this.phone = phone;
		this.birthday = birthday;
		this.sex = sex;
		this.griptype = griptype;
		this.remark = remark;
	}
}