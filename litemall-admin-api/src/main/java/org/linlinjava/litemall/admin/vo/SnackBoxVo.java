package org.linlinjava.litemall.admin.vo;

import org.linlinjava.litemall.db.domain.QingBoxgoods;

import java.util.Date;
import java.util.List;

public class SnackBoxVo {
    private Integer id;

    private String name;

    private String picUrl;

    private String school;

    private String address;

    private Date createTime;

    private Date modifyTime;

    private List<QingBoxgoods> boxgoods;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<QingBoxgoods> getBoxgoods() {
        return boxgoods;
    }

    public void setBoxgoods(List<QingBoxgoods> boxgoods) {
        this.boxgoods = boxgoods;
    }

    @Override
    public String toString() {
        return "SnackBoxVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", school='" + school + '\'' +
                ", address='" + address + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", boxgoods=" + boxgoods +
                '}';
    }
}
