package com.desai.demo.flink.iot;

import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class PeriodicTimestampAssigner extends BoundedOutOfOrdernessTimestampExtractor<SmartDeviceEvent> {
    private static final long serialVersionUID = 5701032997998338570L;

    public PeriodicTimestampAssigner() {
        super(Time.seconds(5));
    }

    //Grab timestamp from event.
    @Override
    public long extractTimestamp(SmartDeviceEvent event) {
        return event.getEventTimestamp();
    }

}
