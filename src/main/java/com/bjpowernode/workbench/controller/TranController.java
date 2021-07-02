package com.bjpowernode.workbench.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.domain.TranHistory;
import com.bjpowernode.workbench.service.CustomerService;
import com.bjpowernode.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/transaction")
public class TranController {

    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;


    @RequestMapping(value = "add.do")
    public ModelAndView doadd(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ModelAndView mv = new ModelAndView();
        List<User> users= userService.getUserList();
        mv.addObject("uList",users);
        mv.setViewName("forward:/workbench/transaction/save.jsp");
        return mv;
    }

  @RequestMapping(value = "getCustomerName.do")
  @ResponseBody
  public List<String> dogetCustomerName(HttpServletRequest request, HttpServletResponse response){
      String name = request.getParameter("name");
      List<String>  sList =customerService.getCustomerName(name);

      return sList;
  }


    @RequestMapping(value = "save.do")
    public ModelAndView dosave(HttpServletRequest request, HttpServletResponse response, Tran tran, HttpSession session) throws IOException {

        ModelAndView mv = new ModelAndView();
        String customerName = request.getParameter("customerName");//接收客户名字
        //设置id，创建时间，创建人
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        String createBy = ((User)session.getAttribute("user")).getName();
        tran.setCreateBy(createBy);

        boolean flag = tranService.save(tran,customerName);
         if(flag){//添加成功转到交易页面
             mv.setViewName("redirect:/workbench/transaction/index.jsp");//重定向
         }
        return mv;
    }
    //跳转到详细信息页detail.do
    @RequestMapping(value = "detail.do")
    public ModelAndView dodetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        Tran tran = tranService. detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("t",tran);

        //处理可能性
        String stage = tran.getStage();
        Map<String,String>pMap = (Map<String, String>)request.getSession().getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        tran.setPossibility(possibility);

        mv.setViewName("forward:/workbench/transaction/detail.jsp");
        return mv;
    }

     @RequestMapping(value = "getHistoryListByTranId.do")
     @ResponseBody
     public List<TranHistory> dogetHistoryListByTranId(String tranId,HttpServletRequest request){

         List<TranHistory> ths=tranService.getHistoryListByTranId(tranId);
         //处理可能性
         Map<String,String>pMap = (Map<String, String>)request.getSession().getServletContext().getAttribute("pMap");
         for(TranHistory th:ths){
             String stage = th.getStage();
             String possibility = pMap.get(stage);
             th.setPossibility(possibility);
         }

         return ths;
     }


    @RequestMapping(value = "changeStage.do")
    @ResponseBody
    public Tran dochangeStage(Tran t,HttpServletRequest request,HttpSession session) {

        t.setEditTime(DateTimeUtil.getSysTime());
        String editBy = ((User)session.getAttribute("user")).getName();
        t.setEditBy(editBy);

        int i = tranService.changeStage(t);

        Map<String,String>pMap = (Map<String, String>)request.getSession().getServletContext().getAttribute("pMap");
        String stage = request.getParameter("stage");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        if (i==1) {
            return t;
        } else {
            return null;
        }

    }

    //getCharts.do
    @RequestMapping(value = "getCharts.do")
    @ResponseBody
    public Map<String,Object> dogetCharts(){

        Map<String,Object> map=tranService.getCharts();


        return map;
    }





}
