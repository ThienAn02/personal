package com.annie.base.filter;

import com.annie.base.contants.ApplicationMessage;
import com.annie.base.exception.ForbiddenException;
import com.annie.base.exception.UnauthorizedException;
import com.annie.base.security.JwtGenerator;
import com.annie.base.security.JwtPayload;

import jakarta.annotation.Priority;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String JWT_EMAIL_PROPERTY = "jwt.email";
    private static final String JWT_ROLE_PROPERTY = "jwt.role";

    @Inject
    private JwtGenerator jwtGenerator;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext reqCtx) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(RolesAllowed.class)) {
            try {
                String authHeader = reqCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                    throw new UnauthorizedException(ApplicationMessage.MISSING_TOKEN_ERROR);
                }

                String token = authHeader.substring(BEARER_PREFIX.length()).trim();

                JwtPayload payload = getPayloadFromToken(token);

                reqCtx.setProperty(JWT_EMAIL_PROPERTY, payload.getEmail());
                reqCtx.setProperty(JWT_ROLE_PROPERTY, payload.getRole().toString());

                RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
                checkAccess(String.valueOf(payload.getRole()), Arrays.asList(rolesAllowed.value()));
            } catch (UnauthorizedException e) {
                throw e;
            } catch (Exception e) {
                throw new UnauthorizedException("Error validating token: " + e.getMessage());
            }
        }
    }

    /**
     * Gets JWT payload from token using JwtGenerator.
     * This method uses the Auth0 JWT implementation.
     */
    public JwtPayload getPayloadFromToken(String token) {
        try {
            return JwtPayload.fromMap(jwtGenerator.validateToken(token));
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(ApplicationMessage.INVALID_TOKEN);
        }
    }

    /**
     * Extracts email from request context that was set during authentication.
     * @param reqCtx The container request context
     * @return The email from the JWT token
     */
    public String getEmailFromContext(ContainerRequestContext reqCtx) {
        Object email = reqCtx.getProperty(JWT_EMAIL_PROPERTY);
        if (email == null) {
            throw new UnauthorizedException("JWT email not found in request context");
        }
        return email.toString();
    }

    /**
     * Checks if the user has the required role.
     */
    private void checkAccess(String userRole, List<String> allowedRoles) {
        if (allowedRoles.contains("*")) {
            return;
        }
        if (!allowedRoles.contains(userRole)) {
            throw new ForbiddenException(ApplicationMessage.UNAUTHORIZED);
        }
    }
    public String getEmailFromToken(String token) throws UnauthorizedException {
        try {
            JwtPayload payload = getPayloadFromToken(token);
            return payload.getEmail();
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token: " + e.getMessage());
        }
    }
}