package org.springultron.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springultron.core.result.ApiResult;

/**
 * @author brucewuu
 * @date 2020/1/9 09:36
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    public ApiResult<Void> loginSuccess() {
        return ApiResult.success();
    }
}
