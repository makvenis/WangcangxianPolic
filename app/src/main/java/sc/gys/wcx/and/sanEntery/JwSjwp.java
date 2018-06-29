package sc.gys.wcx.and.sanEntery;

import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2018/5/26.
 */

public class JwSjwp {

    private Integer id;

    private Integer chufaid;

    private String bianhao1;

    private String bianhao2;

    private Integer bianhao3;

    private String tiaokuan;

    private String cyrName;

    private Date sjTime;

    private String cyrSignature;

    private String bgrSignature;

    private String policeSignature;

    private String unitid; //单位id

    private List<JwSjwpDetails> jwSjwpDetails;

    public String getUnitId() {
        return unitid;
    }

    public void setUnitId(String unitId) {
        this.unitid = unitId;
    }

    public Integer getChufaid() {
        return chufaid;
    }

    public void setChufaid(Integer chufaid) {
        this.chufaid = chufaid;
    }

    public List<JwSjwpDetails> getJwSjwpDetails() {
        return jwSjwpDetails;
    }

    public void setJwSjwpDetails(List<JwSjwpDetails> jwSjwpDetails) {
        this.jwSjwpDetails = jwSjwpDetails;
    }

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

    public String getBianhao2() {
        return bianhao2;
    }

    public void setBianhao2(String bianhao2) {
        this.bianhao2 = bianhao2 == null ? null : bianhao2.trim();
    }


    public Integer getBianhao3() {
        return bianhao3;
    }

    public void setBianhao3(Integer bianhao3) {
        this.bianhao3 = bianhao3;
    }

    public String getTiaokuan() {
        return tiaokuan;
    }

    public void setTiaokuan(String tiaokuan) {
        this.tiaokuan = tiaokuan == null ? null : tiaokuan.trim();
    }

    public String getCyrName() {
        return cyrName;
    }

    public void setCyrName(String cyrName) {
        this.cyrName = cyrName == null ? null : cyrName.trim();
    }

    public Date getSjTime() {
        return sjTime;
    }

    public void setSjTime(Date sjTime) {
        this.sjTime = sjTime;
    }

    public String getCyrSignature() {
        return cyrSignature;
    }

    public void setCyrSignature(String cyrSignature) {
        this.cyrSignature = cyrSignature == null ? null : cyrSignature.trim();
    }

    public String getBgrSignature() {
        return bgrSignature;
    }

    public void setBgrSignature(String bgrSignature) {
        this.bgrSignature = bgrSignature == null ? null : bgrSignature.trim();
    }

    public String getPoliceSignature() {
        return policeSignature;
    }

    public void setPoliceSignature(String policeSignature) {
        this.policeSignature = policeSignature == null ? null : policeSignature.trim();
    }

}
