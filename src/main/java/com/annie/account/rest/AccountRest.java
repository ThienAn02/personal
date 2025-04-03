package com.annie.account.rest;

import com.annie.account.dto.AccountDto;
import com.annie.account.dto.AccountUpdateRequest;
import com.annie.account.service.AccountService;
import com.annie.auth.contants.AuthResponseMessage;
import com.annie.base.contants.ApplicationMessage;
import com.annie.base.response.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("Account")
@Tag(name = "", description = "Operations related to account")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class AccountRest {

    @Inject
    private AccountService accountService;

    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get accounts successfully"),
            @ApiResponse(responseCode = "400", description = ApplicationMessage.BAD_REQUEST_ERROR),
            @ApiResponse(responseCode = "401", description = ApplicationMessage.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApplicationMessage.FORBIDDEN),
            @ApiResponse(responseCode = "500", description = ApplicationMessage.INTERNAL_SEVER_ERROR)
    })
    @Operation(summary = "Get All Accounts", description = "Retrieve a list of all accounts")
    public Response getAllAccounts() {
        return Response.ok().entity(new ResponseModel<>(
                AuthResponseMessage.LOGIN_SUCCESSFULLY,
                accountService.findAll()
        )).build();
    }

    @GET
    @Path("/{accountId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "400", description = ApplicationMessage.BAD_REQUEST_ERROR),
            @ApiResponse(responseCode = "401", description = ApplicationMessage.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApplicationMessage.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = ApplicationMessage.INTERNAL_SEVER_ERROR)
    })
    @Operation(summary = "Get an account", description = "Get an account by their ID")
    public Response getAccountById(@PathParam("accountId") Long accountId) {
        AccountDto accountDto = accountService.findById(accountId);
        return Response.ok().entity(new ResponseModel<>("Account retrieved successfully", accountDto)).build();
    }

    @GET
    @Path("/email")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "400", description = ApplicationMessage.BAD_REQUEST_ERROR),
            @ApiResponse(responseCode = "401", description = ApplicationMessage.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApplicationMessage.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = ApplicationMessage.INTERNAL_SEVER_ERROR)
    })
    @Operation(summary = "Get an account", description = "Get an account by their email")
    public Response getAccountByEmail(@QueryParam("email") String email) {
        AccountDto accountDto = accountService.findByEmail(email);
        return Response.ok().entity(new ResponseModel<>("Account retrieved successfully", accountDto)).build();
    }

    @PUT
    @Path("/{accountId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "400", description = ApplicationMessage.BAD_REQUEST_ERROR),
            @ApiResponse(responseCode = "401", description = ApplicationMessage.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApplicationMessage.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = ApplicationMessage.INTERNAL_SEVER_ERROR)
    })
    @Operation(summary = "Update an account", description = "Update an account by their ID")
    public Response updateAccount(@PathParam("accountId") Long accountId, AccountUpdateRequest accountDTO) {
        AccountDto accountDto = accountService.updateAccount(accountId, accountDTO);
        return Response.ok().entity(new ResponseModel<>("Account updated successfully", accountDto)).build();
    }

    @DELETE
    @Path("/{accountId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "400", description = ApplicationMessage.BAD_REQUEST_ERROR),
            @ApiResponse(responseCode = "401", description = ApplicationMessage.UNAUTHORIZED),
            @ApiResponse(responseCode = "403", description = ApplicationMessage.FORBIDDEN),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = ApplicationMessage.INTERNAL_SEVER_ERROR)
    })
    @Operation(summary = "Delete an account", description = "Deletes an account by their ID")
    public Response deleteAccount(@PathParam("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
        return Response.noContent().entity(new ResponseModel<>("Account deleted successfully", null)).build();
    }
}
