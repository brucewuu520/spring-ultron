package org.springultron.security.util;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springultron.security.model.UserDetailsModel;

import java.util.Optional;

/**
 * SecurityUtils
 *
 * @author brucewuu
 * @date 2020/1/9 15:51
 */
public class SecurityUtils {

    /**
     * 判断是否是匿名用户
     */
    public static boolean isAnonymousUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> "anonymousUser".equals(authentication.getName())).orElse(Boolean.TRUE);
    }

    /**
     * 获取登录用户信息
     *
     * @return {@link UserDetailsModel}
     */
    public static UserDetailsModel getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsModel) {
            return (UserDetailsModel) principal;
        }
        return null;
    }

    /**
     * 校验用户信息合法性
     */
    public static void checkUserDetails(UserDetails user) {
        if (user == null) {
            throw new UsernameNotFoundException("username or password is invalid");
        } else if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        } else if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
    }

}
