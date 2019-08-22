package cn.emay.boot.base.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;

import cn.emay.orm.AbstractPojoDaoSupport;

/**
 * 基础面向对象Dao，作为父类使用，提供常用方法。
 * 
 * @author Frank
 *
 */
public class BasePojoSuperDaoImpl<E extends java.io.Serializable> extends AbstractPojoDaoSupport<E> {

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
