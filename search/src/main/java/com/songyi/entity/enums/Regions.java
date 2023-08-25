package com.songyi.entity.enums;


public enum Regions {
    US_EAST_1("美国东部", "us-east-1"),
    EU_CENTRAL_1("欧洲中心", "eu-central-1"),
    AP_SOUTHEAST_1("东南亚", "ap-southeast-1"),
    AP_EAST_1("东亚", "ap-east-1");
    private String name;
    private String code;

    Regions(String name, String code) {
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
