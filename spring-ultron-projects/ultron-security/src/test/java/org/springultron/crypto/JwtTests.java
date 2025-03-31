package org.springultron.crypto;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springultron.security.jwt.JwtUtils;

import java.time.Duration;
import java.util.regex.Pattern;

/**
 * @author brucewuu
 * @date 2024/5/6 15:21
 */
public class JwtTests {

    @Test
    public void generateSecret() {
        System.err.println(JwtUtils.generateSecret());
    }

    @Test
    public void generateJwt() {
        final String secret = "ipxnK0SZvdVz9NBAzcmqY6su6/PTsIo4o4HYvX1vqr7l/60wJ8t868msAgQLSJQ58okTn8ew775FwEZGt10IUw==";
        String jwt = JwtUtils.generateToken("admin123", secret, Duration.ofSeconds(10));
        System.err.println(jwt);
        System.err.println("====解码Token=====》");
        System.err.println(JwtUtils.obtainUsername(jwt, secret));
    }

    @Test
    public void encodePwd() {
        String pwd = "c8837b23ff8aaa8a2dde915473ce0991"; // 123321
        System.err.println(new BCryptPasswordEncoder().encode(pwd));
    }

    @Test
    public void test1() {
        // System.err.println("15502162569".substring(5));
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*_,\\\\.\\-]).{8,18}$");
        System.err.println(pattern.matcher("12345678a").matches());
    }

}
