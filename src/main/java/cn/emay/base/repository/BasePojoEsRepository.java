package cn.emay.base.repository;

import cn.emay.elasticsearch.respoitory.PojoEsRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * 基础Dao，作为父类使用，提供常用方法。<br/>
 * es 仅可使用原生sql和jdbctemplate相关用法，不可以使用Hibernate相关用法。
 *
 * @author Frank
 */
public class BasePojoEsRepository<E extends java.io.Serializable> extends PojoEsRepository<E> {

    @Resource(name = "EsJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;
    @Resource
    protected RestHighLevelClient client;

    @Override
    protected RestHighLevelClient getClient() {
        return client;
    }

    @Override
    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
