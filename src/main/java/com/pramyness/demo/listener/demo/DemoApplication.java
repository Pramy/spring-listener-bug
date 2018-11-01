package com.pramyness.demo.listener.demo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;

@EnableAspectJAutoProxy
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        context.close();
    }

    class Listener implements DisposableBean {

        @EventListener
        public void close(ApplicationEvent event) {
            System.out.println("---------------invoke listener"+event );
        }

        @Override
        public void destroy() throws Exception {
            System.out.println("--------------------listener destroy-----------------------");
        }
    }


    //1.insure FeignContext initialize before listener
    //2.insure FeignContext destroy after listener
    @Bean
    public Listener listener(FeignContext context) {
        //3.initialize client
        context.getInstance("BASE-SERVICE",Client.class);
        return new Listener();
    }

//    class Listener implements ApplicationListener<ContextClosedEvent> {
//
//        @Override
//        public void onApplicationEvent(@NonNull ContextClosedEvent event) {
//            System.out.println("----------------------close");
//        }
//    }

    @FeignClient("BASE-SERVICE")
    interface Client {}
}
