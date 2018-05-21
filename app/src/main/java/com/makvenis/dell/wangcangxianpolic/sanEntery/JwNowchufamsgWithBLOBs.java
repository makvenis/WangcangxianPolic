package com.makvenis.dell.wangcangxianpolic.sanEntery;

/**
 * 触发 父类
 */

public class JwNowchufamsgWithBLOBs {

    private String content1;

    private String content2;

    private String chufaZxfs;

    private String bjcSignature;


    public String getBjcSignature() {
        return bjcSignature;
    }

    public void setBjcSignature(String bjcSignature) {
        this.bjcSignature = bjcSignature;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1 == null ? null : content1.trim();
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2 == null ? null : content2.trim();
    }

    public String getChufaZxfs() {
        return chufaZxfs;
    }

    public void setChufaZxfs(String chufaZxfs) {
        this.chufaZxfs = chufaZxfs == null ? null : chufaZxfs.trim();
    }
}
