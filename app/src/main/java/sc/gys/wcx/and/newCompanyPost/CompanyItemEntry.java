package sc.gys.wcx.and.newCompanyPost;

/**
 * Created by dell on 2018/6/3.
 */

public class CompanyItemEntry {
    public String type; //当前Item的类型

    public String name; //当前Item的标题名称

    public String key;  //当前Item的键

    public String value;//当前Item的值

    @Override
    public String toString() {
        return "CompanyItemEntry{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public CompanyItemEntry(String type, String name, String key, String value) {
        this.type = type;
        this.name = name;
        this.key = key;
        this.value = value;
    }

    public CompanyItemEntry() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
