package com.songyi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatApplication {
    public static void main(String[] args) {
        System.out.println("项目启动中。。。");
        SpringApplication.run(ChatApplication.class,args);
        System.out.println("项目启动成功。。。");
    }
}
