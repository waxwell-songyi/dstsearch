package com.songyi.entity.enums;

import lombok.Data;


public enum Style {

    relaxed("轻松","relaxed"),
    survival("生存","survival"),
    endless("无尽","endless"),
    wilderness("荒野","wilderness"),
    lightsout("暗无天日","lightsout"),
    OceanFishing("海钓","OceanFishing");
    private String name;
    private String code;

    Style(String name, String code) {
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

    /**
     * 匹配方法
     * @param code
     * @return
     */
    public static String getNameByCode(String code){
        for(Style style : values()){
            if (style.getCode().equals(code)) {
                return style.getName();
            }
        }
        return code;
    }
}
