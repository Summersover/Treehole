package com.example.treehole;

import com.example.treehole.DAO.AlphaDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
//指定运行环境配置类为正式环境
@ContextConfiguration(classes = TreeholeApplication.class)
class TreeholeApplicationTests implements ApplicationContextAware {
    //该接口可以模拟正式环境创建spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    //测试容器是否存在
    public void testApplicationContext() {
        System.out.println(applicationContext);
        //可以通过类或者Repository名来获取
        AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
        System.out.println(alphaDao.select());
    }

    @Test
    public void testBeanConfig(){
        SimpleDateFormat simpleDateFormat=
                applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
    }

    @Autowired  //依赖注入，直接使用bean，就不用像上面一样用getBean()方法
    @Qualifier("hibernate")  //通过这个注解可以指定bean的容器
    private AlphaDao alphaDao;
}
