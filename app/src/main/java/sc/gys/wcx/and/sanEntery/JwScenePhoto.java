package sc.gys.wcx.and.sanEntery;

import java.util.Date;

/**
 * 证据图片上传
 */

public class JwScenePhoto {

    private Integer id;

    private Integer bianhao;

    private String photourl1;

    private String photourl2;

    private String photourl3;

    private Date addtime;

    private String username;

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

    public String getPhotourl1() {
        return photourl1;
    }

    public void setPhotourl1(String photourl1) {
        this.photourl1 = photourl1 == null ? null : photourl1.trim();
    }

    public String getPhotourl2() {
        return photourl2;
    }

    public void setPhotourl2(String photourl2) {
        this.photourl2 = photourl2 == null ? null : photourl2.trim();
    }

    public String getPhotourl3() {
        return photourl3;
    }

    public void setPhotourl3(String photourl3) {
        this.photourl3 = photourl3 == null ? null : photourl3.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }
}
