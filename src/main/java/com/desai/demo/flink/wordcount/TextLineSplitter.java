package com.desai.demo.flink.wordcount;

import java.util.stream.Stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class TextLineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

    @Override
    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
        // split the input into individual words
        Stream.of(value.toLowerCase().split("\\W+"))
        // leave out empty/blank words
        .filter(t -> t.length() > 0)
        // for each word, collect and create a tuple
        // note that calling collect() is a sink operation that triggers the actual data transformation 
        .forEach(token -> out.collect(new Tuple2<>(token, 1)));
    }
}
