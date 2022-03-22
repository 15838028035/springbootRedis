package cn.thinkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class SpringbootRedisApplication {
	
    public static void main(String[] args) {
    	 SpringApplication.run(SpringbootRedisApplication.class, args);
    }
  
}
