package com.example.treehole.DAO;

import org.springframework.stereotype.Repository;

//接口的实现类
//将接口和实现类分开的意义在于可以更换实现框架，不用改数据访问类，比如把Hibernate技术换成MyBatis
@Repository("hibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
