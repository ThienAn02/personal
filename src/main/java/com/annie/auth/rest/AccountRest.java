package com.annie.auth.rest;

import com.annie.auth.dto.LoginRequestDto;
import com.annie.auth.dto.SignUpRequestDto;
import com.annie.auth.service.AuthService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AccountRest {

    @Inject
    private AuthService accountService;

    @POST
    @Path("/signup")
    public Response register(SignUpRequestDto request) {
        return Response.ok(accountService.register(request)).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequestDto request) {
        return Response.ok(accountService.login(request)).build();
    }
}
