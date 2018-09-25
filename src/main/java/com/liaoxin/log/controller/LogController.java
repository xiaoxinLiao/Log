package com.liaoxin.log.controller;

import com.liaoxin.log.annotations.OperationLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @RequestMapping("/log")
    @OperationLog(functionName = "系统日志")
    public String log(String sign, String appkey) {
        return appkey + ":" + sign;
    }
}
