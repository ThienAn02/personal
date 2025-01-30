package com.annie;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("api")
@OpenAPIDefinition(
        info = @Info(title = "Agile Assessment API", version = "1.0", description = "API Documentation"),
        servers = @Server(url = "http://localhost:8080/agile-assessment") // URL with '/agile-assessment'

)
public class AgileAssessment extends Application {
  // Needed to enable Jakarta REST and specify path.
}
