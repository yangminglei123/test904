package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.dao.CustomerDao;
import com.bjpowernode.workbench.dao.TranDao;
import com.bjpowernode.workbench.dao.TranHistoryDao;
import com.bjpowernode.workbench.domain.Customer;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.domain.TranHistory;
import com.bjpowernode.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Autowired
   private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
    @Autowired
    private CustomerDao customerDao;

    @Override
    public boolean save(Tran tran, String customerName) {
        //通过传入的客户名字查询客户单条
        Customer cus = customerDao.getCustomerByName(customerName);

        boolean flag = true;

        if (cus == null) { //如果查询不到名字创建一个客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(tran.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(tran.getContactSummary());
            cus.setNextContactTime(tran.getNextContactTime());
            cus.setOwner(tran.getOwner());
            int count1 = customerDao.save(cus);
            if (count1 != 1) {
                flag = false;
            }
        }
        //取出客户id放入交易中
        tran.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(tran);
        if (count2 != 1) {
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(tran.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran tran = tranDao.detail(id);

        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> ths = tranHistoryDao. getHistoryListByTranId(tranId);

        return ths;
    }

    @Override
    public int changeStage(Tran t) {
        int i = tranDao.changeStage(t);

        //交易阶段改变后，生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(i==1&&count2==1){
            return 1;
        }else {
            return 0;
        }

    }

    @Override
    public Map<String, Object> getCharts() {
        int total = tranDao.getcount();

       List<Map<String,Object>> dataList = tranDao.getCharts();

        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);


        return map;
    }
}
