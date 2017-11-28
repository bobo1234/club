package com.java.back.model.club;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.java.back.model.BaseBean;

/**
 * 参赛记录
 * 
 */
@Entity
@Table(name = "te_member_match_log")
public class TeMemberMatchLog extends BaseBean {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String id;

	@OneToOne
	@JoinColumn(name = "match_id")
	private TeMatch match;
	
	@OneToOne
	@JoinColumn(name = "member_id")
	private TeMember members;

	/**
	 * 创建时间
	 */
	private String createtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TeMatch getMatch() {
		return match;
	}

	public void setMatch(TeMatch match) {
		this.match = match;
	}

	public TeMember getMembers() {
		return members;
	}

	public void setMembers(TeMember members) {
		this.members = members;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public TeMemberMatchLog() {
		super();
	}
}