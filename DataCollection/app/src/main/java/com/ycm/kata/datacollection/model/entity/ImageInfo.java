package com.ycm.kata.datacollection.model.entity;

/**
 * Created by changmuyu on 2017/9/23.
 * Description:
 */

public class ImageInfo {
    private String path;
    private int width;
    private int height;

    public ImageInfo(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
