package com.humam.security.file;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file_checks")
public class FileCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "in_use", nullable = false)
    private Boolean inUse;

    @ManyToOne
    @JoinColumn(name = "checked_by", nullable = false)
    private com.humam.security.user.User checkedBy;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileData file;
}
