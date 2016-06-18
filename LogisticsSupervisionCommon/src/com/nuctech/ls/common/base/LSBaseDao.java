/**
 * 
 */
package com.nuctech.ls.common.base;

import static com.nuctech.util.SqlRemoveUtils.removeFetchKeyword;
import static com.nuctech.util.SqlRemoveUtils.removeOrders;
import static com.nuctech.util.SqlRemoveUtils.removeSelect;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javacommon.xsqlbuilder.SafeSqlProcesser;
import javacommon.xsqlbuilder.SafeSqlProcesserFactory;
import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;
import javacommon.xsqlbuilder.safesql.DirectReturnSafeSqlProcesser;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionFactoryImpl;

import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.util.NuctechUtil;

/**
 * dao 基类
 * 
 * @author sunming
 *
 */
public class LSBaseDao<T, PK extends Serializable> {

	public static final String FIRSTRESULT = "FirstResult";
	public static final String MAXRESULTS = "MaxResults";
	private Class<T> entityClass;
	protected final Logger logger = Logger.getLogger(this.getClass().getName());

	@Resource
	protected SessionFactory sessionFactory;

	/**
	 * create session
	 * 
	 * @return session
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 添加
	 * 
	 * @param entity
	 */
	public void persist(T entity) {
		sessionFactory.getCurrentSession().persist(entity);
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(T entity) {
		sessionFactory.getCurrentSession().save(entity);
		sessionFactory.getCurrentSession().flush();
	}

	/**
	 * @param entity
	 */
	public void saveOrUpdate(T entity) {
		Session session = sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		session.saveOrUpdate(entity);
		trans.commit();
		session.close();
	}

	/**
	 * 修改
	 * 
	 * @param entity
	 */
	public void update(T entity) {
		sessionFactory.getCurrentSession().update(entity);
	}

	/**
	 * 修改
	 * 
	 * @param entity
	 */
	public void merge(T entity) {
		sessionFactory.getCurrentSession().merge(entity);
	}

	/**
	 * 删除
	 * 
	 * @param entity
	 */
	public void delete(T entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	public void delete(List<T> list) {
		for (T entity : list) {
			this.delete(entity);
		}
	}

	/**
	 * 根据主键删除
	 * 
	 * @param id
	 */
	public void deleteById(PK id) {
		T entity = this.findById(id);
		this.delete(entity);
	}

	/**
	 * 根据属性组合删除数据
	 * 
	 * @param propertiesMap
	 */
	public void deleteByPropertys(final HashMap<String, Object> propertiesMap) {
		List<T> list = this.findAllBy(propertiesMap, null);
		for (T entity : list) {
			this.delete(entity);
		}
	}

	public int batchDeleteByHql(String hql, final HashMap<String, Object> propertiesMap) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (propertyName != null && value != null) {
					query.setParameter(propertyName, value);
				}
			}
		}
		int deletedCount = query.executeUpdate();
		sessionFactory.getCurrentSession().flush();
		return deletedCount;
	}

	@SuppressWarnings("unchecked")
	public T findById(PK id) {
		assert (id != null);
		T entity = (T) sessionFactory.getCurrentSession().get(getEntityClass(), id);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T findByProperty(final String propertyName, final Object value) {
		return (T) sessionFactory.getCurrentSession().createCriteria(this.getEntityClass()).add(Restrictions.eq(propertyName, value))
				.setMaxResults(1).uniqueResult();

	}

	@SuppressWarnings("unchecked")
	public List<T> findAllBy(final String propertyName, final Object value) {
		return sessionFactory.getCurrentSession().createCriteria(this.getEntityClass()).add(Restrictions.eq(propertyName, value)).list();

	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return sessionFactory.getCurrentSession().createCriteria(this.getEntityClass()).list();
	}

	@SuppressWarnings("unchecked")
	public T findByProperty(final String propertyName, final Object value, HashMap<String, String> orderby) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertyName != null && value != null) {
			crit.add(Restrictions.eq(propertyName, value));
		}
		if (orderby != null) {
			Iterator<String> it = orderby.keySet().iterator();
			while (it.hasNext()) {
				String orderName = it.next();
				String orderType = orderby.get(orderName);
				if ("desc".equals(orderType.toLowerCase())) {
					crit.addOrder(Order.desc(orderName));
				} else {
					crit.addOrder(Order.asc(orderName));
				}
			}
		}
		crit.setMaxResults(1);
		return (T) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T findByProperties(final HashMap<String, Object> propertiesMap, HashMap<String, String> orderby) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (propertyName != null && value != null) {
					crit.add(Restrictions.eq(propertyName, value));
				}
			}
		}
		if (orderby != null) {
			Iterator<String> it = orderby.keySet().iterator();
			while (it.hasNext()) {
				String orderName = it.next();
				String orderType = orderby.get(orderName);
				if ("desc".equals(orderType.toLowerCase())) {
					crit.addOrder(Order.desc(orderName));
				} else {
					crit.addOrder(Order.asc(orderName));
				}
			}
		}
		crit.setMaxResults(1);
		return (T) crit.uniqueResult();
	}

	// 支持模糊查询,in查询
	@SuppressWarnings("unchecked")
	public T findByProperties(final HashMap<String, Object> propertiesMap, final HashMap<String, Object> propertiesLikeMap,
			final HashMap<String, Object> propertiesInMap, HashMap<String, String> orderby) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (propertyName != null && value != null) {
					crit.add(Restrictions.eq(propertyName, value));
				}
			}
		}
		if (propertiesLikeMap != null) {
			Iterator<String> keyIt = propertiesLikeMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesLikeMap.get(propertyName);
				if (propertyName != null && value != null) {
					crit.add(Restrictions.ilike(propertyName, value.toString(), MatchMode.ANYWHERE));
				}
			}
		}
		if (propertiesInMap != null) {
			Iterator<String> keyIt = propertiesInMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value[] = (Object[]) propertiesInMap.get(propertyName);
				if (propertyName != null && value != null) {
					crit.add(Restrictions.in(propertyName, value));
				}
			}
		}
		if (orderby != null) {
			Iterator<String> it = orderby.keySet().iterator();
			while (it.hasNext()) {
				String orderName = it.next();
				String orderType = orderby.get(orderName);
				if ("desc".equals(orderType.toLowerCase())) {
					crit.addOrder(Order.desc(orderName));
				} else {
					crit.addOrder(Order.asc(orderName));
				}
			}
		}
		crit.setMaxResults(1);
		return (T) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllBy(final String propertyName, final Object value, HashMap<String, String> orderby) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertyName != null && value != null) {
			crit.add(Restrictions.eq(propertyName, value));
		}
		if (orderby != null) {
			Iterator<String> it = orderby.keySet().iterator();
			while (it.hasNext()) {
				String orderName = it.next();
				String orderType = orderby.get(orderName);
				if ("desc".equals(orderType.toLowerCase())) {
					crit.addOrder(Order.desc(orderName));
				} else {
					crit.addOrder(Order.asc(orderName));
				}
			}
		}
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllBy(final HashMap<String, Object> propertiesMap, HashMap<String, String> orderby) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (FIRSTRESULT.equals(propertyName) && value != null) {
					crit.setFirstResult(Integer.parseInt(value.toString()));
				} else if (MAXRESULTS.equals(propertyName) && value != null) {
					crit.setMaxResults(Integer.parseInt(value.toString()));
				} else {
					if (propertyName != null && value != null) {
						crit.add(Restrictions.eq(propertyName, value));
					}
				}
			}
		}
		if (orderby != null) {
			Iterator<String> it = orderby.keySet().iterator();
			while (it.hasNext()) {
				String orderName = it.next();
				String orderType = orderby.get(orderName);
				if ("desc".equals(orderType.toLowerCase())) {
					crit.addOrder(Order.desc(orderName));
				} else {
					crit.addOrder(Order.asc(orderName));
				}
			}
		}
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllBy(final HashMap<String, Object> propertiesMap, final HashMap<String, Object> propertiesLikeMap,
			HashMap<String, String> orderby) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (FIRSTRESULT.equals(propertyName) && value != null) {
					crit.setFirstResult(Integer.parseInt(value.toString()));
				} else if (MAXRESULTS.equals(propertyName) && value != null) {
					crit.setMaxResults(Integer.parseInt(value.toString()));
				} else {
					if (propertyName != null && value != null) {
						crit.add(Restrictions.eq(propertyName, value));
					}
				}
			}
		}
		if (propertiesLikeMap != null) {
			Iterator<String> keyIt = propertiesLikeMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesLikeMap.get(propertyName);
				if (FIRSTRESULT.equals(propertyName) && value != null) {
					crit.setFirstResult(Integer.parseInt(value.toString()));
				} else if (MAXRESULTS.equals(propertyName) && value != null) {
					crit.setMaxResults(Integer.parseInt(value.toString()));
				} else {
					if (NuctechUtil.isNotNull(propertyName) && NuctechUtil.isNotNull(value)) {
						crit.add(Restrictions.ilike(propertyName, value.toString(), MatchMode.ANYWHERE));
					}
				}
			}
		}
		if (orderby != null) {
			Iterator<String> it = orderby.keySet().iterator();
			while (it.hasNext()) {
				String orderName = it.next();
				String orderType = orderby.get(orderName);
				if ("desc".equals(orderType.toLowerCase())) {
					crit.addOrder(Order.desc(orderName));
				} else {
					crit.addOrder(Order.asc(orderName));
				}
			}
		}
		return crit.list();
	}

	public int getCountAllBy(final HashMap<String, Object> propertiesMap) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(this.getEntityClass());
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (FIRSTRESULT.equals(propertyName) && value != null) {
					// crit.setFirstResult(Integer.parseInt(value.toString()));
				} else if (MAXRESULTS.equals(propertyName) && value != null) {
					// crit.setMaxResults(Integer.parseInt(value.toString()));
				} else {
					if (propertyName != null && value != null) {
						crit.add(Restrictions.eq(propertyName, value));
					}
				}
			}
		}
		// 得到总记录数
		crit.setProjection(Projections.rowCount());
		return ((Number) crit.uniqueResult()).intValue();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getEntityClass() {
		if (this.entityClass == null) {
			this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		return this.entityClass;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllByHql(String hql) {
		return this.sessionFactory.getCurrentSession().createQuery(hql).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllByHql(String hql, int rowNum) {
		return this.sessionFactory.getCurrentSession().createQuery(hql).setMaxResults(rowNum).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllByHql(String hql, final HashMap<String, Object> propertiesMap, int firstResult, int maxResults) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (propertyName != null && value != null) {
					query.setParameter(propertyName, value);
				}
			}
		}
		return query.setFirstResult(firstResult).setMaxResults(maxResults).list();
	}

	@SuppressWarnings("unchecked")
	public int getCountAllByHql(String hql, final HashMap<String, Object> propertiesMap) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (propertiesMap != null) {
			Iterator<String> keyIt = propertiesMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String propertyName = keyIt.next();
				Object value = propertiesMap.get(propertyName);
				if (propertyName != null && value != null) {
					query.setParameter(propertyName, value);
				}
			}
		}
		// 得到总记录
		return ((Number) query.uniqueResult()).intValue();
	}

	public List<T> findAllByHql(String hql, HashMap paramMap, int rowNum) {
		Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
		if (paramMap != null) {
			Iterator keyIt = paramMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String key = (String) keyIt.next();
				query.setParameter(key, paramMap.get(key));
			}
		}
		return query.setMaxResults(rowNum).list();
	}

	public static Query setQueryParameters(Query q, Map params) {
		for (Iterator it = params.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			q.setParameter((String) entry.getKey(), entry.getValue());
		}
		return q;
	}

	protected XsqlBuilder getXsqlBuilder() {
		SessionFactoryImpl sf = (SessionFactoryImpl) this.sessionFactory;
		Dialect dialect = sf.getDialect();
		SafeSqlProcesser safeSqlProcesser = SafeSqlProcesserFactory.getFromCacheByHibernateDialect(dialect);
		XsqlBuilder builder = new XsqlBuilder(safeSqlProcesser);

		if (builder.getSafeSqlProcesser().getClass() == DirectReturnSafeSqlProcesser.class) {
			logger.info("getXsqlBuilder(): 故意报错,你未开启Sql安全过滤,单引号等转义字符在拼接sql时需要转义,不然会导致Sql注入攻击的安全问题，请修改源码使用new XsqlBuilder(SafeSqlProcesserFactory.getDataBaseName())开启安全过滤");
		}
		return builder;
	}

    public PageList pageQuery(final String queryString, final PageQuery<Map> pageQuery) {
        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        filtersMap.put("sortColumns", pageQuery.getSortColumns());
        
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        final String countQueryString = "select count(*) " + removeSelect(removeFetchKeyword((queryString)));
        XsqlFilterResult countQueryXsqlResult = builder.generateHql(countQueryString, pageQuery.getFilters());
        Query query = setQueryParameters(this.sessionFactory.getCurrentSession().createQuery(queryXsqlResult.getXsql()),
                queryXsqlResult.getAcceptedFilters());
        Query countQuery = setQueryParameters(this.sessionFactory.getCurrentSession().createQuery(
                removeOrders(countQueryXsqlResult.getXsql())), countQueryXsqlResult.getAcceptedFilters());
        PageList pageList = new PageList();
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        int firstRow = pageList.getFirstRecordIndex();
        int maxResults = pageQuery.getPageSize();
        pageList.addAll(query.setFirstResult(firstRow).setMaxResults(maxResults).list());
        pageList.setTotalItems(((Number) countQuery.uniqueResult()).intValue());
        return pageList;
    }
    
    public List findAllList(final String queryString, final PageQuery<Map> pageQuery) {
    	XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        filtersMap.put("sortColumns", pageQuery.getSortColumns());
        
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        final String countQueryString = "select count(*) " + removeSelect(removeFetchKeyword((queryString)));
        XsqlFilterResult countQueryXsqlResult = builder.generateHql(countQueryString, pageQuery.getFilters());
        Query query = setQueryParameters(this.sessionFactory.getCurrentSession().createQuery(queryXsqlResult.getXsql()),
                queryXsqlResult.getAcceptedFilters());
        Query countQuery = setQueryParameters(this.sessionFactory.getCurrentSession().createQuery(
                removeOrders(countQueryXsqlResult.getXsql())), countQueryXsqlResult.getAcceptedFilters());
        return query.list();
    }
    
    /**
     * 拼HQL或SQL查询时的查询条件
     * 
     * @param query
     * @param propertiesMap
     * @return
     */
    protected Query buildQueryCondition(Query query, HashMap<String, Object> propertiesMap)
            throws HibernateException {
        if (propertiesMap != null) {
            Iterator<String> keyIt = propertiesMap.keySet().iterator();
            while (keyIt.hasNext()) {
                String propertyName = keyIt.next();
                Object value = propertiesMap.get(propertyName);
                if (propertyName != null && value != null && value instanceof Collection) {
                    @SuppressWarnings("rawtypes")
                    Collection v = (Collection) value;
                    if (propertyName != null && v.size() > 0) {
                        query.setParameterList(propertyName, v);
                    }
                } else if (propertyName != null && value != null && value instanceof Object[]) {
                    Object[] v = (Object[]) value;
                    if (propertyName != null && v.length > 0) {
                        query.setParameterList(propertyName, v);
                    }
                } else if (propertyName != null && value != null) {
                    query.setParameter(propertyName, value);
                }
            }
        }
        return query;
    }


	/**
	 * 通过hql脚本进行批量修改或删除
	 * 
	 * @param hql
	 *            修改或删除语句
	 * @param propertiesMap
	 *            HashMap<String, Object>，条件中字段名，字段值map
	 * @return 批量修改或删除数据的数量
	 */
	public int batchUpdateOrDeleteByHql(String hql, HashMap<String, Object> propertiesMap) throws HibernateException {
		Query query = getSession().createQuery(hql);
		// 拼查询条件
		query = buildQueryCondition(query, propertiesMap);
		int updateCount = query.executeUpdate();
		return updateCount;
	}

}
