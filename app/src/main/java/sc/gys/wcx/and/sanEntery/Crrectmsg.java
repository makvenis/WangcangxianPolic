package sc.gys.wcx.and.sanEntery;

import java.util.Date;


public class Crrectmsg {

    private Integer id;

    private String bianhao1;

    private Integer bianhao2;

    private Integer bianhao3;

    private Integer unitid;

    private Date tzsTime;

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

    public Integer getUnitid() {
        return unitid;
    }

    public void setUnitid(Integer unitid) {
        this.unitid = unitid;
    }

    public Date getTzsTime() {
        return tzsTime;
    }

    public void setTzsTime(Date tzsTime) {
        this.tzsTime = tzsTime;
    }
}
