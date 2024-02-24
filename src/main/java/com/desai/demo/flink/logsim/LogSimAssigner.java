package com.desai.demo.flink.logsim;

import javax.annotation.Nullable;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public class LogSimAssigner implements AssignerWithPunctuatedWatermarks<Tuple2<String, Long>> {

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(Tuple2<String, Long> lastElement, long extractedTimestamp) {
        return new Watermark(extractedTimestamp - 2000);
    }

    @Override
    public long extractTimestamp(Tuple2<String, Long> element, long previousElementTimestamp) {
        return element.f1;
    }
}
