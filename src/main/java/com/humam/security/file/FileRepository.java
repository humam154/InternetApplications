package com.humam.security.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByGroupId(Integer groupId);
    List<File> findByCreatedById(Integer userId);
}
