package com.liaoxin.log.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "operation_log")
public class OperationLog {

    /**
     * 日志ID
     */
    private Long id;

    /**
     * 操作方IP
     */
    private String ip;

    /**
     * URL
     */
    private String url;

    /**
     * 调用方法名
     */
    private String functionName;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作人
     */
    private String operator;

    public OperationLog() {
    }

    public OperationLog(String ip, String url, String functionName, LocalDateTime operationTime, String operator) {
        this.ip = ip;
        this.url = url;
        this.functionName = functionName;
        this.operationTime = operationTime;
        this.operator = operator;
    }

    public OperationLog(String ip, String url, String functionName, String requestParams, LocalDateTime operationTime, String operator) {
        this.ip = ip;
        this.url = url;
        this.functionName = functionName;
        this.requestParams = requestParams;
        this.operationTime = operationTime;
        this.operator = operator;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ip", length = 50)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "url", length = 100)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "function_name", length = 50)
    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Column(name = "operation_time")
    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    @Column(name = "operator", length = 20)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "request_params")
    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }
}
