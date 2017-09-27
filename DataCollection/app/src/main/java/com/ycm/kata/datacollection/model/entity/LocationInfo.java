package com.ycm.kata.datacollection.model.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by changmuyu on 2017/9/27.
 * Description:
 */
@Entity
public class LocationInfo {
    private long id;
    private String address;
    @Generated(hash = 243395165)
    public LocationInfo(long id, String address) {
        this.id = id;
        this.address = address;
    }
    @Generated(hash = 1054559726)
    public LocationInfo() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocationInfo)) {
            return false;
        }

        return ((LocationInfo) obj).getId() == id;
    }
}
