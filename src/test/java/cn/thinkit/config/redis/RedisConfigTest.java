package cn.thinkit.config.redis;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import cn.thinkit.util.thread.ThreadPoolMonitor;
import jdk.nashorn.internal.ir.annotations.Ignore;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {


	private static Logger logger = LoggerFactory.getLogger(RedisConfigTest.class);
	
 	@Autowired
    private RedisTemplate redisTemplate;
 	
 	@Autowired
	private RedissonClient redissonClient;
 	
 	public static   AtomicInteger productCount =  new  AtomicInteger(1000);
    /**
     * 线程池
     */
    private static  ExecutorService executorService = ThreadPoolMonitor.threadPoolMonitor(Runtime.getRuntime().availableProcessors()*2, "buyProduct"); 
 	
 	/**
 	 * 测试set key 方法
 	 */
	@Test
	@Ignore
	public void setKeyTest	() {
		redisTemplate.opsForValue().set("testKey", "testa");
		String value = (String)redisTemplate.opsForValue().get("testKey");
		assertEquals("testa", value);
	}
	
	/**
 	 * 测试set list  key 方法
 	 */
	@Test
	@Ignore
	public void setListTest	() {
		redisTemplate.opsForList().rightPush("LIST", "testa");
		redisTemplate.opsForList().rightPush("LIST", "testb");
		redisTemplate.opsForList().rightPush("LIST", "testc");
		redisTemplate.opsForList().rightPush("LIST", "testd");
	}
	
	/**
 	 * 测试lock方法
 	 */
	public void lockTest	() {
		 //1、获取一把锁，只要锁的名字一样，就是同一把锁
		RLock lock = redissonClient.getLock("my-lock");
        try{
        	boolean b = lock.tryLock(5,2, TimeUnit.SECONDS);
        	
        	if(b) {
        		System.out.println("lock successfully");
        		System.out.println("加锁成功，执行业务..." + Thread.currentThread().getId());
        		
        		int a = productCount.decrementAndGet();
        		
        		logger.info("a===" + a);
        		
              //  Thread.sleep(1000);
                
        	}else {
        		logger.info(" lock fail ");
        	}
          
        } catch (Exception e) {

        	e.printStackTrace();
        } finally {
        	//3、解锁将设解锁代码没有运行，redisson会不会出现死锁
        	logger.info("释放锁..." + Thread.currentThread().getId());
//            boolean isLock = lock.readLock().isLocked();
//            System.out.println("isLock:" + isLock);
            
            if(lock.isLocked()&&lock.isHeldByCurrentThread()){
            	lock.unlock();
            }
        }
        
        logger.info("productCount================" + productCount);
	}
	
	@Test
	@Order(999)
	public void productBuyThreadTest() {
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<1000 ;i++) {
			executorService.execute(() -> {
				lockTest();
			});
		
		}
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
