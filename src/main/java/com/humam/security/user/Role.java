package com.humam.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.humam.security.user.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(
            Set.of(
                    FILE_READ,
                    FILE_UPDATE,
                    FILE_DELETE,
                    FiLE_CREATE,
                    GROUP_READ,
                    GROUP_UPDATE,
                    GROUP_DELETE,
                    GROUP_CREATE,
                    INVITE_READ,
                    INVITE_UPDATE,
                    INVITE_DELETE,
                    INVITE_CREATE,
                    CHECK_READ,
                    CHECK_UPDATE,
                    CHECK_DELETE,
                    CHECK_CREATE
            )
    ),
    ADMIN(
            Set.of(
                    FILE_READ,
                    FILE_UPDATE,
                    FILE_DELETE,
                    FiLE_CREATE,
                    GROUP_READ,
                    GROUP_UPDATE,
                    GROUP_DELETE,
                    GROUP_CREATE,
                    INVITE_READ,
                    INVITE_UPDATE,
                    INVITE_DELETE,
                    INVITE_CREATE,
                    CHECK_READ,
                    CHECK_UPDATE,
                    CHECK_DELETE,
                    CHECK_CREATE,
                    LOGS_READ,

                    DEMO_READ
            )
    )
    ;

    @Getter
    private final Set<Permission> permission;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
