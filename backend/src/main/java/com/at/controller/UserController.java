package com.at.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.at.dao.response.RestApiError;
import com.at.dao.response.RestApiResponse;
import com.at.entities.User;
import com.at.exception.RecordNotFoundException;
import com.at.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.v1.baseUrl}/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "auth_token")
public class UserController {

  private final UserService userService;

  @Operation(summary = "Get list of all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK", content = {
          @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class))}),
      @ApiResponse(responseCode = "403", description = "Unauthorized. Invalid or missing token", content = {
          @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestApiError.class))})})
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public RestApiResponse<List<User>> getAll() {
    List<User> list = userService.fetchAll();
    list.forEach(u -> u.setPassword(null));
    return new RestApiResponse<>(HttpStatus.OK.value(), list);
  }

  @Operation(summary = "Get a user by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the user", content = {
          @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))}),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = {
          @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestApiError.class))}),
      @ApiResponse(responseCode = "404", description = "User not found", content = {
          @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestApiError.class))})})
  @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
  public RestApiResponse<User> get(
      @Parameter(description = "id of user to be searched") @PathVariable(value = "id") Integer id) {
    User user = userService.findById(id);
    if (user == null) {
      throw new RecordNotFoundException("User not found");
    }

    user.setPassword(null);
    return new RestApiResponse<>(HttpStatus.OK.value(), user);
  }
}
