package cn.emay.configuration.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xpack.sql.jdbc.EsDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * elastic search client
 *
 * @author frank
 */
@Configuration
@ConfigurationProperties(prefix = "es")
@Order(0)
public class ElasticSearchConfiguration {

    private String ip;

    private int port;

    private String user;

    private String password;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticSearchClient() {
        RestHighLevelClient client;
        if (user != null && password != null) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
            client = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(ip, port, "http")).setHttpClientConfigCallback(httpClientBuilder -> {
                        httpClientBuilder.disableAuthCaching();
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }));
        } else {
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, "http")));
        }
        return client;
    }

    @Bean(name = "EsDataSource")
    public DataSource esDataSource() {
        EsDataSource datasource = new EsDataSource();
        datasource.setUrl("jdbc:es://http://" + ip + ":" + port);
        if (user != null && password != null) {
            Properties pro = new Properties();
            pro.setProperty("user", user);
            pro.setProperty("password", password);
            datasource.setProperties(pro);
        }
        return datasource;
    }

    @Bean(name = "EsJdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(esDataSource());
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
