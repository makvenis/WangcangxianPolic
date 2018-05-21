package com.makvenis.dell.wangcangxianpolic.sanEntery;

import java.util.Date;

/**
 * 当场处罚 实体类
 */

public class JwNowchufamsg extends JwNowchufamsgWithBLOBs{

    private Integer id;

    private Integer bianhao;

    private Integer unitid;

    private String tiaoli;

    private Integer tiaonum;

    private Integer kuannum;

    private Integer xiangnum;

    private String chufaContent;

    private String chufaAddress;

    private String policename;

    private Integer captureid;

    private Date chufamesTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getBianhao() {
        return bianhao;
    }

    public void setBianhao(Integer bianhao) {
        this.bianhao = bianhao;
    }

    public Integer getUnitid() {
        return unitid;
    }

    public void setUnitid(Integer unitid) {
        this.unitid = unitid;
    }

    public String getTiaoli() {
        return tiaoli;
    }

    public void setTiaoli(String tiaoli) {
        this.tiaoli = tiaoli == null ? null : tiaoli.trim();
    }

    public Integer getTiaonum() {
        return tiaonum;
    }

    public void setTiaonum(Integer tiaonum) {
        this.tiaonum = tiaonum;
    }

    public Integer getKuannum() {
        return kuannum;
    }

    public void setKuannum(Integer kuannum) {
        this.kuannum = kuannum;
    }

    public Integer getXiangnum() {
        return xiangnum;
    }

    public void setXiangnum(Integer xiangnum) {
        this.xiangnum = xiangnum;
    }

    public String getChufaContent() {
        return chufaContent;
    }

    public void setChufaContent(String chufaContent) {
        this.chufaContent = chufaContent == null ? null : chufaContent.trim();
    }

    public String getChufaAddress() {
        return chufaAddress;
    }

    public void setChufaAddress(String chufaAddress) {
        this.chufaAddress = chufaAddress == null ? null : chufaAddress.trim();
    }

    public String getPolicename() {
        return policename;
    }

    public void setPolicename(String policename) {
        this.policename = policename == null ? null : policename.trim();
    }

    public Integer getCaptureid() {
        return captureid;
    }

    public void setCaptureid(Integer captureid) {
        this.captureid = captureid;
    }

    public Date getChufamesTime() {
        return chufamesTime;
    }

    public void setChufamesTime(Date chufamesTime) {
        this.chufamesTime = chufamesTime;
    }
}
