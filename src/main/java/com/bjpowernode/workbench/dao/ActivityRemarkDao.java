package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
   //传入外键ids查询
    int getCountByAids(String[] ids);
    //传入外键ids删除
    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
