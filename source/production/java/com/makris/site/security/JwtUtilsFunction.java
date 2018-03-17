package com.makris.site.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.makris.site.entities.UserPrincipal;

import java.util.Date;
import java.util.Map;

public interface JwtUtilsFunction {
    UserPrincipal getUserFromToken(String token, boolean passwordNeeded);
    String generateToken(JwtUserPrincipal user, String subject,
                         long expirationMin);
    DecodedJWT verifyToken(String token);
    String getHeader(DecodedJWT decodedJWT);
    String getPayload(DecodedJWT decodedJWT);
    Map<String, Claim> getPayloadClaimsFromToken(String token);
    Claim getPayloadClaimByKeyWithToken(String token, String key);
    Date getExpirationDateFromToken(String token);
    Date generateExpirationDate(long expiration);
    Date getCreatedDateFromToken(String token);
}
