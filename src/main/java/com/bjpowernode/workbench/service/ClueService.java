package com.bjpowernode.workbench.service;

import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;

public interface ClueService {
    int save(Clue clue);

    Clue detail(String id);

    int unbund(String id);

    boolean bund(String cid, String[] aid);

    boolean convert(String clueId, Tran tran, String createBy);
}
