package com.annie.exception;

import com.annie.response.ResponseModel;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException e) {

        return Response.status(Response.Status.NOT_FOUND)
                .entity(ResponseModel.builder().code(404).message(e.getMessage()).build())
                .build();
    }
}
