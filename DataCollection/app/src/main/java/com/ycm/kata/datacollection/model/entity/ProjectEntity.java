package com.ycm.kata.datacollection.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by changmuyu on 2017/9/18.
 * Description:
 */
@Entity
public class ProjectEntity {
    @Id
    private int id;
    private String projectName;
    private long checkDate;
    private long updateTime;
    private String unitEngineering;
    private String block;
    private String pilNo;
    private String remark;
    private String defects;
    private String imagePath;
    @Generated(hash = 1231905102)
    public ProjectEntity(int id, String projectName, long checkDate,
            long updateTime, String unitEngineering, String block, String pilNo,
            String remark, String defects, String imagePath) {
        this.id = id;
        this.projectName = projectName;
        this.checkDate = checkDate;
        this.updateTime = updateTime;
        this.unitEngineering = unitEngineering;
        this.block = block;
        this.pilNo = pilNo;
        this.remark = remark;
        this.defects = defects;
        this.imagePath = imagePath;
    }
    @Generated(hash = 939074542)
    public ProjectEntity() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProjectName() {
        return this.projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public long getCheckDate() {
        return this.checkDate;
    }
    public void setCheckDate(long checkDate) {
        this.checkDate = checkDate;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
    public String getUnitEngineering() {
        return this.unitEngineering;
    }
    public void setUnitEngineering(String unitEngineering) {
        this.unitEngineering = unitEngineering;
    }
    public String getBlock() {
        return this.block;
    }
    public void setBlock(String block) {
        this.block = block;
    }
    public String getPilNo() {
        return this.pilNo;
    }
    public void setPilNo(String pilNo) {
        this.pilNo = pilNo;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getDefects() {
        return this.defects;
    }
    public void setDefects(String defects) {
        this.defects = defects;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
