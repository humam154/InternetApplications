package com.humam.security.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileData, Integer> {
    List<FileData> findByGroupId(Integer groupId);
    List<FileData> findByCreatedById(Integer userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM FileData f WHERE f.id = :id")
    Optional<FileData> findByIdForUpdate(@Param("id") Integer id);
    
    Optional<FileData> findById(Integer id);
}
