package com.acharya.dikshanta.HospitalManagement.identity.model;

import com.acharya.dikshanta.HospitalManagement.common.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private UUID staffId;
    private Role role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Stream<SimpleGrantedAuthority> roleAuthority =
                Stream.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

        Stream<SimpleGrantedAuthority> permissionAuthorities =
                role.getPermissions().stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.name()));

        return Stream.concat(roleAuthority, permissionAuthorities).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
