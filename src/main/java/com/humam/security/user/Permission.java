package com.humam.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    FILE_READ("file:read"),
    FILE_UPDATE("file:update"),
    FILE_DELETE("file:delete"),
    FiLE_CREATE("file:create"),


    GROUP_READ("group:read"),
    GROUP_UPDATE("group:update"),
    GROUP_DELETE("group:delete"),
    GROUP_CREATE("group:create"),

    INVITE_READ("invite:read"),
    INVITE_UPDATE("invite:update"),
    INVITE_DELETE("invite:delete"),
    INVITE_CREATE("invite:create"),

    CHECK_READ("check:read"),
    CHECK_UPDATE("check:update"),
    CHECK_DELETE("check:delete"),
    CHECK_CREATE("check:create"),

    LOGS_READ("logs:read"),

    DEMO_READ("demo:read"),
    ;


    @Getter
    private final String permission;
}
