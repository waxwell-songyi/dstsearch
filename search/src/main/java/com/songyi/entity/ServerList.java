package com.songyi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.songyi.entity.enums.Regions;
import lombok.Data;

/*
 * 服务器信息实体类
 * */
@Data
public class ServerList {

    /**
     * ip地址（wegame的已经都脱敏为127.0.0.1）
     */
    @JsonProperty("__addr")
    private String addr;

    /**
     * 房间的Id
     */
    @JsonProperty("__rowId")
    private String rowId;

    /**
     * 主机的克雷ID（有可能是第一位管理员的克雷ID）
     */
    private String host;

    /**
     * 是否启用mod
     */
    private boolean mods;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 是否密码
     */
    private boolean password;

    /**
     * 最大连接数
     */
    private int maxconnections;

    /**
     * 服务器是否专用
     */
    private boolean dedicated;

    /**
     * 客户端ID是否为rowId，申请了不止一个服务器token就会为false
     */
    private boolean clienthosted;

    /**
     * 目前连接人数
     */
    private int connected;
    /**
     * 游戏模式
     */
    private String mode;

    /**
     * 端口
     */
    private int port;

    /**
     * 版本号
     */
    private int v;

    /**
     * 季节
     */
    private String season;

    /**
     * 只局域网
     */
    private boolean lanonly;

    /**
     * 预设
     */
    private String intent;
    /**
     * 多层世界信息
     */
    //private Secondaries secondaries;
    private String data;

    /**
     * 世界设置的加密字符串
     */
    private String worldgen;

    /**
     * 玩家字符串：return {\n  {\n    colour=\"CD4F39\",\n    eventlevel=0,\n    name=\"79f5235e\",\n    netid=\"76561199374256552\",\n    prefab=\"wendy\" \n  } \n}"
     */
    private String players;

    /**
     * 描述信息
     */
    private String desc;

    private int tick;

    private boolean clientmodsoff;

    private int nat;

    /**
     * 标记是注册在哪个区域的服务器（目前一共有四个在MyPlatform枚举）
     */
    private String region;
}
