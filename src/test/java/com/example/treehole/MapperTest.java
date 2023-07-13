package com.example.treehole;

import com.example.treehole.DAO.UserMapper;
import com.example.treehole.Entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//指定运行环境配置类为正式环境
@ContextConfiguration(classes = TreeholeApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser() {
        int rows = (userMapper.updateStatus(150, 1));
        System.out.println(rows);

        rows = (userMapper.updatePassword(150, "123456"));
        System.out.println(rows);
    }
}
