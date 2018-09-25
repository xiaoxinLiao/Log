package com.liaoxin.log.repository;

import com.liaoxin.log.domain.OperationLog;
import org.springframework.data.repository.CrudRepository;

public interface OperationLogRepository extends CrudRepository<OperationLog,Long> {
}
