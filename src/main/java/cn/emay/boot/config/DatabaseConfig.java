package cn.emay.boot.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.hibernate.SessionFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 数据库配置<br/>
 * 包含hibernate、基于AOP的事物控制
 * 
 * @author Frank
 *
 */
@Configuration
@ConfigurationProperties(prefix = "db")
@EnableAutoConfiguration(exclude = HibernateJpaAutoConfiguration.class)
@Order(0)
public class DatabaseConfig {

	private DataSourceInfo datasource;

	private HibernateInfo hibernate;

	@Autowired
	private ResourceLoader rl;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		PoolProperties properties = new PoolProperties();
		properties.setUrl(datasource.getUrl());
		properties.setDriverClassName(datasource.getDriverClassName());
		properties.setUsername(datasource.getUsername());
		properties.setPassword(datasource.getPassword());
		properties.setMaxActive(datasource.getMaxActive());
		properties.setMaxIdle(datasource.getMaxIdle());
		properties.setMinIdle(datasource.getMinIdle());
		properties.setInitialSize(datasource.getInitialSize());
		properties.setTimeBetweenEvictionRunsMillis(datasource.getTimeBetweenEvictionRunsMillis());
		properties.setNumTestsPerEvictionRun(datasource.getNumTestsPerEvictionRun());
		properties.setLogAbandoned(datasource.isLogAbandoned());
		properties.setTestWhileIdle(true);
		properties.setValidationQuery("select 1");
		return new org.apache.tomcat.jdbc.pool.DataSource(properties);
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public LocalSessionFactoryBean localSessionFactoryBean() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", hibernate.getDialect());
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hibernate.getHbm2ddl_auto());
		hibernateProperties.setProperty("hibernate.show_sql", hibernate.getShow_sql().toString());
		hibernateProperties.setProperty("hibernate.format_sql", hibernate.getFormat_sql().toString());
		hibernateProperties.setProperty("hibernate.jdbc.batch_size", hibernate.getJdbc_batchSize().toString());
		hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", hibernate.getEnable_lazy_load_no_trans().toString());
		hibernateProperties.setProperty("hibernate.current_session_context_class", hibernate.getCurrent_session_context_class());
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		if (hibernate.getPackageScan() != null) {
			sessionFactory.setPackagesToScan(hibernate.getPackageScan().split(","));
		}
		if (hibernate.getMappingResources() != null) {
			sessionFactory.setMappingLocations(loadResources(hibernate.getMappingResources().split(",")));
		}
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	protected SessionFactory sessionFactory() {
		return localSessionFactoryBean().getObject();
	}

	public Resource[] loadResources(String... mResources) {
		if (mResources == null || mResources.length == 0) {
			return null;
		}
		List<Resource> resourcesList = new ArrayList<>();
		for (String resource : mResources) {
			try {
				Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(rl).getResources(resource);
				resourcesList.addAll(Arrays.asList(resources));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return resourcesList.toArray(new Resource[resourcesList.size()]);
	}

	@Bean
	public HibernateTemplate hibernateTemplate() {
		return new HibernateTemplate(sessionFactory());
	}

	@Bean
	public PlatformTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory());
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

	@Bean(name = "MytransactionInterceptor")
	public TransactionInterceptor transactionInterceptor() {
		Properties properties = new Properties();
		properties.setProperty("add*", "PROPAGATION_REQUIRED");
		properties.setProperty("save*", "PROPAGATION_REQUIRED");
		properties.setProperty("delete*", "PROPAGATION_REQUIRED");
		properties.setProperty("remove*", "PROPAGATION_REQUIRED");
		properties.setProperty("update*", "PROPAGATION_REQUIRED");
		properties.setProperty("reset*", "PROPAGATION_REQUIRED");
		properties.setProperty("modify*", "PROPAGATION_REQUIRED");
		properties.setProperty("change*", "PROPAGATION_REQUIRED");
		properties.setProperty("on*", "PROPAGATION_REQUIRED");
		properties.setProperty("off*", "PROPAGATION_REQUIRED");
		properties.setProperty("select*", "PROPAGATION_SUPPORTS,readOnly");
		properties.setProperty("find*", "PROPAGATION_SUPPORTS,readOnly");
		properties.setProperty("find*", "PROPAGATION_SUPPORTS,readOnly");
		properties.setProperty("get*", "PROPAGATION_SUPPORTS,readOnly");
		return new TransactionInterceptor(hibernateTransactionManager(), properties);
	}

	@Bean
	public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
		BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
		creator.setInterceptorNames("MytransactionInterceptor");
		creator.setBeanNames("*Service", "*ServiceImpl");
		creator.setProxyTargetClass(true);
		return creator;
	}

	public DataSourceInfo getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSourceInfo datasource) {
		this.datasource = datasource;
	}

	public HibernateInfo getHibernate() {
		return hibernate;
	}

	public void setHibernate(HibernateInfo hibernate) {
		this.hibernate = hibernate;
	}

	public static class HibernateInfo {
		private String dialect;
		private String hbm2ddl_auto = "none";
		private Boolean show_sql = false;
		private Boolean format_sql = false;
		private Integer jdbc_batchSize = 50;
		private Boolean enable_lazy_load_no_trans = false;
		private String current_session_context_class;
		private String packageScan;
		private String mappingResources;

		public String getDialect() {
			return dialect;
		}

		public void setDialect(String dialect) {
			this.dialect = dialect;
		}

		public String getHbm2ddl_auto() {
			return hbm2ddl_auto;
		}

		public void setHbm2ddl_auto(String hbm2ddl_auto) {
			this.hbm2ddl_auto = hbm2ddl_auto;
		}

		public Integer getJdbc_batchSize() {
			return jdbc_batchSize;
		}

		public void setJdbc_batchSize(Integer jdbc_batchSize) {
			this.jdbc_batchSize = jdbc_batchSize;
		}

		public String getCurrent_session_context_class() {
			return current_session_context_class;
		}

		public void setCurrent_session_context_class(String current_session_context_class) {
			this.current_session_context_class = current_session_context_class;
		}

		public String getPackageScan() {
			return packageScan;
		}

		public void setPackageScan(String packageScan) {
			this.packageScan = packageScan;
		}

		public String getMappingResources() {
			return mappingResources;
		}

		public void setMappingResources(String mappingResources) {
			this.mappingResources = mappingResources;
		}

		public Boolean getShow_sql() {
			return show_sql;
		}

		public void setShow_sql(Boolean show_sql) {
			this.show_sql = show_sql;
		}

		public Boolean getFormat_sql() {
			return format_sql;
		}

		public void setFormat_sql(Boolean format_sql) {
			this.format_sql = format_sql;
		}

		public Boolean getEnable_lazy_load_no_trans() {
			return enable_lazy_load_no_trans;
		}

		public void setEnable_lazy_load_no_trans(Boolean enable_lazy_load_no_trans) {
			this.enable_lazy_load_no_trans = enable_lazy_load_no_trans;
		}

	}

	public static class DataSourceInfo {
		private String driverClassName;
		private String url;
		private String username;
		private String password;
		private int maxActive = 4;
		private int maxIdle = 1;
		private int minIdle = 0;
		private int initialSize = 1;
		private int timeBetweenEvictionRunsMillis = 60000;
		private int numTestsPerEvictionRun = 100;
		private boolean logAbandoned = true;

		public String getDriverClassName() {
			return driverClassName;
		}

		public void setDriverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getMaxActive() {
			return maxActive;
		}

		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}

		public int getMaxIdle() {
			return maxIdle;
		}

		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}

		public int getMinIdle() {
			return minIdle;
		}

		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

		public int getInitialSize() {
			return initialSize;
		}

		public void setInitialSize(int initialSize) {
			this.initialSize = initialSize;
		}

		public int getTimeBetweenEvictionRunsMillis() {
			return timeBetweenEvictionRunsMillis;
		}

		public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
			this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		}

		public int getNumTestsPerEvictionRun() {
			return numTestsPerEvictionRun;
		}

		public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
			this.numTestsPerEvictionRun = numTestsPerEvictionRun;
		}

		public boolean isLogAbandoned() {
			return logAbandoned;
		}

		public void setLogAbandoned(boolean logAbandoned) {
			this.logAbandoned = logAbandoned;
		}

	}

}
