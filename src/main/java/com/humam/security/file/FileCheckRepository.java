package com.humam.security.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileCheckRepository extends JpaRepository<FileCheck, Integer> {
    List<FileCheck> findByFileId(Integer fileId);
    List<FileCheck> findByCheckedById(Integer userId);
}
