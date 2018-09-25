package com.liaoxin.log.service;


import com.liaoxin.log.domain.OperationLog;

public interface OperationLogService  {
    /**
     * 保存日志
     * @param operationLog
     * @return true: 保存成功， false：保存失败
     */
    boolean insert(OperationLog operationLog);
}
