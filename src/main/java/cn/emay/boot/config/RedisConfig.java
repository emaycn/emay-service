package cn.emay.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import cn.emay.redis.RedisClient;
import cn.emay.redis.impl.RedisClusterClient;
import cn.emay.redis.impl.RedisShardedClient;
import cn.emay.redis.impl.RedisSingleClient;

/**
 * Redis组件配置
 * 
 * @author Frank
 *
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
@Order(0)
public class RedisConfig {

	/**
	 * 单点Redis参数
	 */
	private SingleInfo single;
	/**
	 * 分片Redis参数
	 */
	private ShardedInfo sharded;
	/**
	 * 集群Redis参数
	 */
	private clusterInfo cluster;
	/**
	 * 超时时间
	 */
	private int timeoutMillis;
	/**
	 * 最大空闲线程数
	 */
	private int maxIdle;
	/**
	 * 最大线程数
	 */
	private int maxTotal;
	/**
	 * 最小空闲线程数
	 */
	private int minIdle;
	/**
	 * 最大等待时间
	 */
	private long maxWaitMillis;
	/**
	 * 日期格式
	 */
	private String datePattern;

	@Bean(name = "RedisClient")
	public RedisClient redisClient() {
		RedisClient redis = null;
		if (single != null) {
			redis = new RedisSingleClient(single.getHost(), single.getPort(), timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern);
		} else if (sharded != null) {
			String hosts = "";
			for (String host : sharded.getHosts()) {
				hosts += host + ",";
			}
			hosts = hosts.substring(0, hosts.length() - 1);
			redis = new RedisShardedClient(hosts, timeoutMillis, maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern);
		} else if (cluster != null) {
			String hosts = "";
			for (String host : cluster.getHosts()) {
				hosts += host + ",";
			}
			hosts = hosts.substring(0, hosts.length() - 1);
			redis = new RedisClusterClient(hosts, timeoutMillis, cluster.getMaxRedirections(), maxIdle, maxTotal, minIdle, maxWaitMillis, datePattern);
		}
		return redis;
	}

	public SingleInfo getSingle() {
		return single;
	}

	public void setSingle(SingleInfo single) {
		this.single = single;
	}

	public ShardedInfo getSharded() {
		return sharded;
	}

	public void setSharded(ShardedInfo sharded) {
		this.sharded = sharded;
	}

	public clusterInfo getCluster() {
		return cluster;
	}

	public void setCluster(clusterInfo cluster) {
		this.cluster = cluster;
	}

	public int getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	/**
	 * 集群参数
	 * 
	 * @author Frank
	 *
	 */
	public static class clusterInfo {

		/**
		 * 节点地址
		 */
		private String[] hosts;

		/**
		 * 最大寻址次数
		 */
		int maxRedirections = 6;

		public String[] getHosts() {
			return hosts;
		}

		public void setHosts(String[] hosts) {
			this.hosts = hosts;
		}

		public int getMaxRedirections() {
			return maxRedirections;
		}

		public void setMaxRedirections(int maxRedirections) {
			this.maxRedirections = maxRedirections;
		}

	}

	/**
	 * 切片参数
	 * 
	 * @author Frank
	 *
	 */
	public static class ShardedInfo {

		/**
		 * 节点地址
		 */
		private String[] hosts;

		public String[] getHosts() {
			return hosts;
		}

		public void setHosts(String[] hosts) {
			this.hosts = hosts;
		}
	}

	/**
	 * 单点参数
	 * 
	 * @author Frank
	 *
	 */
	public static class SingleInfo {

		/**
		 * 地址
		 */
		private String host;

		/**
		 * 端口
		 */
		private int port;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}

}
