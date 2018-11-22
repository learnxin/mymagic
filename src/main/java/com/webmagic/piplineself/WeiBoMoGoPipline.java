package com.webmagic.piplineself;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.webmagic.dao.entity.WeiBo;
import com.webmagic.dao.service.WeiBoDao;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class WeiBoMoGoPipline implements Pipeline{
    /*@Autowired
    WeiBoDao weiBoDao;
    @Autowired
    private MongoTemplate mongoTemplate;*/

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        for (Map.Entry<String,Object> entry:resultItems.getAll().entrySet()
             ) {
            String key = entry.getKey();
            List<String> value = (List<String>) entry.getValue();
//            mongoTemplate.save(new WeiBo(key,value));
            List<Document> documents = new ArrayList<Document>();
            for (String obj:value
                 ) {
                documents.add(new Document(key, JSON.parseObject(obj)));
            }
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("weibotest");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            collection.insertMany(documents);
        }
    }
}
