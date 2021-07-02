package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.workbench.dao.CustomerDao;
import com.bjpowernode.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerName(String name) {
        List<String> list = customerDao. getCustomerName(name);
        return list;
    }
}
