package chapter02;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @ClassName StreamWordCount
 * @Description TODO
 * @Author ZFQ
 * @Date 2024/10/19 下午 02:15
 * @Version 1.0
 */
public class StreamWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        ParameterTool tools = ParameterTool.fromArgs(args);
        // 返回的结果不一样
        // nc -lk port
        // -host 添加host 地址 -port  指定port 添加运行参数
        String host = tools.get("host");
        int port = tools.getInt("port");

        // 按行读取
        DataStreamSource<String> inputStream = env.socketTextStream(host, port);


        SingleOutputStreamOperator<String> returns = inputStream
                .flatMap((String line, Collector<String> stringCollector) -> {
                    Arrays.stream(line.split(" "))
                            .forEach(stringCollector::collect);
                }).returns(Types.STRING);
        SingleOutputStreamOperator<Tuple2<String, Long>> outputStreamOperator = returns
                .map(words -> Tuple2.of(words, 1L)).returns(Types.TUPLE(Types.STRING, Types.LONG));

        KeyedStream<Tuple2<String, Long>, String> tuple2StringKeyedStream = outputStreamOperator
                .keyBy(t -> t.f0);
        tuple2StringKeyedStream.sum(1)
                .print();


        env.execute();
    }
}