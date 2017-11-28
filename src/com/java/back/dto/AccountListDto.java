package com.java.back.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 账户列表实体传输类
 *   
 * @author Kiro
 */
public class AccountListDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String nickname;
	private Date date;
	private String creator;
	private String time;
	private boolean initAccount;
	private Integer acctState;
	private String mark;
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public AccountListDto() {
		// TODO Auto-generated constructor stub
	}

	public int getAcctState() {
		return acctState;
	}

	public void setAcctState(int acctState) {
		this.acctState = acctState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AccountListDto(long acctId, String acctName, String acctNickname, String time, String creator,
			boolean acctSuper,Integer acctState,String mark) {
		super();
		this.id = acctId;
		this.name = acctName;
		this.nickname = acctNickname;
		this.time = time;
		this.creator = creator;
		this.initAccount = acctSuper;
		this.acctState = acctState;
		this.mark = mark;
	}

	@Override
	public String toString() {
		return "AccountListDto [id:" + id + ", name:" + name + ", nickname:" + nickname + ", date:" + date
				+ ", creator:" + creator + ", time:" + time + ", initAccount:" + initAccount + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setAcctState(Integer acctState) {
		this.acctState = acctState;
	}

	public boolean isInitAccount() {
		return initAccount;
	}

	public void setInitAccount(boolean initAccount) {
		this.initAccount = initAccount;
	}

}
