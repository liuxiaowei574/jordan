package com.nuctech.ls.model.service;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service测试模板类
 * 
 * @author zhaoleifeng
 */
@Transactional
@Service
public class TestService {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Resource
    private SessionFactory sessionFactory;

    /**
     * 通过id查找对应信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    private String findVehicleById(Long id) {
        logger.info("------------------查询开始--------------------");
        // User user = (User) sessionFactory.getCurrentSession().get(User.class,
        // userId);
        logger.info("------------------查询结束--------------------");
        return null;
    }
}
