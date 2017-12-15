package com.java.back.dao.support;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.java.back.constant.PageConstant;

public abstract class AbstractDao<T> {

	@Autowired
	private SessionFactory sessionFactory;

	public abstract Class<T> getEntityClass();

	/**
	 * 查询的hql
	 */
	public String getHql = "from " + getEntityClass().getName() + " where 1=1 ";

	/**
	 * 查询数量的hql
	 */
	public String getCountHql = "select count(*) from "
			+ getEntityClass().getName() + " where 1=1 ";

	public Session findSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return findSession().createCriteria(getEntityClass()).list();
	}

	/**
	 * 查询所有,根据时间排序Desc
	 * 
	 * @param timefiled
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllByOrderDesc(String timefiled) {
		return findSession().createCriteria(getEntityClass())
				.addOrder(Order.desc(timefiled)).list();
	}

	/**
	 * 查询所有,根据时间排序Asc
	 * 
	 * @param timefiled
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllByOrderAsc(String timefiled) {
		return findSession().createCriteria(getEntityClass())
				.addOrder(Order.asc(timefiled)).list();
	}

	/**
	 * 根据参数查询单个对象
	 * 
	 * @param pro
	 *            参数名
	 * @param val
	 *            参数值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findUniqueByProperty(String pro, Object val) {
		return (T) findSession().createCriteria(getEntityClass())
				.add(Restrictions.eq(pro, val)).uniqueResult();
	}

	/**
	 * 根据一个参数查询列表
	 * 
	 * @param pro
	 *            参数名
	 * @param val
	 *            参数值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByProperty(String pro, Object val) {
		return findSession().createCriteria(getEntityClass())
				.add(Restrictions.eq(pro, val)).list();
	}

	/**
	 * 查询所有数据条数
	 * 
	 * @return
	 */
	public int findCounnt() {
		String query = "select count(*) from " + getEntityClass().getName();
		return Integer.parseInt(findSession().createQuery(query).uniqueResult()
				.toString());
	}

	/**
	 * 查询条数(like形式)
	 * 
	 * @param pro
	 * @param val
	 * @return
	 */
	public int findCountLike(String pro, String val) {
		String query = "select count(*) from " + getEntityClass().getName()
				+ " where " + pro + " like '%" + val + "%'";
		return Integer.parseInt(findSession().createQuery(query).uniqueResult()
				.toString());
	}

	/**
	 * 根据一个参数查询条数
	 * 
	 * @param pro
	 *            参数名
	 * @param searchValue
	 *            参数值
	 * @return
	 */
	public int findCountByProperty(String pro, Object searchValue) {
		String query = "select count(*) from " + getEntityClass().getName()
				+ " where " + pro + " = '" + searchValue + "'";
		return Integer.parseInt(findSession().createQuery(query).uniqueResult()
				.toString());
	}

	/**
	 * 根据不是string格式的参数删除
	 * 
	 * @param pro
	 * @param value
	 */
	public int deleteByProperty(String pro, Object value) {
		String query = "delete from " + getEntityClass().getName() + " where "
				+ pro + "=" + value.toString();
		return findSession().createQuery(query).executeUpdate();
	}

	/**
	 * 根据类型为string形式的参数删除
	 * 
	 * @param pro
	 * @param val
	 */
	public int deleteByPropertyString(String pro, Object val) {
		String query = "delete from " + getEntityClass().getName() + " where "
				+ pro + "='" + val.toString() + "'";
		return findSession().createQuery(query).executeUpdate();
	}

	/**
	 * 根据id查询单个对象
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findById(long id) {
		return (T) findSession().get(getEntityClass().getName(), id);
	}

	/**
	 * 保存对象
	 * 
	 * @param obj
	 */
	public boolean save(Object obj) {
		try {
			findSession().save(obj);
			return true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除对象
	 * 
	 * @param obj
	 */
	public boolean delete(Object obj) {
		try {
			findSession().delete(obj);
			return true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新对象
	 * 
	 * @param obj
	 */
	public boolean update(Object obj) {
		try {
			findSession().update(obj);
			return true;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据sql语句查询条数
	 * 
	 * @param sql
	 * @return
	 */
	public int countAll(String sql) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createSQLQuery(sql);
		int parseInt = Integer.parseInt(query.uniqueResult().toString());
		return parseInt;
	}

	/**
	 * 根据hql查询条数
	 * 
	 * @param hql
	 * @return
	 */
	public int countHqlAll(String hql) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createQuery(hql);
		int parseInt = Integer.parseInt(query.uniqueResult().toString());
		return parseInt;
	}

	/**
	 * sql语句分页查询
	 * 
	 * @param sql
	 * @param params
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> querySqlForList(String sql, Object[] params, int page) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createSQLQuery(sql);
		query = setParamForquery(query, params);
		query.setFirstResult((page - 1) * PageConstant.PAGE_LIST);
		query.setMaxResults(PageConstant.PAGE_LIST);
		List<Object[]> list = query.list();
		return list;
	}

	/**
	 * hql语句分页查询
	 * 
	 * @param hql
	 * @param params
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryHqlForList(String hql, Object[] params, int page) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createQuery(hql);
		query = setParamForquery(query, params);
		query.setFirstResult((page - 1) * PageConstant.PAGE_LIST);
		query.setMaxResults(PageConstant.PAGE_LIST);
		return query.list();
	}

	/**
	 * 给query设置参数
	 * 
	 * @param query
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Query setParamForquery(Query query, Object[] params) {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				// query.setParameter(i, params[i]);//通用的设置参数的方法

				if (params[i] instanceof String) {
					query.setString(i, params[i].toString());
				} else if (params[i] instanceof Integer) {
					query.setInteger(i, Integer.parseInt(params[i].toString()));
				} else if (params[i] instanceof java.sql.Date) {
					query.setDate(i, new Date(params[i].toString()));
				} else if (params[i] instanceof Long) {
					query.setLong(i, Long.parseLong(params[i].toString()));
				}
			}
		}
		return query;
	}

	/**
	 * 根据sql获取列表
	 * 
	 * @param sql
	 * @param params
	 *            参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getListBySql(String sql, Object[] params) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createSQLQuery(sql);
		query = setParamForquery(query, params);
		return query.list();
	}

	/**
	 * hql语句获取列表
	 * 
	 * @param hql
	 * @param params
	 *            参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryHqlForList(String hql, Object[] params) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createQuery(hql);
		query = setParamForquery(query, params);
		return query.list();
	}

	/**
	 * 执行带有参数的sql语句(用于删除,修改等情况)
	 * 
	 * @param sql
	 * @param params
	 *            (传null 为不带参数)
	 * @return
	 */
	public int executeBySql(String sql, Object[] params) {
		// TODO Auto-generated method stub
		Query query = this.findSession().createSQLQuery(sql);
		query = setParamForquery(query, params);
		return query.executeUpdate();
	}

	/**
	 * 根据主键获取该Entity对象
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable id) {
		// TODO Auto-generated method stub
		return (T) findSession().get(getEntityClass(), id);
	}

	/**
	 * 根据主键获取单个对象
	 * 
	 * @param id
	 * @param beanCalss
	 *            对象
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T getModel(Serializable id, Class<T> beanCalss) {
		// TODO Auto-generated method stub
		T bean = (T) findSession().get(beanCalss, id);
		return bean;
	}

	/**
	 * 执行带输出参数存储过程
	 * 
	 * @param callSql 存储过程的名称组合:"{call mycount(?, ?,? )}");
	 * @param inParameters
	 */
	public int prepareCallAndReturn(final String callSql,
			final Object... inParameters) {
		try {
			CallableStatement cs = findSession().connection().prepareCall(
					callSql);
			int inParametersLength = inParameters.length;
			for (int i = 0; i < inParametersLength; i++) {
				cs.setObject(i + 1, inParameters[i]);
			}
			cs.registerOutParameter(inParametersLength + 1, Types.INTEGER);
			cs.executeUpdate();
			return cs.getInt(inParametersLength + 1);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 调用存储过程
	 * 
	 * @param status
	 * @param id
	 */
	public void prepareCall(String status, String id) {
		try {
			Connection conn = findSession().connection();
			CallableStatement proc = null;
			proc = conn.prepareCall("{   call   set_death_age(?,   ?)   }");
			proc.setString(1, status);
			proc.setString(2, id);
			proc.execute();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// findSession().close();
	}

	/**
	 * sql语句查询结果映射为map形式
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryResultToMap(String sql, int page) {
		// TODO Auto-generated method stub
		Query query = findSession().createSQLQuery(sql).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		query.setFirstResult((page - 1) * PageConstant.PAGE_LIST);
		query.setMaxResults(PageConstant.PAGE_LIST);
		return query.list();
	}

	/**
	 * sql语句查询结果映射为map形式
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryResultToMap(String sql) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = findSession().createSQLQuery(sql)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}

	/**
	 * 保存或修改对象(merge)
	 * 
	 * @param object
	 * @return
	 */
	public boolean saveOrUpdate(Object object) {
		// TODO Auto-generated method stub
		try {
			findSession().merge(object);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
