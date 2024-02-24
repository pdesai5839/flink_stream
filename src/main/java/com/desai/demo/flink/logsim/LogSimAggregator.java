package com.desai.demo.flink.logsim;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;

public class LogSimAggregator implements AggregateFunction<Tuple2<String, Long>, Tuple2<String, Integer>, Tuple2<String, Integer>> {

    @Override
    public Tuple2<String, Integer> createAccumulator() {
        return new Tuple2<>("", 0);
    }

    @Override
    public Tuple2<String, Integer> add(Tuple2<String, Long> value, Tuple2<String, Integer> accumulator) {
        accumulator.f0 = value.f0;
        accumulator.f1 += 1;
        return accumulator;
    }

    @Override
    public Tuple2<String, Integer> getResult(Tuple2<String, Integer> accumulator) {
        return accumulator;
    }

    @Override
    public Tuple2<String, Integer> merge(Tuple2<String, Integer> a, Tuple2<String, Integer> b) {
        Tuple2<String, Integer> merged = new Tuple2<String, Integer>();
        merged.f0 = a.f0;
        merged.f1 = a.f1 + b.f1;
        return merged;
    }
}
