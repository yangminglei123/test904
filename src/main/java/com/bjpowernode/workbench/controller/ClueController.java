package com.bjpowernode.workbench.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clue")
public class ClueController {

    @Autowired
    private UserService service;

    @Autowired
    private ClueService clueService;

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

    @RequestMapping(value = "save.do")
    @ResponseBody
    public int dosave(Clue clue,HttpSession session){
        String id = UUIDUtil.getUUID();
        clue.setId(id);
        clue.setCreateTime(DateTimeUtil.getSysTime());
        String createBy = ((User)session.getAttribute("user")).getName();
        clue.setCreateBy(createBy);

        int i = clueService.save(clue);

        return i;
    }


    //请求转发到市场活动详细信息页面
    @RequestMapping(value = "detail.do")
    public ModelAndView dodetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Clue clue = clueService. detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("c",clue);

        mv.setViewName("forward:/workbench/clue/detail.jsp");
        return mv;
    }

    //取到线索（潜在客户）关联的市场活动
    @RequestMapping(value = "getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> dogetActivityListByClueId(String clueId){

         List<Activity> activityList = activityService.getActivityListByClueId(clueId);

        return activityList;
    }


    @RequestMapping(value = "unbund.do")
    @ResponseBody
    public int dounbund(String id){

        int i=clueService.unbund(id);

        return i;
    }

    //修改之前的查询操作
    @RequestMapping(value = "getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> dogetActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String clueId = request.getParameter("clueId");
        String aname = request.getParameter("aname");
        Map<String, String> map =new HashMap<>();
        map.put("clueId",clueId);
        map.put("aname",aname);
        List<Activity>alist = activityService.getActivityListByNameAndNotByClueId(map);

        return alist;
    }

   //关联市场活动操作
    @RequestMapping(value = "bund.do")
    @ResponseBody
    public boolean dobund(HttpServletRequest request,HttpServletResponse response) {

        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        Boolean flag = clueService.bund(cid,aids);

        return flag;
    }


    @RequestMapping(value = "getActivityListByName.do")
    @ResponseBody
    public List<Activity> dogetActivityListByName(String aname) {

        List<Activity> activityList = activityService.getActivityListByName(aname);

        return activityList;
    }


    @RequestMapping(value = "convert.do")
    public  ModelAndView  doconvert(Tran tran , String clueId,HttpSession session){
      ModelAndView modelAndView =new ModelAndView();
        String createBy = ((User)session.getAttribute("user")).getName();
        //表示表单没有提交

        if(tran==null){
            modelAndView.addObject("clueId",clueId);
            tran =null;
        }else {//表示提交了交易信息表单
            //完善交易表单信息
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());

            tran.setCreateBy(createBy);

            modelAndView.addObject("clueId",clueId);
            modelAndView.addObject("tran",tran);
        }
        boolean flag  = clueService.convert(clueId,tran,createBy);

        if(flag){

            modelAndView.setViewName("forward:/workbench/clue/index.jsp");
        }
        return modelAndView;

    }

}
