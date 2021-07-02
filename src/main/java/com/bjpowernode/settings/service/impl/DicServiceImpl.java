package com.bjpowernode.settings.service.impl;

import com.bjpowernode.settings.dao.DicTypeDao;
import com.bjpowernode.settings.dao.DicValueDao;
import com.bjpowernode.settings.domain.DicType;
import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao;

    private DicValueDao dicValueDao;


    //set注入赋值

    public void setDicTypeDao(DicTypeDao dicTypeDao) {
        this.dicTypeDao = dicTypeDao;
    }

    public void setDicValueDao(DicValueDao dicValueDao) {
        this.dicValueDao = dicValueDao;
    }

    @Override
    public Map<String, List<DicValue>> getAll() {
        List <DicType> dtlist = dicTypeDao.getTypeList();

        Map<String, List<DicValue>> map= new HashMap<>();
        //将字典类型列表DicType遍历
        for(DicType dt: dtlist){

           String code = dt.getCode();//拿到七种code类型

          List<DicValue>dicValues =dicValueDao.getListByCode(code);//根据code查出value

            map.put(code,dicValues);//一个code对应多个value
        }

        return map;
    }
}

