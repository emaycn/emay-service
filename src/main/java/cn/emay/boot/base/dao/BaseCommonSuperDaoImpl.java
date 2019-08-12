package cn.emay.boot.base.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;

import cn.emay.orm.AbstractDaoSupport;

/**
 * 基础Dao，作为父类使用，提供常用方法。
 * @author Frank
 *
 */
public class BaseCommonSuperDaoImpl extends AbstractDaoSupport {

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

}
