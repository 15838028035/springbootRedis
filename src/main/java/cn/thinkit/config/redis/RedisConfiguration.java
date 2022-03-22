package cn.thinkit.config.redis;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;

@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisConfig redisCfg;

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        // 连接池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisCfg.getRedisLettucePoolMaxIdle());
        poolConfig.setMinIdle(redisCfg.getRedisLettucePoolMinIdle());
        poolConfig.setMaxTotal(redisCfg.getRedisLettucePoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisCfg.getRedisLettucePoolMaxWait());

        String redisCluster = redisCfg.getRedisClusterNodes();
        if(!redisCluster.contains(",")){
            // 单机redis
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(redisCluster.split(":")[0]);
            redisConfig.setPort(Integer.parseInt(redisCluster.split(":")[1]));
            if(redisCfg.getRedisPassword()!=null&&!redisCfg.getRedisPassword().isEmpty()){
                    redisConfig.setPassword(redisCfg.getRedisPassword());
            }
            LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                    .poolConfig(poolConfig)
                    .build();
            return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
        }else {

            // 集群redis
            RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
            Set<RedisNode> nodeses = new HashSet<>();
            String[] hostses = redisCluster.split(",");
            for (String h : hostses) {
                h = h.replace("\\s", "").replace("\n", "");
                if (!"".equals(h)) {
                    String host = h.split(":")[0];
                    int port = Integer.valueOf(h.split(":")[1]);
                    nodeses.add(new RedisNode(host, port));
                }
            }
            redisConfig.setClusterNodes(nodeses);
            // 跨集群执行命令时要遵循的最大重定向数量
            redisConfig.setMaxRedirects(3);
            if(redisCfg.getRedisPassword()!=null&&!redisCfg.getRedisPassword().isEmpty()){
                redisConfig.setPassword(redisCfg.getRedisPassword());
            }

            // 支持自适应集群拓扑刷新和动态刷新源
            ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                    //开启自适应刷新
                    //.enableAdaptiveRefreshTrigger(ClusterTopologyRefreshOptions.RefreshTrigger.MOVED_REDIRECT, ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
                    //开启所有自适应刷新，MOVED，ASK，PERSISTENT都会触发
                    .enableAllAdaptiveRefreshTriggers()
                    // 自适应刷新超时时间(默认30秒)
                    .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(25)) //默认关闭开启后时间为30秒
                    // 开周期刷新
                    .enablePeriodicRefresh(Duration.ofSeconds(20))  // 默认关闭开启后时间为60秒 ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD 60  .enablePeriodicRefresh(Duration.ofSeconds(2)) = .enablePeriodicRefresh().refreshPeriod(Duration.ofSeconds(2))
                    .build();

            ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                    .topologyRefreshOptions(clusterTopologyRefreshOptions).build();

            LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                    .poolConfig(poolConfig)
                    .clientOptions(clusterClientOptions)
                    .build();
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
            lettuceConnectionFactory.setShareNativeConnection(false);// 是否允许多个线程操作共用同一个缓存连接，默认 true，false 时每个操作都将开辟新的连接
            lettuceConnectionFactory.resetConnection();// 重置底层共享连接, 在接下来的访问时初始化
            return lettuceConnectionFactory;

        }

    }


    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(stringSerializer);// value序列化
        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(stringSerializer);// Hash value序列化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}