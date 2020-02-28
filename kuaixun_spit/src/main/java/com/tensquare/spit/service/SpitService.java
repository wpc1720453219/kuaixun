package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idCreater;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Spit> findAll() {
        return spitDao.findAll();
    }
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    public void save(Spit spit) {
        // 指定id
        spit.set_id(String.valueOf(idCreater.nextId()));
        spit.setPublishtime(new Date());        // 发布日期
        spit.setVisits(0);      // 浏览量
        spit.setShare(0);       // 分享数
        spit.setThumbup(0);     // 点赞数
        spit.setComment(0);     // 回复数
        spit.setState("1");     // 状态
        // 如果当前的吐槽有父节点，那么父节点加一
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        }
        spitDao.save(spit);
    }
    public void update(Spit spit) {
        spitDao.save(spit);
    }
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    public Page<Spit> findByParentid(String parentid,int page,int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return  spitDao.findByParentid(parentid,pageable);
    }
    public Page<Spit> findBy(int page,int size){
        PageRequest pageable = PageRequest.of(page - 1, size);
        return  spitDao.findAll( pageable);
    }

    public void thumbup(String spitId) {
        // 利用原生mongo命令实现自增  db.spit.update({"_id": "1"}, {$inc: {thumbup: NumberInt(1)}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
