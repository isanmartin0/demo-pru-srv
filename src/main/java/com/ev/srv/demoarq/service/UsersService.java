package com.ev.srv.demoarq.service;

import com.evo.api.springboot.exception.EntityNotFoundException;
import com.ev.srv.demoarq.model.User;
import com.evo.api.springboot.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

/**
 * A delegate to be called by the {@link UsersController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
public interface UsersService {


 	/**
 	 * Find pet by ID
 	 * Returns a single pet
     * @return
     */
    User getUserById(String userId)  throws EntityNotFoundException;    
}
