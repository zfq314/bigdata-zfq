1、初始flink
2、flink 快速上手
3、flink 部署
4、flink 运行时架构
5、flink DataStream API 基础篇
6、flink 中的时间和窗口
7、处理函数
8、多流转换
9、状态编程
10、容错机制
11、Table API和SQL

12、Flink CEP
总结起来，复杂事件处理（CEP）的流程可以分成三个步骤：
（1）定义一个匹配规则
（2）将匹配规则应用到事件流上，检测满足规则的复杂事件
（3）对检测到的复杂事件进行处理，得到结果进行输出

CEP主要用于实时流数据的分析处理。CEP可以帮助在复杂的、看似不相关的事件流中找出那些有意义的事件组合，进而可以接近实时地进行分析判断、输出通知信息或报警。
这在企业项目的风控管理、用户画像和运维监控中，都有非常重要的应用。
风险控制
设定一些行为模式，可以对用户的异常行为进行实时检测。
当一个用户行为符合了异常行为模式，比如短时间内频繁登录并失败、大量下单却不支付（刷单），就可以向用户发送通知信息，
或是进行报警提示、由人工进一步判定用户是否有违规操作的嫌疑。这样就可以有效地控制用户个人和平台的风险。

用户画像
利用CEP可以用预先定义好的规则，对用户的行为轨迹进行实时跟踪，从而检测出具有特定行为习惯的一些用户，做出相应的用户画像。
基于用户画像可以进行精准营销，即对行为匹配预定义规则的用户实时发送相应的营销推广；
这与目前很多企业所做的精准推荐原理是一样的。

运维监控
对于企业服务的运维管理，可以利用CEP灵活配置多指标、多依赖来实现更复杂的监控模式。
CEP的应用场景非常丰富。很多大数据框架，如Spark、Samza、Beam等都提供了不同的CEP解决方案，但没有专门的库（library）。
而Flink提供了专门的CEP库用于复杂事件处理，可以说是目前CEP的最佳解决方案。