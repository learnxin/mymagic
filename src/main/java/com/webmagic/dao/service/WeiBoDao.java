package com.webmagic.dao.service;

import com.webmagic.dao.entity.WeiBo;
import org.springframework.stereotype.Repository;

@Repository
public interface WeiBoDao {
    void insert(WeiBo weiBo);
}
