package com.ev.srv.demoarq.api;

import com.evo.api.springboot.exception.EntityNotFoundException;
import com.ev.srv.demoarq.model.User;
import com.ev.srv.demoarq.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import com.evo.api.springboot.exception.ApiException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class UsersController {

	@Autowired
    private UsersService service;

	@ApiOperation(value = "Find pet by ID", nickname = "getUserById", notes = "Returns a single pet", response = User.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = User.class),
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Pet not found") })
    @RequestMapping(value = "/v1/users/{userId}",
        produces = { "application/json", "application/xml" }, 
        method = RequestMethod.GET)
	//@PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUserById(@ApiParam(value = "ID of user to return",required=true) @PathVariable("userId") String userId) throws EntityNotFoundException{
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("authorization " + authentication);
		return new ResponseEntity<User>(service.getUserById(userId), HttpStatus.NOT_IMPLEMENTED);
    }

}
