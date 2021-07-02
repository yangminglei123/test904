package com.bjpowernode.web.listener;

import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.settings.service.DicService;
import com.bjpowernode.settings.service.impl.DicServiceImpl;
import com.bjpowernode.utils.ServiceFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("处理数据字典开始");
        ServletContext application = servletContextEvent.getServletContext();//全局作用域对象
        //拿service
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring.xml");
        DicService dicService = (DicService) context.getBean("dicService");
        //取数据字典
        Map<String, List<DicValue> > map = dicService.getAll();
         //将map解析为上下文作用域中保存的键值对
        Set<String> keys =map.keySet();//拿到集合中所有的key
        for(String key:keys){
            application.setAttribute(key,map.get(key));//通过key取value
        }
        System.out.println("处理数据字典结束");


       //数据字典处理完成后处理Stage2Possibility.properties文件---------------------------

        //解析properties文件

        Map<String,String> pMap = new HashMap<String,String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()){

            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key, value);


        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);

    }





    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent){}

}
