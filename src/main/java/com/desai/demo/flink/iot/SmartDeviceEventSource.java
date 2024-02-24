package com.desai.demo.flink.iot;

import java.time.Instant;
import java.util.Random;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

//emulate 5 devices sending an event every 100ms
public class SmartDeviceEventSource extends RichParallelSourceFunction<SmartDeviceEvent> {
    private static final long serialVersionUID = 2798986034985155837L;
    private volatile boolean isRunning = true;
    private int numberOfDevices = 5;
    private Random random = new Random(System.currentTimeMillis());
    
    @Override
    public void run(SourceContext<SmartDeviceEvent> srcCtx) throws Exception {
        while (isRunning) {
            for (int i = 0; i < numberOfDevices; i++) {
                String deviceId = "DEVICE-" + random.nextInt(numberOfDevices);
                long timeStampMillis = Instant.now().toEpochMilli();
                double randomTemp = 65 + (random.nextDouble() * 10);
                SmartDeviceEvent deviceEvent = SmartDeviceEvent.builder()
                    .deviceId(deviceId)
                    .eventTimestamp(timeStampMillis)
                    .temperatureInFahrenheit(randomTemp)
                    .build();
                srcCtx.collect(deviceEvent);
            }
            Thread.sleep(100);
        }
    }
    
    @Override
    public void cancel() {
        isRunning = false;
    }
}
