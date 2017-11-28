package com.java.back.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.back.constant.ClubConst;
import com.java.back.dao.support.AbstractDao;
import com.java.back.model.forum.TeHeadpic;
import com.java.back.service.HeadPicService;
import com.java.back.support.JSONReturn;
import com.java.back.utils.DateTimeUtil;
import com.java.back.utils.StringUtil;
import com.java.back.utils.fileUtils.FileOperations;

@Service
@Transactional(readOnly=true)
public class HeadPicServiceImpl extends AbstractDao<TeHeadpic> implements
		HeadPicService {

	@Override
	public TeHeadpic findPicById(long picid) {
		// TODO Auto-generated method stub
		TeHeadpic teHeadpic = get(picid);
		return teHeadpic;
	}

	@Transactional
	@Override
	public JSONReturn modefyPicById(long id, int ifuss) {
		// TODO Auto-generated method stub
		TeHeadpic headpic = get(id);
		if (ifuss == ClubConst.INVALID) {// 作废
			headpic.setPorder(0);
		} else {// 启用
			int countAll = countAll("select count(*) from te_headpic where ifuseless="
					+ ClubConst.VALID);
			if (countAll > 4) {
				return JSONReturn.buildFailure("操作失败,只能设置5张图片");
			}
			headpic.setPorder(getMaxporder() + 1);
		}
		headpic.setIfuseless(ifuss);
		boolean update = update(headpic);
		if (update) {
			return JSONReturn.buildSuccess("操作成功");
		}
		return JSONReturn.buildFailure("操作失败");
	}

	@Override
	@Transactional
	public JSONReturn addPic(TeHeadpic headpic) {
		// TODO Auto-generated method stub
		headpic.setIfuseless(ClubConst.INVALID);// 默认不启用的图片
		headpic.setCreatetime(DateTimeUtil.getCurrentTime());
		headpic.setPorder(getMaxporder() + 1);
		boolean save = this.save(headpic);
		if (save) {
			return JSONReturn.buildSuccess("新增成功,若使用请启用该图片");
		}
		return JSONReturn.buildFailure("新增失败");
	}

	/**
	 * 使用中的最大的顺序
	 * 
	 * @return
	 */
	private int getMaxporder() {
		String sql = "select max(porder) from  te_headpic where ifuseless=?";
		Object uniqueResult = findSession().createSQLQuery(sql)
				.setInteger(0, ClubConst.VALID).uniqueResult();
		if (uniqueResult == null) {
			return 0;
		}
		return Integer.parseInt(uniqueResult.toString());
	}

	/**
	 * 使用中的最小的顺序
	 * 
	 * @return
	 */
	private int getMinporder() {
		String sql = "select min(porder) from  te_headpic where ifuseless=?";
		Object uniqueResult = findSession().createSQLQuery(sql)
				.setInteger(0, ClubConst.VALID).uniqueResult();
		if (uniqueResult == null) {
			return 0;
		}
		return Integer.parseInt(uniqueResult.toString());
	}

	@Override
	@Transactional
	public JSONReturn deletePic(long picid, HttpServletRequest request) {
		// TODO Auto-generated method stub
		TeHeadpic teHeadpic = get(picid);
		String rootPath = request.getSession().getServletContext()
				.getRealPath("");
		FileOperations.delefile(rootPath, teHeadpic.getPicurl());
		int deleteByProperty = deleteByProperty("id", picid);
		return deleteByProperty > 0 ? JSONReturn.buildSuccess("删除成功")
				: JSONReturn.buildFailure("删除失败");
	}

	@Override
	public JSONReturn findAllPic(String title) {
		// TODO Auto-generated method stub
		Criteria criteria = findSession().createCriteria(getEntityClass());
		if (StringUtil.isNotEmpty(title)) {
			criteria.add(Restrictions.like("title", title, MatchMode.ANYWHERE));
		}
		List<TeHeadpic> findAll = criteria
				.add(Restrictions.eq("ifuseless", ClubConst.INVALID))
				.addOrder(Order.desc("createtime")).list();
		return JSONReturn.buildSuccess(findAll);
	}

	@Override
	public Class<TeHeadpic> getEntityClass() {
		// TODO Auto-generated method stub
		return TeHeadpic.class;
	}

	@Transactional
	@Override
	public JSONReturn moveHead(long id) {
		// TODO Auto-generated method stub
		try {
			TeHeadpic headpic = get(id);
			int order = headpic.getPorder();
			if (order == getMinporder()) {
				return JSONReturn.buildFailure("已经到头啦");
			}
			List<TeHeadpic> list = findSession()
					.createCriteria(getEntityClass())
					.add(Restrictions.eq("ifuseless", ClubConst.VALID))
					.add(Restrictions.lt("porder", order))
					.addOrder(Order.desc("porder")).list();

			TeHeadpic teHeadpic = list.get(0);
			int neworder = teHeadpic.getPorder();
			teHeadpic.setPorder(order);
			update(teHeadpic);

			headpic.setPorder(neworder);// 操作的数据向前移动
			update(headpic);
			return JSONReturn.buildSuccess("移动成功");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONReturn.buildFailure("操作失败");
	}

	@Override
	public JSONReturn findUsePic() {
		// TODO Auto-generated method stub
		List<TeHeadpic> findAll = findSession()
				.createCriteria(getEntityClass())
				.add(Restrictions.eq("ifuseless", ClubConst.VALID))
				.addOrder(Order.asc("porder")).list();
		return JSONReturn.buildSuccess(findAll);
	}

}
