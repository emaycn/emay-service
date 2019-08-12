package cn.emay.boot.base.dao;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;

import cn.emay.orm.AbstractPojoDaoSupport;
import cn.emay.utils.db.common.Page;

/**
 * 基础面向对象Dao，作为父类使用，提供常用方法。
 * 
 * @author Frank
 *
 */
public class BaseSuperDaoImpl<E extends java.io.Serializable> extends AbstractPojoDaoSupport<E> {

	@Resource
	protected JdbcTemplate jdbcTemplate;
	@Resource
	protected HibernateTemplate hibernateTemplate;
	@Resource
	protected SessionFactory sessionFactory;

	@Override
	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	protected HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 返回单个对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> T findSqlForObj(Class<T> clazz, String sql, List<Object> parameters) {
		Object[] args = params(parameters);
		BeanPropertyRowMapper<T> argTypes = new BeanPropertyRowMapper<T>(clazz);
		T t = jdbcTemplate.queryForObject(sql, argTypes, args);
		return t;
	}

	/**
	 * 返回分页list DTO对象
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> Page<T> findSqlForPageForMysql(Class<T> clazz, String sql, List<Object> parameters, int start, int limit) {
		return this.findSqlForPageForMysql(sql, parameters, start, limit, new BeanPropertyRowMapper<T>(clazz));
	}

	public <T> Page<T> findSqlForPageForMysql(String sql, List<Object> parameters, int start, int limit, RowMapper<T> rowMapper) {
		Integer totalCount = queryTotalCount(sql, parameters);
		StringBuffer querySql = new StringBuffer(sql);
		querySql.append(" LIMIT " + start + "," + limit + "");
		List<T> list = findSqlForListObj(querySql.toString(), parameters, rowMapper);
		Page<T> page = new Page<T>();
		page.setList(list);
		page.setNumByStartAndLimit(start, limit, totalCount);
		return page;
	}

	/**
	 * 按页获取数据
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public <T> List<T> findSqlForListForMysql(Class<T> clazz, String sql, List<Object> parameters, int currentPage, int pageSize) {
		StringBuffer querySql = new StringBuffer(sql);
		if (pageSize > 0) {
			int start = (currentPage - 1) * pageSize;
			querySql.append(" LIMIT " + start + "," + pageSize + "");
		}
		List<T> list = findSqlForListObj(querySql.toString(), parameters, new BeanPropertyRowMapper<T>(clazz));
		return list;
	}

	private Integer queryTotalCount(String sql, List<Object> parameters) {
		// 寻找from
		int fromindex = sql.toLowerCase().indexOf(" from ");
		if (fromindex < 0) {
			throw new RuntimeException("sql" + " has no from");
		}
		// 判断是否能截取最后的order
		boolean isHasOrder = false;
		int orderbyindex = sql.toLowerCase().indexOf(" order ");
		if (orderbyindex > 0) {
			isHasOrder = !sql.toLowerCase().substring(orderbyindex).contains(")");
		}
		// 截取order
		String countsql = sql;
		if (isHasOrder) {
			orderbyindex = countsql.toLowerCase().indexOf(" order ");
			countsql = countsql.substring(0, orderbyindex);
		}
		// 拼接SQL
		countsql = "select count(*)  from ( " + countsql + " ) total ";
		// System.out.println("PojoDaoImpl.class selct count sql is : " + countsql);
		Object[] args = params(parameters);
		Integer totalCount = jdbcTemplate.queryForObject(countsql, args, Integer.class);
		return totalCount;
	}

	/**
	 * 返回List dto
	 *
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public <T> List<T> findSqlForListObj(String sql, List<Object> parameters, RowMapper<T> rowMapper) {
		Object[] args = params(parameters);
		List<T> list = jdbcTemplate.query(sql, args, rowMapper);
		return list;
	}

	private Object[] params(List<Object> parameters) {
		if (null == parameters) {
			parameters = new ArrayList<Object>();
		}
		Object[] args = parameters.toArray();
		return args;
	}

	public <T> List<T> findSqlForListObj(Class<T> clazz, String sql, List<Object> parameters) {
		return this.findSqlForListObj(sql, parameters, new BeanPropertyRowMapper<T>(clazz));
	}

	/**
	 * @Title: nameJdbcBatchExec
	 * @Description: 实体批量入库
	 * @param @param beanList
	 * @param @param jdbcTemplate
	 * @param @param useId
	 * @return void
	 */
	public <T> int[] nameJdbcBatchExec(List<T> beanList, JdbcTemplate jdbcTemplate, String dbTableName, Boolean isIgnore, boolean useId) {
		if (beanList.size() == 0) {
			throw new IllegalStateException("list is empty. ");
		}
		String sql = transBean2Sql(beanList.get(0), dbTableName, isIgnore, useId);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(beanList.toArray());
		return namedParameterJdbcTemplate.batchUpdate(sql, params);
	}

	/**
	 * @Title: transBean2Sql
	 * @Description: 实体转换sql :name格式
	 * @param @param clazz
	 * @param @param useId 是否id入库
	 * @param @return
	 * @return String
	 */
	private static <T> String transBean2Sql(T clazz, String dbTableName, boolean isIgnore, Boolean useId) {
		if (null == clazz) {
			return null;
		}
		List<String> tableColumns = new ArrayList<String>();
		List<String> modelColumns = new ArrayList<String>();
		String simpleName = clazz.getClass().getSimpleName();
		if (StringUtils.isEmpty(dbTableName)) {
			dbTableName = classNameToDbTableName(simpleName);
		}
		try {
			String fileToConvert = "";
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String filedName = property.getName();
				if (!"class".equals(filedName)) {
					if ("id".equals(filedName) && !useId) {
						continue;
					}
					fileToConvert = fileToConvert(filedName); // 处理大小写字段
					modelColumns.add(":" + filedName);
					tableColumns.add(fileToConvert);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer buff = new StringBuffer();
		if (isIgnore) {
			buff.append("insert ignore into ");
		} else {
			buff.append("insert into ");
		}
		buff.append(dbTableName).append(" ");
		buff.append("(").append(StringUtils.join(tableColumns, ",")).append(")");
		buff.append(" values ");
		buff.append("(").append(StringUtils.join(modelColumns, ",")).append(")");
		return buff.toString();
	}

	private static String classNameToDbTableName(String fileName) {
		int fileNameLength = fileName.length();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < fileNameLength; i++) {
			char c = fileName.charAt(i);
			if (Character.isUpperCase(c) && i > 1) {
				buff.append("_");
			}
			buff.append(c);
		}
		return buff.toString().toUpperCase();
	}

	private static String fileToConvert(String fileName) {
		int fileNameLength = fileName.length();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < fileNameLength; i++) {
			char c = fileName.charAt(i);
			if (Character.isUpperCase(c)) {
				buff.append("_");
			}
			buff.append(c);
		}
		return buff.toString().toUpperCase();
	}
}
