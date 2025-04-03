package com.annie;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
@ApplicationPath("api")
@OpenAPIDefinition(
        info = @Info(
                title = "Hospital Management API",
                version = "1.0",
                description = "API Documentation for Hospital Management System"
        ),
        servers = {
                @Server(url = "http://localhost:8080/hospital-management/")
        }
)
public class HospitalManagement extends Application {
}
