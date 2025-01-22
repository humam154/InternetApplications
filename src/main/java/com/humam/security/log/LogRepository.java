package com.humam.security.log;


import com.humam.security.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

    @Query("SELECT l FROM Log l WHERE l.logType = :type")
    Page<Log> findByType(@Param("type") LogType type, Pageable pageable);

    @Query("SELECT COUNT(l) FROM Log l WHERE l.logType = 'GROUPS'")
    int countByLogTypeAndGroupInference(Group group);
}
