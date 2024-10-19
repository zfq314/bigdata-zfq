package chapter02;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @ClassName BoundedStreamWordCount
 * @Description TODO
 * @Author ZFQ
 * @Date 2024/10/19 下午 01:57
 * @Version 1.0
 */
public class BoundedStreamWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        String input = BoundedStreamWordCount.class.getClassLoader().getResource("log4j.properties").getPath();

        DataStreamSource<String> inputSteam = env.readTextFile(input);

        SingleOutputStreamOperator<Tuple2<String, Long>> returns = inputSteam.flatMap((String line, Collector<String> out) ->
        {
            Arrays.stream(line.split("\\.")).forEach(out::collect);
        }).returns(Types.STRING)
                .map(word -> Tuple2.of(word, 1L))
                .returns(Types.TUPLE(Types.STRING, Types.LONG));
        KeyedStream<Tuple2<String, Long>, String> tuple2StringKeyedStream = returns
                .keyBy(t -> t.f0);
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = tuple2StringKeyedStream
                .sum(1);
        sum
                .print();


        env.execute();
    }
}