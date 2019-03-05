package com.newer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.newer.service.MovieService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MzyHystrixDemoApplicationTests {

	long time = 0l;
	
	@Before
	public void start() {
		System.out.println("测试开始");
		time = System.currentTimeMillis();
	}
	
	@After
	public void end() {
		System.out.println("结束测试，执行是的时长是："+(System.currentTimeMillis() - time));
	}
	
	//模拟请求数量
	private static final int THREAD_NUM = 1000;
	
	//倒计数器，juc包中常用工具类
	private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
	
	@Autowired
	private MovieService movieService;
	
	@Test
	public void benchmark() throws IOException{
		HystrixRequestContext context = HystrixRequestContext.initializeContext();
		//创建并不是马上发起请求
		for (int i = 0; i < THREAD_NUM; i++) {
			//js
			final String code = "code-" + (i+1);
			// 多线程模拟用户查询请求
			Thread thread = new Thread(()-> {
				try {
					// 代码在这里等待，等待countDownLatch为0，代表所有的线程都start,在运行后续的代码.
					countDownLatch.await();
					// http请求，实际上就是多线程来调用这个方法
					Map<String,Object> result = movieService.queryMovie(code);
					// 打印查询的结果
					//System.out.println(Thread.currentThread().getName() +" 查询结束，结果是：" + result);
				} catch (Exception e) {
					System.out.println(Thread.currentThread().getName() + "执行线程出现异常" + e.getMessage());
				}
			});

			// redis 缓存 高并发，juc 多线程，锁，海量数据。源码。设计模式
			//  死锁。 合并 一致性hash nginx
			// 15-30K 优化 处理数据能力和学习态度
			thread.setName("price-thread-"+code);
			thread.start();
			//田径，启动后，倒计时器计数减去1，代表又有一个线程就绪了
			countDownLatch.countDown();
		}


		//下18-15-30
		// 输入任意内容退出
		System.in.read();
		context.shutdown();
	}
	
}
