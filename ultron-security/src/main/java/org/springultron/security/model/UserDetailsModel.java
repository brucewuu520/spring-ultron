package org.springultron.security.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springultron.core.utils.Lists;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户详情基类 {@link UserDetails}
 *
 * @author brucewuu
 * @date 2020/3/18 10:45
 */
public class UserDetailsModel implements UserDetails, CredentialsContainer {
    private static final long serialVersionUID = 520L;

    private final UserInfo userInfo;
    private final List<UserPermission> permissionList;
    private Set<GrantedAuthority> authorities;

    public UserDetailsModel(@NonNull UserInfo userInfo, @Nullable List<UserPermission> permissionList) {
        this.userInfo = userInfo;
        this.permissionList = permissionList;
        if (Lists.isNotEmpty(permissionList)) {
            this.authorities = Collections.unmodifiableSet(sortAuthorities(permissionList.stream()
                    .filter(permission -> permission.getValue() != null)
                    .map(permission -> {
                        if (null == permission.getType() || permission.getType() == 0) { // 角色
                            return new SimpleGrantedAuthority("ROLE_" + permission.getValue());
                        } else { // 权限
                            return new SimpleGrantedAuthority(permission.getValue());
                        }
                    })
                    .collect(Collectors.toList())));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
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
        return Optional.ofNullable(userInfo.getStatus()).map(status -> status == 1).orElse(Boolean.FALSE);
    }

    @Override
    public void eraseCredentials() {
        userInfo.setPassword(null);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public List<UserPermission> getPermissions() {
        return permissionList;
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserDetailsModel && this.getUsername().equals(((UserDetailsModel) obj).getUsername());
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = 521L;

        private AuthorityComparator() {
        }

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            } else {
                return g1.getAuthority() == null ? 1 : g1.getAuthority().compareTo(g2.getAuthority());
            }
        }
    }
}
