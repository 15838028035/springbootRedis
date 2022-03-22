package cn.thinkit.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* @Title: RedisConfig.java
* @Description: redis config
 */
@Component
public class RedisConfig {

	/**
	 * 单机或集群节点
	 */
	@Value("${redis.cluster.nodes}")
	private String redisClusterNodes;
	
	/**
	 * 单机或集群密码
	 */
	@Value("${redis.password}")
	private String redisPassword;
	
	/**
	 * 关闭超时时间
	 */
	@Value("${redis.lettuce.shutdown-timeout:100}")
	private int  redisLettuceShutdownTimeout;
	
	/**
	 * 连接池最大连接数（使用负值表示没有限制）
	 */
	@Value("${redis.lettuce.pool.max-active:8}")
	private int  redisLettucePoolMaxActive;
	
	/**
	 * 连接池中的最大空闲连接
	 */
	@Value("${redis.lettuce.pool.max-idle:8}")
	private int  redisLettucePoolMaxIdle;
	
	/**
	 * 连接池最大阻塞等待时间（使用负值表示没有限制）
	 */
	@Value("${redis.lettuce.pool.max-wait:30}")
	private int  redisLettucePoolMaxWait;
	
	/**
	 * 连接池中的最小空闲连接
	 */
	@Value("${redis.lettuce.pool.min-idle:0}")
	private int  redisLettucePoolMinIdle;
	
	/**
	 * 最大连接数量
	 */
	@Value("${redis.lettuce.pool.maxTotal:8}")
	private int  redisLettucePoolMaxTotal;

	public String getRedisClusterNodes() {
		return redisClusterNodes;
	}

	public void setRedisClusterNodes(String redisClusterNodes) {
		this.redisClusterNodes = redisClusterNodes;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	public int getRedisLettuceShutdownTimeout() {
		return redisLettuceShutdownTimeout;
	}

	public void setRedisLettuceShutdownTimeout(int redisLettuceShutdownTimeout) {
		this.redisLettuceShutdownTimeout = redisLettuceShutdownTimeout;
	}

	public int getRedisLettucePoolMaxActive() {
		return redisLettucePoolMaxActive;
	}

	public void setRedisLettucePoolMaxActive(int redisLettucePoolMaxActive) {
		this.redisLettucePoolMaxActive = redisLettucePoolMaxActive;
	}

	public int getRedisLettucePoolMaxIdle() {
		return redisLettucePoolMaxIdle;
	}

	public void setRedisLettucePoolMaxIdle(int redisLettucePoolMaxIdle) {
		this.redisLettucePoolMaxIdle = redisLettucePoolMaxIdle;
	}

	public int getRedisLettucePoolMaxWait() {
		return redisLettucePoolMaxWait;
	}

	public void setRedisLettucePoolMaxWait(int redisLettucePoolMaxWait) {
		this.redisLettucePoolMaxWait = redisLettucePoolMaxWait;
	}

	public int getRedisLettucePoolMinIdle() {
		return redisLettucePoolMinIdle;
	}

	public void setRedisLettucePoolMinIdle(int redisLettucePoolMinIdle) {
		this.redisLettucePoolMinIdle = redisLettucePoolMinIdle;
	}

	public int getRedisLettucePoolMaxTotal() {
		return redisLettucePoolMaxTotal;
	}

	public void setRedisLettucePoolMaxTotal(int redisLettucePoolMaxTotal) {
		this.redisLettucePoolMaxTotal = redisLettucePoolMaxTotal;
	}
}
