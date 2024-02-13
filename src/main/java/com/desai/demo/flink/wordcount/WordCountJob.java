package com.desai.demo.flink.wordcount;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class WordCountJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(ParameterTool.fromArgs(args));

        DataStream<String> dataStream = env.fromElements(WORDS);
        dataStream
            // call flatMap for each element of the data stream
            .flatMap(new TextLineSplitter())
            // partition the data stream by position 0 (the token)
            .keyBy(0)
            // apply transformation on grouped data stream
            .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                @Override
                public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                    return new Tuple2<>(value1.f0, value1.f1 + value2.f1);
                }
            })
            // trigger the actual execution
            .print();

        env.execute("Word Count Streaming Demo");
    }
    
    private static final String[] WORDS = new String[]{
        "For all you sucker MC's perpetratin' a fraud",
        "Your rhymes are cold wack and keep the crowd cold lost",
        "You're the kind of guy that girl ignored",
        "I'm drivin' Caddy, you fixin' a Ford",
        "My name is Joseph Simmons but my middle name's Ward",
        "And when I'm rockin' on the mic, you should all applaud",
        "Because we're wheelin, dealin, we got a funny feelin'",
        "We rock from the floor up to the ceilin'",
        "We groove it, you move it, it has been proven",
        "We calm the savage beast because our music is soothin'",
        "We create it, relate it, and often demonstrate it",
        "We'll diss a sucker MC, make the other suckers hate it",
        "We're rising, surprising, and often hypnotizing",
        "We always tell the truth and then we never slip no lies in",
        "No curls, no braids, peasy-head and still get paid",
        "Jam Master cut the record up and down and cross-fades"
    };
}
