package com.github.ulwx.aka.dbutils.database.spring.boot;

import com.github.ulwx.aka.dbutils.database.spring.MDataBaseTemplate;
import com.github.ulwx.aka.dbutils.tool.MD;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AddressDao {

    private MDataBaseTemplate mDataBaseTemplate;
    public void init(){
        mDataBaseTemplate.exeScript("","test.sql",
                false,true,";","utf-8");

    }
    public AddressDao(MDataBaseTemplate mDataBaseTemplate){
        this.mDataBaseTemplate=mDataBaseTemplate;

    }


    public List<Address> getListMd(){
        Map<String, Object> mp=new HashMap<>();

        List<Address> list=mDataBaseTemplate.queryList(Address.class,
                MD.md(),mp);
        return list;

    }

    @Transactional(propagation = Propagation.REQUIRED,transactionManager = "transactionManager")
    public void updateMd1(int id,String name){
        Map<String, Object> mp=new HashMap<>();
        mp.put("name",name);
        mp.put("id",id);
        mDataBaseTemplate.update(MD.md(),mp);

    }
    @Transactional(propagation = Propagation.NESTED,transactionManager = "transactionManager")
    public void updateMd2(int id,String name){
        Map<String, Object> mp=new HashMap<>();
        mp.put("name",name);
        mp.put("id",id);
        mDataBaseTemplate.update(MD.md(),mp);
        throw new RuntimeException();

    }
}
