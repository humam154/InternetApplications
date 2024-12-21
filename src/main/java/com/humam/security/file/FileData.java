package com.humam.security.file;

import com.humam.security.user.User;
import com.humam.security.group.Group;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 512)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // default value = false
    @Column(nullable = false)
    private Boolean accepted = false;


    // default value = false
    @Column(nullable = false)
    private Boolean in_use = false;
}
