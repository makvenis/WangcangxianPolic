package sc.gys.wcx.and.newCompanyPost;

/* 当前签名的实体类 */

public class CompanyGouseEntry {

    public String type; //当前Item的类型

    public String name; //当前Item的标题名称

    public String key;  //当前Item的键

    public Integer integer;//当前Item的右侧小图标 用于点击事件的图标

    @Override
    public String toString() {
        return "CompanyGouseEntry{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", integer=" + integer +
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

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public CompanyGouseEntry() {
    }

    public CompanyGouseEntry(String type, String name, String key, Integer integer) {
        this.type = type;
        this.name = name;
        this.key = key;
        this.integer = integer;
    }
}
