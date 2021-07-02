package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.vo.PaginationVO;
import com.bjpowernode.workbench.dao.ActivityDao;
import com.bjpowernode.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;
import com.bjpowernode.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityRemarkDao activityRemarkDao;
    @Autowired
    private UserDao userDao;

    @Override
    public int save(Activity activity) {

        int i = activityDao.save(activity);
        return i;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map){
        //取得记录条数
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList=activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo中
        PaginationVO<Activity>vo =new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //返回vo

        return vo;
    }

    @Override
    public int dodelete(String[] ids) {

        for(String s:ids){
            System.out.println(s+"------------------");
        }

        //查询方法
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除方法
        int count2 = activityRemarkDao.deleteByAids(ids);

        int count3= activityDao.delete(ids);
        //如果ActivityRemark表查询的条数跟删除的条数一样 并且 Activity表查询的条数跟数组的条数一样 返回1
         if(count1==count2 && count3== ids.length){
             return 1;
         }else {

             return 0;
         }

    }


    @Override
    public Map<String, Object> getUserListandActivity(String id) {
        //取usrtlist
        List<User>uList =userDao.getUserList();

        //取Activity
         Activity a =activityDao.getById(id);

        //装到map中
        Map<String ,Object>map= new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);

        return map;
    }

    @Override
    public int update(Activity activity) {

        int i = activityDao.update(activity);

        return i;

    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> list =  activityRemarkDao.getRemarkListByAid(activityId);

        return list;
    }

    @Override
    public int deleteRemark(String id) {

        int i = activityRemarkDao.deleteRemark(id);

        return i;
    }

    @Override
    public int saveRemark(ActivityRemark activityRemark) {

        int i  = activityRemarkDao.saveRemark(activityRemark);

        return i;
    }

    @Override
    public int updateRemark(ActivityRemark activityRemark) {
        int i = activityRemarkDao.updateRemark(activityRemark);


        return i;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> alist=  activityDao.getActivityListByClueId(clueId);

        return alist;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity>alist = activityDao.getActivityListByNameAndNotByClueId(map);

        return alist;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        List<Activity> activityList=activityDao.getActivityListByName(aname);

        return activityList;
    }
}
