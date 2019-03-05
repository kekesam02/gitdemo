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
package com.newer.web;

import com.newer.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;



    // 用户请求的查询电影的地址
    // nginx + 收集很多这样的请求地址：
    //http://wwww.cxx.com//query/movie?moviecode=234231
    //http://wwww.cxx.com//query/movie?moviecode=234232
    //http://wwww.cxx.com//query/movie?moviecode=234233
    //http://wwww.cxx.com//query/movie?moviecode=234234
    //http://wwww.cxx.com//query/movie?moviecode=234235
    //http://wwww.cxx.com//query/movie?moviecode=234236

//    @GetMapping("/query/movie")
//    public Map<String,Object> queryMovie(String movieCode){
//        return movieService.queryMovie(movieCode);
//    }


}
