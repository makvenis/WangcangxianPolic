package sc.gys.wcx.and.sanEntery;

import java.util.Date;

/**
 * 不同意延期整改通知书
 */

public class JwNoyqYinhuanMsg {

    private Integer id;

    private String bianhao1;

    private Integer bianhao2;

    private Integer bianhao3;

    private Integer bjcUnitid;

    private Date tzsTime;

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

    public Date getTzsTime() {
        return tzsTime;
    }

    public void setTzsTime(Date tzsTime) {
        this.tzsTime = tzsTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}
