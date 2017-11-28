package com.java.back.model.club;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.java.back.model.BaseBean;

/**
 * 比赛
 * 
 */
@Entity
@Table(name = "te_match")
public class TeMatch extends BaseBean {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 说明
	 */
	private String mark;
	/**
	 * 创建时间
	 */
	@Column(updatable=false)
	private String createtime;
	
	/**
	 * 参赛人数
	 */
	private long inmembers;
	/**
	 * 比赛状态
	 */
	@Transient
	private String mstate;
	
	/**
	 * 比赛时间
	 */
	private String matchtime;
	/**
	 * 状态(1:未开始,2:已开始,3:已结束,4:已取消)
	 */
	private int state;
	
	public String getMstate() {
		return mstate;
	}
	public void setMstate(String mstate) {
		this.mstate = mstate;
	}
	
	public long getInmembers() {
		return inmembers;
	}
	public void setInmembers(long inmembers) {
		this.inmembers = inmembers;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getMatchtime() {
		return matchtime;
	}
	public void setMatchtime(String matchtime) {
		this.matchtime = matchtime;
	}
	public TeMatch() {
		super();
	}

}