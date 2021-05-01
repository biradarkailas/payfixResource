package com.payfix.packages.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String PUBLISHER = "ROLE_PUBLISHER";

    public static final String SUBSCRIBER = "ROLE_SUBSCRIBER";

    public static final String TEAM_MEMBER = "ROLE_TEAM_MEMBER";

    private AuthoritiesConstants() {
    }
}
