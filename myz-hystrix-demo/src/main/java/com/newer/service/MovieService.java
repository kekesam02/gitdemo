//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？
package com.newer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class MovieService {

    /*

     1:合并请求：前提必须是你的此接口和方法的并发要非常的大，才会又意义.（为什么？） `
     2: 怎么让请求能合并，合并放在什么地方--Queueu队列
     3：怎么做到每每隔一段时间就去-Queue去消费的请求

     4：数据如何返回？


     //nio bio 线程的开辟 new Thread() ---堆内容----new Thread().start()---线程 --栈--系统空间申请一块区域--来执行这线程任务---32--100kb
     http://wwww.cxx.com//query/movie?moviecode=234231 ---- nginx---tomcat8--开一个线程 htttp-nio-45345----
     http://wwww.cxx.com//query/movie?moviecode=234232 ---- nginx---tomcat8--开一个线程 htttp-nio-345345---
     http://wwww.cxx.com//query/movie?moviecode=234233 ---- nginx---tomcat8--开一个线程 htttp-nio-657567---
     */


    class Request{
        String movieCode;
        CompletableFuture<Map<String,Object>> future;
    }

    // 定义一个用来存储所有请求，或者积攒所有请求的队列(每隔N秒就批量执行一次)
    LinkedBlockingDeque<Request> queue = new LinkedBlockingDeque<>();

    @PostConstruct //  对象在调用方法之前执行的逻辑
    public void init(){
        //线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleAtFixedRate(()->{
            // 1:去queue请求，生成一次批量查询
            int size = queue.size();
            if(size==0){ // 如果queue没有请求直接返回
                return;
            }

            ArrayList<Request> requests = new ArrayList<>();
            for (int i=0;i<size;i++){
                Request request = queue.poll();// 从queue队列取出请求，开始消费poll取出，queue就删除了。
                requests.add(request);
            }

            // 查询所有数据
            //2: 组装一个批量查询的参数 List<String> movieCodes
           ArrayList<String> movieCodes = new ArrayList<>();
            for (Request request :requests){
                movieCodes.add(request.movieCode);
            }

            System.out.println("批量处理数据量是：" +size);

            //查询所有的数据
            List<Map<String,Object>> responses = movieRemoteService.queryMovieInfoByCodeBatch(movieCodes);



            //将结构响应给没个单独的用户请求，有定时任务处理线程
            HashMap<String,Map<String,Object>> responseMap = new HashMap<>();
            for (Map<String,Object> response :responses){
                String code = response.get("code").toString();
                responseMap.put("code",response);
            }

            for (Request request : requests){
                Map<String,Object> result = responseMap.get(request.movieCode);
                /// 将线程的结构返回到对应的请求线程上去
                request.future.complete(result);
            }

        },0,10, TimeUnit.MILLISECONDS);//0.01
    }


    @Autowired
    private MovieRemoteService movieRemoteService;

//    public Map<String,Object> queryMovie(String movieCode){
//        return movieRemoteService.queryMovieInfoByCode(movieCode);
//    }

    // MQ --rabbitmq activeMQ kafka future---阻塞的

    public Map<String,Object> queryMovie(String movieCode) throws  Exception{
       // 目标：1000 请求，怎么才能建成，更少调用呢额？
       // 思路： 讲不同用户同类请求合并起来
       Request request = new Request();
       request.movieCode = movieCode;
       // 如何让返回值，不会立即返回，而且阻塞---，什么时候去返回，通知//
       // juc
        // future/task机制，是开辟一个线程，会阻塞，会通过complement会来get（）；
        CompletableFuture<Map<String,Object>> future = new CompletableFuture<>();
        request.future = future;
        //将请求放入到队列中。
        queue.add(request);

        return future.get();//
      //return movieRemoteService.queryMovieInfoByCodeBatch(movieCode);
   }
}
