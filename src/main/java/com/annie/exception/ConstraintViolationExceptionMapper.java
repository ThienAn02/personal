package com.annie.exception;

import com.annie.response.ResponseModel;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        HashMap<String, Object> map = new HashMap<>();
        e.getConstraintViolations()
                .forEach(constraint -> {
                    String field = constraint.getPropertyPath().toString().split("\\.")[2];
                    map.put("error" + field.substring(0, 1).toUpperCase() + field.substring(1), constraint.getMessage());
                });

        return Response.status(Response.Status.BAD_REQUEST).entity(
                ResponseModel.builder().code(400).data(map).build()
        ).build();
    }
}
