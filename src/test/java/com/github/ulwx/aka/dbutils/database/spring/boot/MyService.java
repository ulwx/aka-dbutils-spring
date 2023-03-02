package com.github.ulwx.aka.dbutils.database.spring.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);
    private AddressDao addressDao;


    public void init(){
        addressDao.init();
    }
    public MyService(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Transactional(propagation = Propagation.REQUIRED ,transactionManager = "transactionManager")
    public void updateMdb(){

        addressDao.updateMd1(1,"123");

        try {
            addressDao.updateMd2(2, "abc");
        }catch (Exception ex){
            LOGGER.debug(ex+"");
        }
       //MyService方法的内部调用会使用被调用方法上声明的事务失效，所以需要用下面方式调用
        int i=0;


    }



}
