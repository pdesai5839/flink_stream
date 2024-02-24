package com.desai.demo.flink.logsim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class LogSimSource implements SourceFunction<String> {
    private volatile boolean isRunning = true;
    private Random rand;

    public LogSimSource() {
        this.rand = new Random();
    }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (this.isRunning) {
            try {
                char username = 'a';
                username = (char) (username + new Random().nextInt(26));

                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = dateFormat.format(now);

                String log = String.format("%s\t%s", String.valueOf(username), time);

                ctx.collect(log);

                Thread.sleep(5);
            }
            catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
