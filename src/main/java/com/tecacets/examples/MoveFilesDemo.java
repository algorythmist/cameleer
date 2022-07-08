package com.tecacets.examples;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

class FileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String content = exchange.getIn().getBody(String.class);
        System.out.println(content);
        if (content.trim().equalsIgnoreCase("exception")) {
            throw new IllegalCallerException();
        }
        System.out.println("Processing..." + exchange.getExchangeId());
    }
}

class SimpleRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        onException(IllegalCallerException.class)
                .process(exchange -> System.out.println("Got an exception"));

        from("file:input") //Use file:input?noop=true to leave the file in place
                .process(new FileProcessor())
                .to("file:output");
    }

}


public class MoveFilesDemo {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        SimpleRouteBuilder routeBuilder = new SimpleRouteBuilder();
        context.addRoutes(routeBuilder);
        context.start();
        Thread.sleep(51000);
        context.stop();
    }
}