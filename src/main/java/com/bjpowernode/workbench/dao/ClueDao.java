package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Clue;

public interface ClueDao {

    int save(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
