package com.songyi.controller;

import com.alibaba.fastjson.JSONObject;
import com.songyi.entity.ServerList;
import com.songyi.entity.enums.Roles;
import com.songyi.entity.enums.Season;
import com.songyi.entity.enums.Style;
import com.songyi.service.schedule.TaskService;
import com.songyi.service.KleiApiService;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


@RestController
@RequestMapping("/api/test")
public class TestMessage {

    public static Map<String, List<ServerList>> detail = new HashMap<>();

    @Resource
    private KleiApiService kleiApiService;

    /**
     * 根据服务器的name字段进行模糊搜索
     */
    @GetMapping("/search")
    public String search(String qid, String keywords) {

        if (StringUtils.isEmpty(qid)) {
            return "";
        }
        List<ServerList> pidList = new CopyOnWriteArrayList<>();
        detail.put(qid, pidList);
        //进行服务器数据的遍历，然后进行返回的操作
        //返回的文本信息
        StringBuilder resultMessage = new StringBuilder();
        //关键词不存在，进行数据全部搜索（显示前10条数据，然后展示总数，有多少页，当前第几页）
        int size = TaskService.resultList1.size() +
                TaskService.resultList2.size() +
                TaskService.resultList3.size() +
                TaskService.resultList4.size();
        if (size == 0) {
            return "未查询到结果";
        }
        //查询所有服务器时的分支
        if (StringUtils.isBlank(keywords)) {
            resultMessage.append("当前是第1页 一共")
                    .append((int) Math.ceil((double) (size) / 10))
                    .append("页\n");
            processList(resultMessage, TaskService.resultList1);
            for (ServerList serverList : TaskService.resultList1) {
                if (pidList.size() < 9) {
                    pidList.add(serverList);
                }
            }
            return resultMessage.toString();
        }
        //参数过长，返回失败
        if (keywords.length() > 30) {
            return "房间名过长查询失败";
        }
        //命中的服务器列表数量
        Integer count = 0;
        try {
            count = matching(count, keywords, TaskService.resultList1, pidList);
            count = matching(count, keywords, TaskService.resultList2, pidList);
            count = matching(count, keywords, TaskService.resultList3, pidList);
            count = matching(count, keywords, TaskService.resultList4, pidList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //处理查询到的信息conut, resultMessage
        if (pidList.size() == 0) {
            return "未查询到房间信息";
        }
        resultMessage.append("当前是第1页 一共").append((int) Math.ceil((double) count / 10)).append("页\n");
        processList(resultMessage, pidList);
        return resultMessage.toString();
    }

    /**
     * 查询某一个详细系统的信息,每位玩家对应一个结果集（2min保存）
     */
    @GetMapping("/detail")
    public String findDetailedInfo(String qid, Integer number) {
        if (StringUtils.isBlank(qid) || Objects.isNull(number) || number <= 0 || number > 10) {
            return "";
        }
        List<ServerList> serverLists = detail.get(qid);
        if (ObjectUtils.isEmpty(serverLists)) {
            return "";
        }
        StringBuilder resultMessage = new StringBuilder();
        for (int i = 0; i < serverLists.size(); i++) {
            if (number == i + 1) {
                ServerList serverList = serverLists.get(i);
                processDetail(resultMessage, serverList);
            }
        }
        return resultMessage.toString();
    }


    /**
     * 处理查服粗略结果方法
     */
    private void processList(StringBuilder resultMessage, List<ServerList> pidList) {
        for (int i = 0; i < pidList.size(); i++) {
            ServerList serverList = pidList.get(i);
            if (i > 8) break;
            resultMessage.append(i + 1).append(".").append(serverList.getName()).append("(").append(serverList.getConnected()).append("/").append(serverList.getMaxconnections()).append(")\n");
        }
    }

    /**
     * 处理详细的结果的方法
     */
    private String processDetail(StringBuilder resultMessage, ServerList serverList) {
        String rowId = serverList.getRowId();
        String region = serverList.getRegion();
        ServerList detail = kleiApiService.detailMessage(region, rowId);
        if (ObjectUtils.isEmpty(detail)) {
            return "查询失败";
        }
        //封装最后的返回结果
        String day = detail.getData();//天数信息
        Map<String, Object> days = complement(day.replace("return", "")).get(0);
        Integer selapsed = (Integer) days.get("dayselapsedinseason");
        Integer left = (Integer) days.get("daysleftinseason");
        String season = Season.getNameByCode(detail.getSeason());
        season = season + "(" + (selapsed + 1) + "/" + (selapsed + left) + ")";
        List<Map<String, Object>> playersList = transform(detail.getPlayers().replace("return", ""));
        StringBuilder playResult = new StringBuilder();
        if (!ObjectUtils.isEmpty(playersList)) {
            for (int i = 0; i < playersList.size(); i++) {
                if (i < 10) {
                    playResult.append(playersList.get(i).get("name")).append("(").append(Roles.getNameByCode((String) playersList.get(i).get("prefab"))).append(")");
                } else {
                    break;
                }
                if (i < playersList.size() - 1 && i < 9) {
                    playResult.append(",");
                }
            }
        }else {
            playResult.append("无");
        }
        String desc = detail.getDesc();
        if (StringUtils.isBlank(desc)) {
            desc = "无";
        }
        resultMessage.append(detail.getName())
                .append("(")
                .append(serverList.getConnected())
                .append("/")
                .append(serverList.getMaxconnections())
                .append(")\n")
                .append("模式: ").append(Style.getNameByCode(detail.getMode())).append("/").append(Style.getNameByCode(detail.getIntent())).append("\n")
                .append("天数: 第").append(days.get("day")).append("天 ").append(season).append("\n")
                .append("描述: ").append(desc).append("\n")
                .append("玩家: ").append(playResult);
        return resultMessage.toString();
    }


    /**
     * 遍历匹配服务器缓存列表的方法
     */
    private Integer matching(Integer count, String keywords, List<ServerList> resultList, List<ServerList> qidList) {
        for (ServerList serverList : resultList) {
            //匹配命中
            if (serverList.getName().contains(keywords)) {
                if (count++ >= 9) {
                    continue;
                }
                //记录符合要求的服务器信息（只会保存前9个）
                qidList.add(serverList);
            }
        }
        return count;
    }

    /**
     * 处理data和players字段的转换方法
     */
    private List<Map<String, Object>> transform(String responseStr) {
        if (StringUtils.isBlank(responseStr)){
            return null;
        }
        responseStr = responseStr.substring(responseStr.indexOf("{") + "{".length());
        responseStr = responseStr.substring(0, responseStr.lastIndexOf("}"));
        return complement(responseStr);
    }

    /**
     * 解析的公用方法
     */
    private List<Map<String, Object>> complement(String responseStr) {
        if (StringUtils.isBlank(responseStr)){
            return null;
        }
        responseStr = responseStr.replace(" ", "");
        responseStr = responseStr.replace("\n", "");
        responseStr = responseStr.replace("},", "}},");
        String[] split = responseStr.split("},");
        List<Map<String, Object>> result = new ArrayList<>();
        for (String string : split) {
            string = string.replace(":", "");
            string = string.replace("{", "{\"");
            string = string.replace("=", "\":");
            string = string.replace(",", ",\"");

            result.add(JSONObject.parseObject(string, Map.class));
        }
        return result;
    }
}
