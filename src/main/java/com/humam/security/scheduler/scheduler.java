package com.humam.security.scheduler;

import com.humam.security.backup.Backup;
import com.humam.security.backup.BackupRepository;
import com.humam.security.file.FileData;
import com.humam.security.file.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class scheduler {

    private final BackupRepository backupRepository;
    private final FileRepository fileRepository;

    @Scheduled(cron = "0 0 * */6 * ?")
    public void scheduledBackup(){

        backupRepository.deleteAll();

        List<FileData> files = fileRepository.findAll();

        List<String> filePaths = new ArrayList<>();

        for (FileData file : files) {
            filePaths.add(file.getFilePath());
        }

        Backup backup = Backup.builder()
                .filePath(filePaths)
                .build();
        backupRepository.save(backup);
    }
}
