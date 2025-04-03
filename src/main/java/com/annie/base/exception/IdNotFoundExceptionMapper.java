package com.annie.base.exception;

import com.annie.base.response.ResponseModel;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IdNotFoundExceptionMapper implements ExceptionMapper<IdNotFoundException> {

    @Override
    public Response toResponse(IdNotFoundException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ResponseModel.builder().message(e.getMessage()).build())
                .build();
    }
}
