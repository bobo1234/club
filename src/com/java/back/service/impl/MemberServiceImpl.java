package com.java.back.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.ClubConst;
import com.java.back.constant.PageConstant;
import com.java.back.dao.MemberCardDao;
import com.java.back.dao.MemberConsumeDao;
import com.java.back.dao.support.AbstractDao;
import com.java.back.model.club.TeCard;
import com.java.back.model.club.TeMember;
import com.java.back.model.club.TeMemberCard;
import com.java.back.service.MemberService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.CompareUtil;
import com.java.back.utils.DateUtil;
import com.java.back.utils.GetPinyin;
import com.java.back.utils.PageUtils;
import com.java.back.utils.StringUtil;

@Scope
@Service
@Transactional(readOnly = true)
public class MemberServiceImpl extends AbstractDao<TeMember> implements
		MemberService {

	@Autowired
	private MemberCardDao memberCardDao;

	@Autowired
	private MemberConsumeDao memberConsumeDao;

	@Override
	public JSONReturn findList(int page, String serVal, int sex) {
		// TODO Auto-generated method stub
		String sql = "SELECT a.* from ( select * from te_member where 1=1 ";
		if (StringUtil.isNotEmpty(serVal)) {
			sql += " and realname" + StringUtil.formatLike(serVal);
			sql += " UNION SELECT  * from te_member where phone"
					+ StringUtil.formatEndLike(serVal);
			sql += " UNION SELECT  * from te_member where namecode"
					+ StringUtil.formatEqual(serVal);
		}
		sql += sex > 0 ? " and sex=" + sex : "";
		sql += " ) a  ORDER BY a.createtime desc  ";
		Query query = this.findSession().createSQLQuery(sql)
				.addEntity(getEntityClass());
		query.setFirstResult((page - 1) * PageConstant.PAGE_LIST);
		query.setMaxResults(PageConstant.PAGE_LIST);
		List<TeMember> list = query.list();
		for (TeMember teMember : list) {
			teMember.setRank(ClubConst.getRank(teMember.getConsumeMoney()));
			teMember.setAge(DateUtil.getAgefromBir(teMember.getBirthday()
					.split("-")[0]));// 计算年龄与消费等级
		}
		return JSONReturn.buildSuccess(list);
	}

	@Override
	public JSONReturn findCount(int page, String serVal, int sex) {
		// TODO Auto-generated method stub
		String sql = "SELECT count(*) from ( select 1 from te_member where 1=1 ";
		if (StringUtil.isNotEmpty(serVal)) {
			sql += " and realname" + StringUtil.formatLike(serVal);
			sql += " UNION SELECT 1 from te_member where phone"
					+ StringUtil.formatEndLike(serVal);
			sql += " UNION SELECT  1 from te_member where namecode"
					+ StringUtil.formatEqual(serVal);
		}
		sql += sex > 0 ? " and sex=" + sex : "";
		sql += " ) a";
		int count = countAll(sql);
		return JSONReturn.buildSuccess(PageUtils.calculatePage(page, count,
				PageConstant.PAGE_LIST));
	}

	@Override
	public JSONReturn findMById(long id) {
		// TODO Auto-generated method stub
		TeMember teMember = this.get(id);
		if (CompareUtil.isEmpty(teMember))
			return JSONReturn.buildFailure("获取源数据失败!");
		return JSONReturn.buildSuccess(teMember);
	}

	@Transactional
	@Override
	public JSONReturn modifyMember(TeMember member) {
		// TODO Auto-generated method stub
		try {
			boolean verifiyPhone = verifiyPhone(member.getPhone(),
					member.getId());
			if (!verifiyPhone) {
				return JSONReturn.buildFailure("该手机号已存在");
			}
			member.setNamecode(GetPinyin.getPinYinHeadChar(member.getRealname()));// 设置名称的首字母
			this.update(member);
			return JSONReturn.buildSuccess("修改成功");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return JSONReturn.buildFailure("修改失败");
	}

	@Transactional
	@Override
	public JSONReturn addMember(TeMember member, long cardtypeid, int ctimes) {
		// TODO Auto-generated method stub
		try {
			boolean verifiyPhone = verifiyPhone(member.getPhone(), 0);
			if (!verifiyPhone) {
				return JSONReturn.buildFailure("该手机号已存在");
			}
			TeCard card = memberCardDao.getModel(cardtypeid, TeCard.class);
			if (CompareUtil.isNotEmpty(card)) {
				if (card.getUsertimes() == 0) {
					if (ctimes > 1) {
						return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
								JSONReturn.TYPE_WARNING,
								"操作失败,月卡只能本人消费,每次只能消费一次!");
					}
				} else {
					if ((card.getUsertimes() - ctimes) < 0) {
						return JSONReturn.buildOther(JSONReturn.HEAD_FAILURE,
								JSONReturn.TYPE_WARNING, "操作失败,次数不足!");
					}
				}
			}
			member.setConsumeMoney(0);
			member.setStatus(0);
			member.setCreatetime(DateUtil.getAllDate());
			member.setNamecode(GetPinyin.getPinYinHeadChar(member.getRealname()));// 设置名称的首字母
			if (CompareUtil.isNotEmpty(card)) {// 办理会员卡
				member.setConsumeMoney(member.getConsumeMoney() == null ? 0
						: member.getConsumeMoney() + card.getPrice());// 消费金额
			}
			this.save(member);
			if (CompareUtil.isNotEmpty(card)) {// 办理会员卡
				TeMemberCard memberCard = memberCardDao.addMemberCard(
						member.getId(), card, ctimes);
				if (ctimes > 0) {// 消费了
					if (!memberConsumeDao.consumeCard(memberCard, ctimes)) {// 消费ctimes次
						return JSONReturn.buildFailure("消费失败");
					}
				}
			}
			return JSONReturn.buildSuccess("办理成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return JSONReturn.buildFailure();
	}

	/**
	 * 验证手机号(唯一)
	 * 
	 * @param phone
	 * @return
	 */
	private boolean verifiyPhone(String phone, long mid) {
		String sql = getCountHql + " and phone" + StringUtil.formatEqual(phone);
		if (mid > 0) {
			sql += " and id!=" + mid;
		}
		int i = this.countHqlAll(sql);
		if (i > 0) {
			return false;
		}
		return true;
	}

	@Transactional
	@Override
	public JSONReturn deleteMember(long id) {
		// TODO Auto-generated method stub
		int i = this.deleteByProperty("id", id);
		if (i > 0) {// 级联删除
			String sql = "delete from te_member_card where memberid=?";
			this.executeBySql(sql, new Long[] { id });
			sql = "delete from te_member_consume where memberid=?";
			this.executeBySql(sql, new Long[] { id });
			return JSONReturn.buildSuccess("删除成功");
		}
		return JSONReturn.buildFailure("删除失败");
	}

	@Override
	public Class<TeMember> getEntityClass() {
		// TODO Auto-generated method stub
		return TeMember.class;
	}

	@Override
	public JSONObject findListByserVal(String serVal) {
		// TODO Auto-generated method stub
		String sql = "select id,realname,phone from te_member where 1=1 ";
		sql += StringUtil.isNotEmpty(serVal) ? " and namecode "
				+ StringUtil.formatLike(serVal) : "";
		List<Map<String, Object>> list = queryResultToMap(sql);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", list);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONReturn findAll(String serVal, int griptype, String matchid) {
		// TODO Auto-generated method stub
		// String sql="SELECT a.* FROM ( SELECT * FROM TE_MEMBER WHERE 1=1 ";
		// if (StringUtil.isNotEmpty(serVal)) {
		// sql += " AND NAMECODE " + StringUtil.formatLike(serVal);
		// sql += " UNION SELECT  * FROM TE_MEMBER WHERE PHONE like '%"
		// + serVal+"' ";
		// }
		// sql += " ) a  ORDER BY a.createtime desc  ";
		String sql = "SELECT MB.* FROM TE_MEMBER MB WHERE 1=1 ";
		if (StringUtil.isNotEmpty(serVal)) {
			sql += " and (MB.NAMECODE like '%" + serVal
					+ "%' or MB.PHONE like '%" + serVal + "' )";
		}
		sql += griptype > 0 ? " and MB.griptype=" + griptype : "";
		sql += " AND  NOT EXISTS ( SELECT 	log.member_id 	FROM te_member_match_log log WHERE log.member_id = MB.id and log.match_id"
				+ StringUtil.formatEqual(matchid) + " ) ";
		sql += "  ORDER BY MB.CREATETIME DESC  ";
		List<TeMember> list = this.findSession().createSQLQuery(sql)
				.addEntity(getEntityClass()).list();
		for (TeMember teMember : list) {
			teMember.setAge(DateUtil.getAgefromBir(teMember.getBirthday()
					.split("-")[0]));// 计算年龄
		}
		return JSONReturn.buildSuccess(list);
	}

}
