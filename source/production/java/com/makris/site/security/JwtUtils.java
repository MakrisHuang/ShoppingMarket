package com.makris.site.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.makris.site.entities.UserPrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";

    private static final String ISSUER = "auth0";
    private static final String CLAIM_KEY_USER_ID = "id";
    private static final String CLAIM_KEY_USRE_NAME = "username";
    private static final String CLAIM_KEY_USER_PASSWORD = "password";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";
    private static final String CLAIM_KEY_ACCOUNT_ENABLED = "isAccountEnabled";
    private static final String CLAIM_KEY_ACCOUNT_NON_EXPIRED = "isNonExpired";
    private static final String CLAIM_KEY_EXPIRATION = "expiration";

    private static final Logger logger = LogManager.getLogger();
    public static final String defaultSecret = "sFB4xj23-asg";

    public static final String KEY_TOKEN_HEADER = "tokenHeader";

    public UserPrincipal getUserFromHttpRequest(HttpServletRequest request, boolean passwordNeeded){
        String headerToken = request.getHeader(JwtUtils.KEY_TOKEN_HEADER);
        return this.getUserFromToken(headerToken, passwordNeeded);
    }

    public UserPrincipal getUserFromToken(String token, boolean passwordNeeded){
        UserPrincipal user = null;
        try{
            final Map<String, Claim> claimMap = this.getPayloadClaimsFromToken(token);
            long userId = claimMap.get(CLAIM_KEY_USER_ID).asLong();
            String username = claimMap.get(CLAIM_KEY_USRE_NAME).asString();
//            List<String> roles = claimMap.get(CLAIM_KEY_AUTHORITIES).asList(String.class);
            String isNonExpired = claimMap.get(CLAIM_KEY_ACCOUNT_NON_EXPIRED).asString();
            String isAccountEnabled = claimMap.get(CLAIM_KEY_ACCOUNT_ENABLED).asString();

            if (passwordNeeded){
                String password = claimMap.get(CLAIM_KEY_USER_PASSWORD).asString();
                return new UserPrincipal(userId, username, password,
                        null, isNonExpired, isAccountEnabled);
            }else {
                return new UserPrincipal(userId, username,
                        null, isNonExpired, isAccountEnabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            Algorithm algorithm = Algorithm.HMAC256(defaultSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
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
