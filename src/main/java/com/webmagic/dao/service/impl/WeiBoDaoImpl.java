package com.webmagic.dao.service.impl;

import com.webmagic.dao.entity.WeiBo;
import com.webmagic.dao.service.WeiBoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class WeiBoDaoImpl implements WeiBoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(WeiBo weiBo) {
        mongoTemplate.save(weiBo);
    }
}
