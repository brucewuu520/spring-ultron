package org.springultron.crypto;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springultron.security.jwt.JwtUtils;

import java.time.Duration;

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
        String pwd = "81dc9bdb52d04dc20036dbd8313ed055";
        System.err.println(new BCryptPasswordEncoder().encode(pwd));
    }

}
