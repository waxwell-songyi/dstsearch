package com.songyi.service.schedule;

import com.songyi.entity.ServerList;
import com.songyi.entity.enums.MyPlatform;
import com.songyi.entity.enums.Regions;
import com.songyi.service.KleiApiService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class TaskService {

    @Resource
    private KleiApiService kleiApiService;

    /**
     * 4个区域的
     */
    public static List<ServerList> resultList1 = new CopyOnWriteArrayList<>();
    public static List<ServerList> resultList2 = new CopyOnWriteArrayList<>();
    public static List<ServerList> resultList3 = new CopyOnWriteArrayList<>();
    public static List<ServerList> resultList4 = new CopyOnWriteArrayList<>();
    public static Random random = new Random();


    /**
     * 搜索全部服务器的定时任务，暂定为每分钟刷新一次缓存
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void searchAllServer() throws InterruptedException {
        System.out.println("开始刷新服务器信息.....");
        Thread.sleep(random.nextInt(2000) + 4000);
        try {
            dispose(resultList1, Regions.AP_EAST_1.getCode(), MyPlatform.RAIL.getCode());
            dispose(resultList2, Regions.AP_SOUTHEAST_1.getCode(), MyPlatform.RAIL.getCode());
            dispose(resultList3, Regions.EU_CENTRAL_1.getCode(), MyPlatform.RAIL.getCode());
            dispose(resultList4, Regions.US_EAST_1.getCode(), MyPlatform.RAIL.getCode());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("刷新服务器信息完成.....");
    }

    /**
     * 查询4个服务器的数据，并刷新缓存的操作
     *
     * @param resultList
     * @param code       区域
     * @param platform   平台
     * @throws IOException
     * @throws URISyntaxException
     */
    private void dispose(List<ServerList> resultList, String code, String platform) throws IOException, URISyntaxException {
        List<ServerList> refreshList = kleiApiService.searchServerList(code, platform);
        if (refreshList.size() > 0) {
            resultList.clear();
            refreshList = refreshList.stream().peek(s -> s.setRegion(code)).collect(Collectors.toList());
            resultList.addAll(refreshList);
        }
    }
}
