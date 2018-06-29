package sc.gys.wcx.and.sanEntery;

import java.util.Date;

/**
 * 检查笔录
 */

public class Jcbilu {
    private Integer id;

    private Integer jcProjectid;

    private Integer jcStaffid;

    private String jcSignature;

    private String jlSignature;

    private String bjcSignature;

    private Date jlStarttime;

    private Date jlEndtime;

    private String jcResult;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJcProjectid() {
        return jcProjectid;
    }

    public void setJcProjectid(Integer jcProjectid) {
        this.jcProjectid = jcProjectid;
    }

    public Integer getJcStaffid() {
        return jcStaffid;
    }

    public void setJcStaffid(Integer jcStaffid) {
        this.jcStaffid = jcStaffid;
    }

    public String getJcSignature() {
        return jcSignature;
    }

    public void setJcSignature(String jcSignature) {
        this.jcSignature = jcSignature == null ? null : jcSignature.trim();
    }

    public String getJlSignature() {
        return jlSignature;
    }

    public void setJlSignature(String jlSignature) {
        this.jlSignature = jlSignature == null ? null : jlSignature.trim();
    }

    public String getBjcSignature() {
        return bjcSignature;
    }

    public void setBjcSignature(String bjcSignature) {
        this.bjcSignature = bjcSignature == null ? null : bjcSignature.trim();
    }

    public Date getJlStarttime() {
        return jlStarttime;
    }

    public void setJlStarttime(Date jlStarttime) {
        this.jlStarttime = jlStarttime;
    }

    public Date getJlEndtime() {
        return jlEndtime;
    }

    public void setJlEndtime(Date jlEndtime) {
        this.jlEndtime = jlEndtime;
    }

    public String getJcResult() {
        return jcResult;
    }

    public void setJcResult(String jcResult) {
        this.jcResult = jcResult == null ? null : jcResult.trim();
    }

    @Override
    public String toString() {
        return "Jcbilu [id=" + id + ", jcProjectid=" + jcProjectid
                + ", jcStaffid=" + jcStaffid + ", jcSignature=" + jcSignature
                + ", jlSignature=" + jlSignature + ", bjcSignature="
                + bjcSignature + ", jlStarttime=" + jlStarttime
                + ", jlEndtime=" + jlEndtime + ", jcResult=" + jcResult + "]";
    }
}
