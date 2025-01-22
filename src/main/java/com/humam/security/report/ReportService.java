package com.humam.security.report;

import com.humam.security.group.Group;
import com.humam.security.group.GroupRepository;
import com.humam.security.groupmember.GroupMemberRepository;
import com.humam.security.log.Log;
import com.humam.security.log.LogRepository;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final LogRepository logRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    public byte[] generateMostActiveUserReport() throws DocumentException {

        List<Log> logs = logRepository.findAll();

        Map<User, Long> userActionCounts = logs.stream()
                .collect(Collectors.groupingBy(Log::getUser, Collectors.counting()));

        User mostActiveUser = userActionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("no logs found"));

        Map<String, Long> actionCounts = logs.stream()
                .filter(log -> log.getUser().equals(mostActiveUser))
                .collect(Collectors.groupingBy(Log::getAction, Collectors.counting()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        document.add(new Paragraph("Most active user report"));

        document.add(new Paragraph("User: " + mostActiveUser.fullName()));

        document.add(new Paragraph("Email: " + mostActiveUser.getEmail()));

        document.add(new Paragraph("Actions: " + actionCounts));

        document.add(new Paragraph("Action Breakdown"));

        actionCounts.forEach((action, count) -> {
            try{
                document.add(new Paragraph(action + ":" + count));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        document.close();

        return outputStream.toByteArray();
    }


    public byte[] generateMostActiveGroupReport() throws DocumentException {
        // Query to find the most active group based on logs and member count
        List<Group> groups = groupRepository.findAll();
        Group mostActiveGroup = groups.stream()
                .max((g1, g2) -> {
                    int g1LogCount = logRepository.countByLogTypeAndGroupInference(g1);
                    int g2LogCount = logRepository.countByLogTypeAndGroupInference(g2);
                    return Integer.compare(g1LogCount, g2LogCount);
                })
                .orElseThrow(() -> new IllegalStateException("No groups found"));

        int memberCount = groupMemberRepository.numOfMembers(mostActiveGroup.getId());
        int logCount = logRepository.countByLogTypeAndGroupInference(mostActiveGroup);

        // Create PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        document.add(new Paragraph("Most Active Group Report"));
        document.add(new Paragraph("Group Name: " + mostActiveGroup.getName()));
        document.add(new Paragraph("Description: " + mostActiveGroup.getDescription()));
        document.add(new Paragraph("Creation Date: " + mostActiveGroup.getCreationDate()));
        document.add(new Paragraph("Number of Members: " + memberCount));
        document.add(new Paragraph("Total Logs: " + logCount));

        document.close();
        return outputStream.toByteArray();
    }
}

