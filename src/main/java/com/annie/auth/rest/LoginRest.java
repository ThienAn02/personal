package com.annie.auth.rest;

import com.annie.auth.dto.UserRequestDto;
import com.annie.auth.entity.User;
import com.annie.auth.service.UserService;
import com.annie.response.ResponseModel;
import com.annie.security.UnauthorizedException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginRest {
    @Inject
    private UserService userService;
    @POST
    @Path("/login")
    public Response login(UserRequestDto loginRequestDTO) {
        try {
            User user = userService.validateCredential(loginRequestDTO);
            String token = userService.generateJWT(user);
            ResponseModel response = ResponseModel.builder()
                    .data(token)
                    .build();

            return Response.ok(response).build();
        } catch (UnauthorizedException e) {
            ResponseModel errorResponse = ResponseModel.builder()
                    .message("Invalid credentials")
                    .build();
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(errorResponse)
                    .build();
        } catch (Exception e) {
            ResponseModel errorResponse = ResponseModel.builder()
                    .message("Internal server error")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }
}
