package com.liaoxin.log.annotations.service;

import com.liaoxin.log.domain.OperationLog;
import com.liaoxin.log.repository.OperationLogRepository;
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
