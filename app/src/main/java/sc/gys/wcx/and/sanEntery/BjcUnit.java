package sc.gys.wcx.and.sanEntery;

import java.util.Date;

import cn.mozile.schManage.pojo.JwTrade;

/* 添加单位信息的实体类 */

public class BjcUnit {

    private Integer id;

    private String name;

    private Integer tradeId;

    private String legalaName;

    private String address;

    private String phone;

    private Integer sex;

    private Integer age;

    private Date birthday;

    private Integer zjtype;

    private String zjnum;

    private String type;

    private String pcs;

    private String attr;

    private String level;

    private JwTrade trade;

    private String photoUrl;

    private Integer state;


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public JwTrade getTrade() {
        return trade;
    }

    public void setTrade(JwTrade trade) {
        this.trade = trade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public String getLegalaName() {
        return legalaName;
    }

    public void setLegalaName(String legalaName) {
        this.legalaName = legalaName == null ? null : legalaName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getZjtype() {
        return zjtype;
    }

    public void setZjtype(Integer zjtype) {
        this.zjtype = zjtype;
    }

    public String getZjnum() {
        return zjnum;
    }

    public void setZjnum(String zjnum) {
        this.zjnum = zjnum == null ? null : zjnum.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs == null ? null : pcs.trim();
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr == null ? null : attr.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

}
