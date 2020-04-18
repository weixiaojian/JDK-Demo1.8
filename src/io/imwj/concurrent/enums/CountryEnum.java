package io.imwj.concurrent.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 枚举类其实就是数据版的mysql数据库
 * @author langao_q
 * @create 2020-04-17 16:06
 */
public enum CountryEnum {

    ONE(1, "齐"), TWO(2, "楚"), THREE(3, "燕"),FOUR(4, "赵"), FIVE(5, "魏"),SIX(6, "韩");

    CountryEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public static CountryEnum forEach(int index) {
        CountryEnum[] countryEnums = CountryEnum.values();
        for (CountryEnum countryEnum : countryEnums) {
            if (index == countryEnum.getCode()) {
                return countryEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
