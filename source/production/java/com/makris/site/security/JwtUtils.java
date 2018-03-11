package com.makris.site.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtUtils {
    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

    private static final String ISSUER = "auth0";
    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_USRE_NAME = "username";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";
    private static final String CLAIM_KEY_ACCOUNT_ENABLED = "enabled";
    private static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "nonExpired";
    private static final String CLAIM_KEY_EXPIRATION = "expiration";

    private static final Logger logger = LogManager.getLogger();
    private static final String defaultSecret = "sFB4xj23-asg";

    public JwtUserPrincipal getUserFromToken(String token){
        JwtUserPrincipal user = null;
        try{
            final Map<String, Claim> claimMap = this.getPayloadClaimsFromToken(token);
            long userId = claimMap.get(CLAIM_KEY_USER_ID).asLong();
            String username = claimMap.get(CLAIM_KEY_USRE_NAME).asString();
            List<String> roles = claimMap.get(CLAIM_KEY_AUTHORITIES).asList(String.class);
            boolean isAccountEnabled = claimMap.get(CLAIM_KEY_ACCOUNT_ENABLED).asBoolean();
            boolean isAccountNonExpired = claimMap.get(CLAIM_KEY_ACCOUNT_NON_EXPIRED).asBoolean();

            user = new JwtUserPrincipal(userId, username, isAccountNonExpired,
                                        roles, isAccountEnabled);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            return user;
        }
    }

    // can be used for Test
    public String generateToken(JwtUserPrincipal user, String subject,
                                long expirationMin){
        try {
            Algorithm algorithm = Algorithm.HMAC256(defaultSecret.getBytes());
            String[] authorities = new String[user.getRoles().size()];
            return JWT.create().withIssuer(ISSUER)
                    .withClaim(CLAIM_KEY_USER_ID, user.getUserId())
                    .withClaim(CLAIM_KEY_USRE_NAME, user.getUsername())
                    .withArrayClaim(CLAIM_KEY_AUTHORITIES, user.getRoles().toArray(authorities))
                    .withClaim(CLAIM_KEY_ACCOUNT_ENABLED, user.isAccountEnabled())
                    .withClaim(CLAIM_KEY_ACCOUNT_NON_EXPIRED, user.isNonExpired())
                    .withSubject(subject)
                    .withIssuedAt(new Date())
                    .withExpiresAt(this.generateExpirationDate(expirationMin))
                    .sign(algorithm);
        }  catch (JWTCreationException ex) {
            logger.info("Fail creating token");
            ex.printStackTrace();
        }
        return null;
    }

    public DecodedJWT verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(defaultSecret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        } catch(JWTVerificationException ex){
            logger.info("Fail verifying token");
            ex.printStackTrace();
            return null;
        }
    }

    public String getHeader(DecodedJWT decodedJWT){
        return decodedJWT.getHeader();
    }

    public String getPayload(DecodedJWT decodedJWT){
        return decodedJWT.getPayload();
    }

    public Map<String, Claim> getPayloadClaimsFromToken(String token){
        Map<String, Claim> claims = null;
        DecodedJWT decodedJWT = verifyToken(token);
        try {
            claims = decodedJWT.getClaims();
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return claims;
    }

    public Claim getPayloadClaimByKeyWithToken(String token, String key){
        DecodedJWT decodedJWT = verifyToken(token);
        Claim claim = null;
        try {
            claim = decodedJWT.getClaim(key);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
        return claim;
    }

    /*
        Time Verification
     */

    public Date getExpirationDateFromToken(String token){
        Claim claim = this.getPayloadClaimByKeyWithToken(token, CLAIM_KEY_EXPIRATION);
        return claim.asDate();
    }

    private Date generateExpirationDate(long expiration){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Date getCreatedDateFromToken(String token){
        DecodedJWT decodedJWT = this.verifyToken(token);
        Date createdDate = null;
        try {
             createdDate = decodedJWT.getIssuedAt();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return createdDate;
    }

    /*
        Refresh token (implementation needed later)
     */
}
