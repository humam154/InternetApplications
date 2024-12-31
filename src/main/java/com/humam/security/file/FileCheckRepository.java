package com.humam.security.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileCheckRepository extends JpaRepository<FileCheck, Integer> {
    Optional<FileCheck> findById(Integer id);
    List<FileCheck> findByCheckedById(Integer userId);

    @Query("SELECT fc FROM FileCheck fc WHERE fc.fileId.id = :fileId ORDER BY fc.checkDate DESC")
    Optional<FileCheck> findLastCheckByFile(@Param("fileId") Integer fileId);
}
