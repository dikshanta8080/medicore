package com.acharya.dikshanta.HospitalManagement.common.filter;

import com.acharya.dikshanta.HospitalManagement.common.service.JwtService;
import com.acharya.dikshanta.HospitalManagement.identity.model.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token == null) {
            log.debug("No Bearer token on {} {}, skipping", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.debug("Security context already set, skipping JWT authentication");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.extractUsername(token);
            log.debug("Extracted username '{}' from token", username);

            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userPrincipal)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authenticated '{}' with role '{}'", username, userPrincipal.getRole());
            } else {
                log.warn("Invalid or expired JWT for '{}'", username);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT authentication failed on {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
