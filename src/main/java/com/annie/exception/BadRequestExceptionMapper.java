package com.annie.exception;

import com.annie.response.ResponseModel;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException e) {

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ResponseModel.builder().code(400).message(e.getMessage()).build())
                .build();
    }
}
