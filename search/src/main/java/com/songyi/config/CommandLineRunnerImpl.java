package com.songyi.config;

import com.songyi.service.schedule.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Resource
    TaskService taskService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("启动项目后初始化服务器列表缓存中.....");
        taskService.searchAllServer();
        System.out.println("启动项目后初始化服务器列表缓存完成");
    }

}