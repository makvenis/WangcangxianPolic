package com.makvenis.dell.wangcangxianpolic.newCompanyPost;

/* 时间变量的实体类 */

public class CompanyTimeEntry {

    public String type; //当前Item的类型

    public String name; //当前Item的标题名称

    public String key;  //当前Item的键 用于回到当前的Map键

    public Integer value;//当前Item的时间点击事件的图标

    @Override
    public String toString() {
        return "CompanyTimeEntry{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public CompanyTimeEntry() {
    }

    public CompanyTimeEntry(String type, String name, String key, Integer value) {
        this.type = type;
        this.name = name;
        this.key = key;
        this.value = value;
    }
}
