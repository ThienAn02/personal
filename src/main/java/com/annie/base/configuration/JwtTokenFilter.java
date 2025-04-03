package com.annie.base.configuration;

import com.annie.base.common.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;


@WebFilter("/*")
public class JwtTokenFilter implements Filter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_ATTRIBUTE = "currentToken";
    private static final String SECRET_KEY = AppConfig.getJWTSecretKey();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(AUTH_HEADER);
        if (token != null && token.startsWith("Bearer ")) {
            httpRequest.setAttribute(TOKEN_ATTRIBUTE, token);
        } else {
            httpRequest.setAttribute(TOKEN_ATTRIBUTE, null);
        }
        chain.doFilter(request, response);
    }

    public static String getCurrentToken(HttpServletRequest request) {
        return (String) request.getAttribute(TOKEN_ATTRIBUTE);
    }

    public static Role getRole(String token) {
        String jwtToken = extractToken(token);
        if (jwtToken == null) return null;

        Claims claims = decodeToken(jwtToken);
        return claims != null ? Role.fromString(claims.get("role", String.class)) : null;
    }
    public static String getEmail(String token) {
        String jwtToken = extractToken(token);
        if (jwtToken == null) return null;

        Claims claims = decodeToken(jwtToken);
        return claims != null ? claims.get("email", String.class) : null;
    }

    public static String getSubject(String token) {
        String jwtToken = extractToken(token);
        if (jwtToken == null) return null;

        Claims claims = decodeToken(jwtToken);
        return claims != null ? claims.getSubject() : null;
    }

    private static String extractToken(String token) {
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

    /**
     * Decodes the specified JWT token and returns its claims.
     *
     * @param token the JWT token to decode.
     * @return the claims contained in the token.
     */
    public static Claims decodeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Do nothing because we don't need to initialize anything
        // This method is required to implement the Filter interface
    }

    @Override
    public void destroy() {
        // Do nothing because we don't need to destroy anything
        // This method is required to implement the Filter interface
    }
}