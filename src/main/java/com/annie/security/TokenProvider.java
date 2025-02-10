package com.annie.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static com.annie.security.UnauthorizedException.INVALID_TOKEN;

@ApplicationScoped
public class TokenProvider {
    private static final String SECRET_KEY = "Aavn1234567890~";
    private static final long EXPIRATION_TIME = 86400000L;
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    private static final String ISSUER = "issuer";

    public String generateToken(JwtPayload payload) {
        try {
            return JWT.create()
                    .withPayload(payload.toMap())
                    .withIssuer(ISSUER)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("JWT Creation failed", exception);
        }
    }

    public Map<String, Object> validateToken(String token) throws UnauthorizedException {
        DecodedJWT decodedJWT;
        try {
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedException(INVALID_TOKEN);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("email", decodedJWT.getClaim("email").asString());
        result.put("role", decodedJWT.getClaim("role").asString());
        return result;
    }

}
