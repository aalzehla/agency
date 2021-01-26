package com._3line.gravity.core.security.service.implementation;


import com._3line.gravity.core.security.service.CustomUserDetails;
import com._3line.gravity.core.usermgt.model.AbstractUser;
import com._3line.gravity.core.usermgt.model.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by FortunatusE on 9/22/2018.
 */


public class CustomUserPrincipal implements CustomUserDetails {

    private final AbstractUser user;


    public CustomUserPrincipal(AbstractUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities =  new ArrayList<>();
        getPermissions(user).forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        return authorities;
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    private List<String> getPermissions(AbstractUser user){

        List<String> permissions = new ArrayList<>();

            SystemUser systemUser = (SystemUser) user;
            if(user.getRole()!=null) {
                systemUser.getRole().getPermissions().forEach(permission -> permissions.add(permission.getName()));
            }
        return permissions;
    }

    public AbstractUser getUser() {
        return user;
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
        return this.user.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUserPrincipal)) return false;

        CustomUserPrincipal that = (CustomUserPrincipal) o;

        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }
}
