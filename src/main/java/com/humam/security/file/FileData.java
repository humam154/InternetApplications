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

    @Column(nullable = false, length = 55)
    private String name;

    @Lob
    @Column(nullable = false)
    private String file;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
