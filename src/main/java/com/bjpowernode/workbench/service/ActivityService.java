package com.bjpowernode.workbench.service;

import com.bjpowernode.vo.PaginationVO;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int save(Activity activity);


    PaginationVO<Activity> pageList(Map<String, Object> map);

    int dodelete(String [] ids);

    Map<String, Object> getUserListandActivity(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map);

    List<Activity> getActivityListByName(String aname);
}
