package org.springultron.security.util;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author brucewuu
 * @date 2020/1/9 15:51
 */
public class SecurityUtils {

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
