package pojo.logic;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import pojo.LoginEvent;

import java.util.List;
import java.util.Map;

/**
 * @ClassName LoginFailDetect
 * @Description TODO 逻辑处理层,模拟登录失败发出报警信息
 * @Author ZFQ
 * @Date 2024/10/10 下午 02:27
 * @Version 1.0
 */
public class LoginFailDetect {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 获取登录事件流，并提取时间戳、生成水位线
        KeyedStream<LoginEvent, String> stream = env.fromElements(
                new LoginEvent("user_1", "192.168.0.1", "success", 2000L),
                new LoginEvent("user_1", "192.168.0.2", "fail", 3000L),
                new LoginEvent("user_2", "192.168.1.29", "success", 4000L),
                new LoginEvent("user_1", "171.56.23.10", "fail", 5000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 6000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 7000L),
                new LoginEvent("user_2", "192.168.1.29", "fail", 8000L)
        ).assignTimestampsAndWatermarks(
                WatermarkStrategy.<LoginEvent>forMonotonousTimestamps()
                        .withTimestampAssigner(
                                new SerializableTimestampAssigner<LoginEvent>() {
                                    @Override
                                    public long extractTimestamp(LoginEvent loginEvent, long l) {
                                        return loginEvent.timestamp;
                                    }
                                }
                        )
        ).keyBy(r -> r.userId);
        // 1. 定义一个匹配模式
        //定义Pattern 连续三个登录失败事件
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("first")//以第一个登录失败时间开始
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                }).next("second") //第二个登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                }).next("third")//第三个登录失败事件
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equals("fail");
                    }
                });
        // 2. 将Pattern应用到流上，检测匹配的复杂事件，得到一个PatternStream
        PatternStream<LoginEvent> patternStream = CEP.pattern(stream, pattern);

        patternStream.select(new PatternSelectFunction<LoginEvent, String>() {
            @Override
            public String select(Map<String, List<LoginEvent>> map) throws Exception {
                LoginEvent first = map.get("first").get(0);
                LoginEvent second = map.get("second").get(0);
                LoginEvent third = map.get("third").get(0);
                return first.userId + " 连续三次登录失败！登录时间：" + first.timestamp + ", " + second.timestamp + ", " + third.timestamp;
            }
        }).print("warning");

        env.execute();
    }
}