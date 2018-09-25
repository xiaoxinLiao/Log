package com.liaoxin.log.service.impl;

import com.liaoxin.log.domain.OperationLog;
import com.liaoxin.log.repository.OperationLogRepository;
import com.liaoxin.log.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogRepository logRepository;

    @Override
    public boolean insert(OperationLog operationLog) {
        return logRepository.save(operationLog) != null ? true : false;
    }
}
