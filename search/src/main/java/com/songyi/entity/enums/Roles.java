package com.songyi.entity.enums;

import org.apache.commons.lang.StringUtils;

public enum Roles {

    WILSON("威尔逊", "wilson"),
    WILLOW("薇洛", "willow"),
    WOLFGANG("沃尔夫冈", "wolfgang"),
    WENDY("温蒂", "wendy"),
    WX_78("WX-78", "wx78"),
    WICKERBOTTOM("薇克巴顿", "wickerbottom"),
    WOODIE("伍迪", "woodie"),
    WES("韦斯", "wes"),
    MAXWELL("麦斯威尔", "waxwell"),
    WIGFRID("薇格弗德", "wathgrithr"),
    WEBBER("韦伯", "webber"),
    WINONA("薇诺娜", "winona"),
    WARLY("沃利", "warly"),
    WALTER("沃尔特", "walter"),
    WORTOX("沃拓克斯", "wortox"),
    WORMWOOD("沃姆伍德", "wormwood"),
    WURT("沃特", "wurt"),
    WANDA("旺达", "wanda"),
    WONKEY("芜猴", "wonkey");

    Roles(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private String name;
    private String code;

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
     *
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return "未选择";
        }
        for (Roles roles : values()) {
            if (roles.getCode().equals(code)) {
                return roles.getName();
            }
        }
        return code;
    }
}
