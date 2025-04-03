package com.annie.base.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseModel {
    private String message;
    private Map<String, String> errors;

    public static class ResponseModelBuilder {
        private String message;
        private Map<String, String> errors;

        public ResponseModelBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseModelBuilder errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public ErrorResponseModel build() {
            ErrorResponseModel responseBody = new ErrorResponseModel();
            responseBody.message = this.message;
            responseBody.errors = this.errors;
            return responseBody;
        }
    }
}
