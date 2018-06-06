# spring-cloud-dataflow-demo

## Setup labs on localhost with docker
https://cloud.spring.io/spring-cloud-dataflow/#quick-start

1. Download docker-compose.yml
2. `DATAFLOW_VERSION=1.5.0.RELEASE docker-compose up` 
3. Full DATAFLOW_VERSION list at [Docker Hub](https://hub.docker.com/r/springcloud/spring-cloud-dataflow-server-local/tags/)

## Develop sample processor
### Create sample project
https://start.spring.io/
Group:  io.spring.stream.sample
Artifact:   processor-demo
Dependencies: Kafka, Cloud Stream

### Create ProcessorDemo.java

```
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
```

$ mvn clean package

## Copy the jar into dataflow-server container
`docker cp target/processor-demo-0.0.1-SNAPSHOT.jar dataflow-server:/tmp/processor-demo-0.0.1-SNAPSHOT.jar`

## Register app
http://localhost:9393/dashboard/#/apps, navigate to "ADD APPLICATION(S) > Register Application(s)"
- Name:   processor-demo
- Type:   Processor
- URI:    file://tmp/processor-demo-0.0.1-SNAPSHOT.jar

## Deploy stream with the app
http://localhost:9393/dashboard/#/streams/create
- Name:   time2log
- Definitions:    time | processor-demo | log

## Confirm
1. http://localhost:9393/dashboard/#/runtime/apps > time2log.log > stdout
2. `docker exec -it dataflow-server tail -f /path/stdout_0.log`