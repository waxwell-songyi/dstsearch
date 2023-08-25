package com.songyi.entity.enums;


public enum Season {
    SPRING("春季", "spring"),
    SUMMER("夏季","summer"),
    AUTUMN("秋季","autumn"),
    WINTER("冬季","winter");

    private String name;
    private String code;

    Season(String name, String code) {
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
        for(Season season : values()){
            if (season.getCode().equals(code)) {
                return season.getName();
            }
        }
        return code;
    }
}
