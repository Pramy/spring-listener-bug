package com.pramyness.demo.listener.demo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        context.close();
    }

    class Listener implements DisposableBean {

        @EventListener(ContextClosedEvent.class)
        public void close(ApplicationEvent event) {
            System.out.println("---------------invoke listener"+event );
        }

        @Override
        public void destroy() throws Exception {
            System.out.println("--------------------listener destroy-----------------------");
        }
    }


    //1.insure NamedContextFactory initialize before listener
    //2.insure NamedContextFactory destroy after listener
    @Bean
    public Listener listener() {
        //3.initialize context values
        namedContextFactory().getInstance("demo",DemoApplication.class);
        return new Listener();
    }

    @Bean
    public NamedContextFactory<NamedContextFactory.Specification> namedContextFactory() {
        return new NamedContextFactory<NamedContextFactory.Specification>(Config.class
        ,"demo","demo") {};
    }

    @Configuration
    class Config{}
}
