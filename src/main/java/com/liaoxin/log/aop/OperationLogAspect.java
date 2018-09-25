package com.liaoxin.log.aop;

import com.alibaba.fastjson.JSON;
import com.liaoxin.log.annotations.OperationLog;
import com.liaoxin.log.service.OperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面类
 */
@Aspect
@Component
// 使用 cglib 代理
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class OperationLogAspect {

    private Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogService logService;

    @Pointcut("@annotation(com.liaoxin.log.annotations.OperationLog)")
    public void pointCut() {
    }

    /**
     * 方法执行后拦截
     *
     * @param joinPoint
     */
    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) {
        try {
            Class targetClass = joinPoint.getTarget().getClass();
            Logger logger = LoggerFactory.getLogger(targetClass);

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = methodSignature.getMethod();

            Class<?>[] parameterTypes = methodSignature.getParameterTypes();
            Method implMethod = targetClass.getMethod(targetMethod.getName(), parameterTypes);

            //获取调用方法
            OperationLog methodLogConfig = implMethod.getAnnotation(OperationLog.class);

            //设置操作日志
            String functionName = "";
            if (methodLogConfig != null) {
                functionName = methodLogConfig.functionName();
            }

            //操作日志记录
            Map<String, Object> params = getMethodParams(joinPoint);
            String jsonParams = JSON.toJSONString(params);
            if (logger.isDebugEnabled()) {
                logger.debug("{}接口方法{}的参数{}", new Object[]{functionName, targetMethod.getName(), jsonParams});
            }
            //写入日志
            com.liaoxin.log.domain.OperationLog operationLog;
            //获取IP
            String ip = getIP();
            //获取URL
            String url = getURL();
            operationLog = new com.liaoxin.log.domain.OperationLog(ip, url, functionName, jsonParams, LocalDateTime.now(), "某某");
            boolean result = logService.insert(operationLog);

            if (result && logger.isDebugEnabled()) {
                logger.debug("切面记录功能操作日志成功！");
            } else {
                logger.info("result={},写入日志失败", result);
            }

        } catch (Exception e) {
            logger.error("切面记录功能操作日志失败:{}", e);
        }
    }


    /**
     * 获取方法参数
     *
     * @param joinPoint
     * @return
     */
    private Map<String, Object> getMethodParams(JoinPoint joinPoint) {
        Object[] paramValues = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] paramNames = codeSignature.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        int i = 0;
        for (String paramName : paramNames) {
            if ("response".equals(paramName) || "request".equals(paramName) || paramName.startsWith("model")) {
                i++;
                continue;
            }
            params.put(paramName, paramValues[i++]);
        }
        return params;
    }

    /**
     * 获取IP
     *
     * @return
     */
    private String getIP() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return getHost(attributes.getRequest());
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static final String getHost(HttpServletRequest request) {
        final String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        if ("127.0.0.1".equals(ip)) {
            InetAddress inet = null;
            try { // 根据网卡取本机配置的IP
                inet = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if (inet != null) {
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获取访问的URL
     *
     * @return
     */
    private String getURL() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getRequestURL().toString();
    }

}
