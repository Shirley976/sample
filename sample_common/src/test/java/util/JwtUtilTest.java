package util;

import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JwtUtil.class)
@EnableConfigurationProperties(JwtUtil.class)
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String id;
    private String subject;
    private String roles;
    private String token;
    private Claims claims;

    @Before
    public void setUp(){
        id = "888";
        subject = "Tom";
        roles = "admin";
        token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJUb20iLCJpYXQiOjE2MTM5MzI1MDcsInJvbGVzIjoiYWRtaW4ifQ.d8yLkWDaie_CEegtVq7CLdq3MHft8hbdt9-W7WCIX_Y";
    }

    @Test
    public void createJWT() {
        String tokenCrt = jwtUtil.createJWT(id, subject, roles);
        System.out.println("token = " + tokenCrt);
    }

    @Test
    public void parseJWT() {
        Claims claims = jwtUtil.parseJWT(token);

        Assert.assertEquals("888", claims.getId());
        Assert.assertEquals("Tom", claims.getSubject());
        Assert.assertEquals("admin", claims.get("roles"));
    }
}