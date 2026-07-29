package com.acharya.dikshanta.HospitalManagement.common;

import com.acharya.dikshanta.HospitalManagement.identity.model.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class LoggedInUser {

    private LoggedInUser() {
        // Prevent instantiation
    }

    public static UUID getStaffId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new IllegalStateException("Invalid authentication principal.");
        }

        return userPrincipal.getStaffId();
    }

    public static UserPrincipal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found.");
        }

        if (!(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            throw new IllegalStateException("Invalid authentication principal.");
        }

        return userPrincipal;
    }
}