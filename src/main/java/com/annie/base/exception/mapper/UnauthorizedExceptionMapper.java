package com.annie.base.exception.mapper;

import com.annie.base.exception.UnauthorizedException;
import com.annie.base.response.ResponseModel;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ResponseModel.builder().message(e.getMessage()).build())
                .build();
    }
}
