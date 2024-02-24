package com.desai.demo.flink.logsim;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class LogSimStreamJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        LogSimSource source = new LogSimSource();
        DataStream<String> sourceStream = env.addSource(source);

        DataStream<Tuple2<String, Long>> tupleStream = sourceStream.flatMap(new LogSimMapper());

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStream<Tuple2<String, Long>> timedStream = 
            tupleStream.assignTimestampsAndWatermarks(new LogSimAssigner());

        KeyedStream<Tuple2<String, Long>, Tuple> keyedStream = timedStream.keyBy(0);

        WindowedStream<Tuple2<String, Long>, Tuple, TimeWindow> windowStream =
            keyedStream.window(TumblingEventTimeWindows.of(Time.seconds(5)));

        DataStream<Tuple2<String, Integer>> aggStream = windowStream.aggregate(new LogSimAggregator());

        aggStream.addSink(new SinkFunction<Tuple2<String, Integer>>() {
            @Override
            public void invoke(Tuple2<String, Integer> value, Context context) throws Exception {
                System.out.println(value);
            }
        });

        env.execute("FakeLogStreamJob");
    }
}
