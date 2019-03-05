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

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieRemoteService {

    //dubbo rpc grpc 随便你用什么方式，httpclient

    /***
     *  模拟远程接口调用数据
     * @param movieCode
     * @return
     */
    public Map<String,Object> queryMovieInfoByCode(String movieCode){
        Map<String,Object> map = new HashMap<>();
        map.put("movieId",new Random().nextInt(1000000000));
        map.put("code",movieCode);
        map.put("star","asher");
        map.put("isHadom",true);
        return map;
    }

    // 批处理查询对应数据
    public List<Map<String,Object>> queryMovieInfoByCodeBatch(List<String> movieCodes) {
        List<Map<String, Object>> lists = new ArrayList<>();
        for (String movieCode : movieCodes) {
            Map<String, Object> map = new HashMap<>();
            map.put("movieId", new Random().nextInt(1000000000));
            map.put("code", movieCode);
            map.put("star", "asher");
            map.put("isHadom", true);
            lists.add(map);
        }
        return lists;
    }

}
