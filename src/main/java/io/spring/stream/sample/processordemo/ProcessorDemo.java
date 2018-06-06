package io.spring.stream.sample.processordemo;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;

@EnableBinding(Processor.class)
public class ProcessorDemo {

    @StreamListener(Processor.INPUT)
    @Output(Processor.OUTPUT)
    public String transform(@Payload String intputStr) {
        // replace space with return line
        String outputStr = intputStr.replaceAll(" ", "\r\n");
        return outputStr;
    }
}