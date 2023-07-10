package com.example.treehole.Service;

import com.example.treehole.DAO.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    @PostConstruct//构造方法后调用
    public void init(){
        System.out.println("init");
    }

    public String find(){
        return alphaDao.select();
    }
}
