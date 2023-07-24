package com.example.treehole;

import com.example.treehole.Util.SensitiveFilter;
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
public class SensitiveTests {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "涉黄、赌博还吸毒";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
