package com.ycm.kata.datacollection.view;

import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.util.List;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public interface GetDataListener {
    void loadSuccess(List<ProjectEntity> dataSource);
}
