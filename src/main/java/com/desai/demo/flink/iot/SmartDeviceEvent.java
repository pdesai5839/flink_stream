package com.desai.demo.flink.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmartDeviceEvent {
    private String deviceId;
    private long eventTimestamp;
    private double temperatureInFahrenheit;
}