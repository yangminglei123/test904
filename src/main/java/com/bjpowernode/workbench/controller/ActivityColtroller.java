package com.bjpowernode.workbench.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.vo.PaginationVO;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;
import com.bjpowernode.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/activity")
public class ActivityColtroller {

    @Autowired
    private UserService service;

    @Autowired
    private ActivityService activityService;

    //获取用户名下拉框信息操作
    @RequestMapping(value = "getUserList.do")
    @ResponseBody//把处理器返回对象Activity转换成json格式输出给浏览器
    public List<User> dogetUserList(){

       List<User> user = service.getUserList();
        //获取请求结果数据
       return user;
    }


     //添加操作
    @RequestMapping(value = "save.do")
    public void doActivity(Activity activity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置id,创建人和创建时间
        String id = UUIDUtil.getUUID();
        activity.setId(id);
        activity.setCreateTime(DateTimeUtil.getSysTime());
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        activity.setCreateBy(createBy);

        int i = activityService.save(activity);
        //输出到前端
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pr =response.getWriter();
        pr.print(i);
        pr.flush();
        pr.close();
    }

    //查询页面操作
    @RequestMapping(value = "pageList.do")
    @ResponseBody
    public PaginationVO dopageList(Integer pageNo, Integer pageSize, String name, String owner, String startDate, String endDate){
        //计算掠过的记录数
        int skipCount = (pageNo - 1)*pageSize;
        Map<String, Object> map = new HashMap();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate",endDate);
        map.put("pageSize", pageSize);
        map.put("skipCount",skipCount);

        PaginationVO<Activity> vo = activityService.pageList(map);
        return vo;
    }

    //删除操作
    @RequestMapping(value = "delete.do")
    public void dodelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] str = request.getParameterValues("id");//接收数组变量

        int i = activityService.dodelete(str);

        response.setContentType("application/json;charset=utf-8");
        PrintWriter pr = response.getWriter();
        pr.print(i);
        pr.flush();
        pr.close();
    }

    //修改之前的查询操作
    @RequestMapping(value = "getUserListandActivity.do")
    @ResponseBody
    public Map<String, Object> dogetUserListandActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");

        Map<String, Object> map = activityService.getUserListandActivity(id);

        return map;
    }

    //修改操作
    @RequestMapping(value = "update.do")
    public void doupdate(Activity activity, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置修改人和修改时间
        activity.setEditTime(DateTimeUtil.getSysTime());
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        activity.setEditBy(editBy);

        int i = activityService.update(activity);
        //输出到前端
        response.setContentType("application/json;charset=utf-8");
        PrintWriter pr =response.getWriter();
        pr.print(i);
        pr.flush();
        pr.close();
    }


    //请求转发到市场活动详细信息页面
    @RequestMapping(value = "detail.do")
    public ModelAndView  dodetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Activity activity = activityService. detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("a",activity);

        mv.setViewName("detail");
        return mv;
    }
     //刷新备注信息列表
    @RequestMapping(value = "getRemarkListByAid.do")
    @ResponseBody
    public  List<ActivityRemark> dogetgetRemarkListByAid(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String activityId = request.getParameter("activityId");

        List<ActivityRemark> arlist = activityService.getRemarkListByAid(activityId);

        return arlist;
    }
     //删除备注信息操作
     @RequestMapping(value = "deleteRemark.do")
     public void dodeleteRemark(String id ,HttpServletRequest request, HttpServletResponse response) throws IOException {

         int i = activityService.deleteRemark(id);
         //输出到前端
         response.setContentType("application/json;charset=utf-8");
         PrintWriter pr = response.getWriter();
         pr.print(i);
         pr.flush();
         pr.close();
     }


    //添加备注信息操作
    @RequestMapping(value = "saveRemark.do")
    @ResponseBody
    public Map<String,Object> dosaveRemark(ActivityRemark activityRemark, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = UUIDUtil.getUUID();
        activityRemark.setId(id);//设置备注id
        String createTime = DateTimeUtil.getSysTime();
        activityRemark.setCreateTime(createTime);//设置备注创建时间
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        activityRemark.setCreateBy(createBy);//设置创建人
        activityRemark.setEditFlag(editFlag);//设置是否被修改标记
        int i = activityService.saveRemark(activityRemark);
        Boolean flag =false;
        if(i==1){ flag=true; }
        Map<String,Object>map =new HashMap<>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        //输出到前端
        return map;
    }

    //修改备注信息操作
    @RequestMapping(value = "updateRemark.do")
    @ResponseBody
    public Map<String,Object> updateRemark(ActivityRemark activityRemark, HttpServletRequest request, HttpServletResponse response) throws IOException {

        activityRemark.setEditTime(DateTimeUtil.getSysTime());//设置修改时间
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        activityRemark.setEditBy(editBy);//设置修改人
        String editFlag = "1";
        activityRemark.setEditFlag(editFlag);//设置是否被修改标记
        int i = activityService.updateRemark(activityRemark);
        Boolean flag =false;
        if(i==1){ flag=true; }
        Map<String,Object>map =new HashMap<>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        //输出到前端
        return map;
    }




}
