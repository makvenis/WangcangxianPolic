package com.makvenis.dell.wangcangxianpolic.sanEntery;

/**
 * Created by dell on 2018/5/26.
 */

public class JwSjwpDetails {

    private Integer id;

    private Integer sjwpid;

    private String bianhao;

    private String sjwpname;

    private Integer sjwpnum;

    private String sjwpFeature;

    private String sjwpDispose;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSjwpid() {
        return sjwpid;
    }

    public void setSjwpid(Integer sjwpid) {
        this.sjwpid = sjwpid;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao == null ? null : bianhao.trim();
    }

    public String getSjwpname() {
        return sjwpname;
    }

    public void setSjwpname(String sjwpname) {
        this.sjwpname = sjwpname == null ? null : sjwpname.trim();
    }

    public Integer getSjwpnum() {
        return sjwpnum;
    }

    public void setSjwpnum(Integer sjwpnum) {
        this.sjwpnum = sjwpnum;
    }

    public String getSjwpFeature() {
        return sjwpFeature;
    }

    public void setSjwpFeature(String sjwpFeature) {
        this.sjwpFeature = sjwpFeature == null ? null : sjwpFeature.trim();
    }

    public String getSjwpDispose() {
        return sjwpDispose;
    }

    public void setSjwpDispose(String sjwpDispose) {
        this.sjwpDispose = sjwpDispose == null ? null : sjwpDispose.trim();
    }

}
