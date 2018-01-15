package com.ev.srv.demoarq.service.impl;

import com.ev.srv.demoarq.service.UsersService;
import com.evo.api.springboot.exception.EntityNotFoundException;
import com.ev.srv.demoarq.model.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * A delegate to be called by the {@link UsersController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */

@Service
@Slf4j
public class UsersServiceImpl implements UsersService{

    @Override
    public User getUserById(String userId) throws EntityNotFoundException{
    	User user = User.builder().id(userId).build();
        if ("0".equalsIgnoreCase(userId)) {
        	throw new EntityNotFoundException(User.class, "userId", userId);
        }
        log.info("Get user by id " + userId);
        return user;
     }

}
