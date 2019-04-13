package com.val.mydocs.web.helpers;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserHelper {
    public static boolean isUserLogged() {
        try {
            return !SecurityContextHolder.getContext().getAuthentication()
                    .getName().equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }
}
