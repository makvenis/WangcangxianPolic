package com.makvenis.dell.wangcangxianpolic.sanEntery;

import cn.mozile.schManage.pojo.CorrecTmsg;

/**
 * 限期责令改正通知书 实体类
 */

public class CorrecTmsgWithBLOBs extends CorrecTmsg{

    private String content;

    private String fagui;

    private String gzWay;

    private String bjcSignature;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getFagui() {
        return fagui;
    }

    public void setFagui(String fagui) {
        this.fagui = fagui == null ? null : fagui.trim();
    }

    public String getGzWay() {
        return gzWay;
    }

    public void setGzWay(String gzWay) {
        this.gzWay = gzWay == null ? null : gzWay.trim();
    }

    public String getBjcSignature() {
        return bjcSignature;
    }

    public void setBjcSignature(String bjcSignature) {
        this.bjcSignature = bjcSignature == null ? null : bjcSignature.trim();
    }
}
