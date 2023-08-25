package com.songyi.entity.enums;

public enum MyPlatform {


    STEAM("steam","Steam"),
    PSN("psn","PSN"),
    RAIL("wegame","Rail"),
    XBONE("xbox","XBone"),
    SWITCH("switch","Switch");

    public String name;

    private String code;

    MyPlatform(String name, String code) {
        this.name = name;
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
