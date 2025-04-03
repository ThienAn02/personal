package com.annie.base.exception.mapper;

import com.annie.base.exception.BadRequestException;
import com.annie.base.response.ErrorResponseModel;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException e) {
        ErrorResponseModel.ErrorResponseModelBuilder responseBodyBuilder = ErrorResponseModel.builder().message(e.getMessage());
        if (e.getErrors() != null) {
            responseBodyBuilder.errors(e.getErrors());
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseBodyBuilder.build())
                .build();
    }
}