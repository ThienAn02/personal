package com.annie.configuration;
import com.annie.common.Role;
import com.annie.security.TokenProvider;
import com.annie.security.UnauthorizedException;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Slf4j
@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/auth/login"
    );

    @Inject
    private TokenProvider tokenProvider;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        if (isPublicEndpoint(path)) return;

        try {
            String authorizationHeader = requestContext.getHeaderString("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new UnauthorizedException("Missing or invalid Authorization header");
            }

            String token = authorizationHeader.substring("Bearer ".length()).trim();
            Map<String, Object> claims = tokenProvider.validateToken(token);
            Role role = Role.valueOf((String) claims.get("role"));

            if (isForbiddenForRole(path, method, role)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
        } catch (UnauthorizedException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired token")
                    .build());
        }
    }

    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.contains(path);
    }

    private boolean isForbiddenForRole(String path, String method, Role role) {
        if (path.startsWith("/users") && method.equalsIgnoreCase("GET")) {
            return role == Role.PATIENT;
        }
        return false;
    }
}
