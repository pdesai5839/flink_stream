package com.desai.demo.flink.iot;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class SmartDeviceEventJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        //set watermark at 1 sec
        //watermark indicates that no more delayed events will arrive
        env.getConfig().setAutoWatermarkInterval(1000L);
        
        DataStream<SmartDeviceEvent> temperatureDataStream = env
            .addSource(new SmartDeviceEventSource())
            .assignTimestampsAndWatermarks(new PeriodicTimestampAssigner());
        

        DataStream<SmartDeviceEvent> averageTemperature = temperatureDataStream
            //partition by device id
            .keyBy((KeySelector<SmartDeviceEvent, String>) SmartDeviceEvent::getDeviceId)
            //group in 1 second tumbling windows
            .window(TumblingEventTimeWindows.of(Time.seconds(1)))
            .apply(new AverageTemperatureEmitter());
        
        averageTemperature.print();

        // execute application
        env.execute("Average temperature from devices");
    }
    
    //invoked for each window once
    public static class AverageTemperatureEmitter implements WindowFunction<SmartDeviceEvent, SmartDeviceEvent, String, TimeWindow> {
        private static final long serialVersionUID = 4013733487425693439L;

        @Override
        public void apply(String deviceId, TimeWindow window, Iterable<SmartDeviceEvent> input, Collector<SmartDeviceEvent> out) {
            int count = 0;
            double sum = 0.0D;
            for (SmartDeviceEvent event : input) {
                sum += event.getTemperatureInFahrenheit();
                count++;
            }
            double averageTemperature = sum / count;
            out.collect(new SmartDeviceEvent(deviceId, window.getEnd(), averageTemperature));
        }
    }
}
