package com.makris.site.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.makris.site.entities.UserPrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JwtUtilsTest {

    private static final Logger logger = LogManager.getLogger();

    @Test
    public void generateToken() {
        String secret = "sFB4xj23-asg";
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String token = JWT.create().withIssuer("auth0")
                .withSubject("1234567890")
                .withClaim("name", "John Doe").sign(algorithm);
        logger.info(token);
    }

    @Test
    public void verifyToken() {
        JwtUtils jwtUtils = new JwtUtils();
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXNlcm5hbWUiOiIxMjMiLCJ0ZWxQaG9uZSI6IjEyMyIsInBvc3RDb2RlIjoiMTUzIiwiYWRkcmVzcyI6IjEyMyIsImVtYWlsIjoiMTIzQG1haWwuY29tIiwiaXNOb25FeHBpcmVkIjoiWSIsImlzQWNjb3VudEVuYWJsZWQiOiJZIn0.KSH8FCtnl2JKQlTKX-BvCZ05na7F0-1_8iHiU1AyIUY";
        Claim claim = jwtUtils.getPayloadClaimByKeyWithToken(token, "username");
        String username = claim.asString();
        assertEquals(username, "123");
    }

    @Test
    public void generateTokenWithUser(){
        JwtUtils jwtUtils = new JwtUtils();
        List<String> roles = new ArrayList<>();
        roles.add("ROLE");
        JwtUserPrincipal user = new JwtUserPrincipal(
                1L, "John Doe", true, roles, true
        );
        String token = jwtUtils.generateToken(user, "subject", 30);
        logger.info(token);

        UserPrincipal newUser = jwtUtils.getUserFromToken(token, false);
        assertEquals(newUser.getUsername(), user.getUsername());
    }
}