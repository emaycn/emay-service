package cn.emay.base.dao;

import cn.emay.orm.AbstractPojoDaoSupport;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 基础面向对象Dao，作为父类使用，提供常用方法。
 *
 * @author Frank
 */
public class BasePojoSuperDaoImpl<E extends java.io.Serializable> extends AbstractPojoDaoSupport<E> {

    @Resource(name = "MysqlJdbcTemplate")
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

    protected void updateIsDelete(String tablename, List<Long> ids) {
        String sql = String.format("update %s set is_delete=1 where id=? ", tablename);
        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, ids.get(i));
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });
    }

}
