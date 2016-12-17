package com.zaaach.citypicker.model;

/**
 * author zaaach on 2016/1/26.
 */
public class City {
    private String name;
    private String pinyin;

    public City() {}

    public City(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
