package com.liaoxin.log;

import com.liaoxin.log.annotations.OperationLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogApplicationTests {

    @Test
    public void contextLoads() {
        log("LXX", "nothing");
    }

    @OperationLog(functionName = "日志测试")
    public static void log(String name, String request) {
        System.out.println(name + ":" + request);
    }



}
