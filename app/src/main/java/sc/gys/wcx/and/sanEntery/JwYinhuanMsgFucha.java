package sc.gys.wcx.and.sanEntery;

import java.util.Date;

/**
 * 复查意见书
 */

public class JwYinhuanMsgFucha {

    private Integer id;

    private String bianhao1;

    private Integer bianhao2;

    private Integer bianhao3;

    private Integer bjcUnitid;

    private Date fcTime;

    private Date fcmsgTime;

    private Integer yinghuanmsgid;

    private Integer dabiao;

    private Integer hege;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBianhao1() {
        return bianhao1;
    }

    public void setBianhao1(String bianhao1) {
        this.bianhao1 = bianhao1 == null ? null : bianhao1.trim();
    }

    public Integer getBianhao2() {
        return bianhao2;
    }

    public void setBianhao2(Integer bianhao2) {
        this.bianhao2 = bianhao2;
    }

    public Integer getBianhao3() {
        return bianhao3;
    }

    public void setBianhao3(Integer bianhao3) {
        this.bianhao3 = bianhao3;
    }

    public Integer getBjcUnitid() {
        return bjcUnitid;
    }

    public void setBjcUnitid(Integer bjcUnitid) {
        this.bjcUnitid = bjcUnitid;
    }

    public Date getFcTime() {
        return fcTime;
    }

    public void setFcTime(Date fcTime) {
        this.fcTime = fcTime;
    }

    public Date getFcmsgTime() {
        return fcmsgTime;
    }

    public void setFcmsgTime(Date fcmsgTime) {
        this.fcmsgTime = fcmsgTime;
    }

    public Integer getYinghuanmsgid() {
        return yinghuanmsgid;
    }

    public void setYinghuanmsgid(Integer yinghuanmsgid) {
        this.yinghuanmsgid = yinghuanmsgid;
    }

    public Integer getDabiao() {
        return dabiao;
    }

    public void setDabiao(Integer dabiao) {
        this.dabiao = dabiao;
    }

    public Integer getHege() {
        return hege;
    }

    public void setHege(Integer hege) {
        this.hege = hege;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

}
