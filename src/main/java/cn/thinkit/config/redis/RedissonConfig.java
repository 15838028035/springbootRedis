package cn.thinkit.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("redissonConfig")
public class RedissonConfig {

    @Autowired
    private RedisConfig rcfg;

    @Bean(destroyMethod = "shutdown" ,name="redissonClient")
    public RedissonClient redissonClient(){
        Config config = new Config();
        boolean redisIsCluster = rcfg.getRedisClusterNodes().contains(",");
        if(redisIsCluster){
            String[] splitequal = rcfg.getRedisClusterNodes().split(",");
            for (String spli : splitequal) {
                config.useClusterServers().addNodeAddress("redis://"+spli);
            }
            if(rcfg.getRedisPassword()!=null&&!rcfg.getRedisPassword().isEmpty()){
                config.useClusterServers().setPassword(rcfg.getRedisPassword());
            }
        }else {
            config.useSingleServer().setAddress("redis://"+rcfg.getRedisClusterNodes());
            if(rcfg.getRedisPassword()!=null&&!rcfg.getRedisPassword().isEmpty()){
                config.useSingleServer().setPassword(rcfg.getRedisPassword());
            }
        }
        return Redisson.create(config);
    }

}