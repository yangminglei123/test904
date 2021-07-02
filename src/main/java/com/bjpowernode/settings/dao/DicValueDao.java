package com.bjpowernode.settings.dao;

import com.bjpowernode.settings.domain.DicValue;

import java.util.List;


public interface DicValueDao {

    List<DicValue> getListByCode(String code);
}
