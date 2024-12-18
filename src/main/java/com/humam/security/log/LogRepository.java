package com.humam.security.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByFileId(Integer fileId);
    List<Log> findByUserId(Integer userId);
    List<Log> findByAction(String action);
}
