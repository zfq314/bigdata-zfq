package chapter02;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @ClassName BatchWordCount
 * @Description TODO
 * @Author ZFQ
 * @Date 2024/10/19 上午 11:00
 * @Version 1.0
 */
public class BatchWordCount {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //获取resources目录下的文件
        String path = BatchWordCount.class.getClassLoader().getResource("log4j.properties").getPath();
        //读取文件 按行读取
        DataSource<String> stringDataSource = env.readTextFile(path);
        //数据转换
        FlatMapOperator<String, Tuple2<String, Long>> flatMapOperator = stringDataSource.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] split = line.split("\\.");
            for (String s : split) {
                out.collect(Tuple2.of(s, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));//当Lambda表达式使用 Java 泛型的时候, 由于泛型擦除的存在, 需要显示的声明类型信息
        // 分组.聚合打印
        flatMapOperator.groupBy(0).sum(1).print();
    }
}