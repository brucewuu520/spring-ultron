package org.springultron.security.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springultron.captcha.service.CaptchaService;
import org.springultron.core.utils.WebUtils;

/**
 * 登录验图形证码校验
 *
 * @author brucewuu
 * @date 2023/4/13 11:41
 */
public class CaptchaAuthenticationProvider extends DaoAuthenticationProvider {

    private final CaptchaService captchaService;

    public CaptchaAuthenticationProvider(CaptchaService captchaService) {
        Assert.notNull(captchaService, "captchaService can not be null.");
        this.captchaService = captchaService;
    }

    public CaptchaAuthenticationProvider(CaptchaService captchaService, PasswordEncoder passwordEncoder) {
        super(passwordEncoder);
        Assert.notNull(captchaService, "captchaService can not be null.");
        this.captchaService = captchaService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        HttpServletRequest request = WebUtils.getRequest();
        assert request != null;
        String codeKey = request.getParameter("codeKey");
        if (codeKey == null) {
            codeKey = "";
        }
        String code = request.getParameter("code");
        if (code == null) {
            code = "";
        }
        boolean checkCodeResult = captchaService.validate(codeKey, code);
        if (!checkCodeResult) {
            throw new AuthenticationServiceException("验证码错误");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

}
