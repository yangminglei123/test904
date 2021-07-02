package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    Tran detail(String id);

    int changeStage(Tran t);

    int getcount();

    List<Map<String, Object>> getCharts();
}
