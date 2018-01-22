package com.ev.srv.demoarq.service.impl;

import com.ev.srv.demoarq.service.UsersService;
import com.evo.api.springboot.exception.EntityNotFoundException;
import com.ev.srv.demoarq.model.EE_I_PosicionCliente;
import com.ev.srv.demoarq.model.User;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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
    	
    	RestTemplate restTemplate = new RestTemplate();
    	restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    	restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
    	EE_I_PosicionCliente cliente = EE_I_PosicionCliente.builder().codigoEntidad("0239").idInternoPe(userId)
    			.tipoAcuerdo("0").codigoEstado("9").relacionAcuerdoPersona("01").ecvPersonaAcuerdo("0").build();
    	
    	
    	String fooResourceUrl
    	 // = "https://apiuat.evobanco.com:8443/fullonline/rsi_api/posicion/v2.0.0";
    	="https://52.138.140.198:8443/fullonline/rsi_api/posicion/v2.0.0";
    	try {
    		log.info("Get posicion global");
    	String response
    	  = restTemplate.postForObject(fooResourceUrl,cliente, String.class);
    	log.info("String " + response);
    	} catch (HttpStatusCodeException  ex) {
    		log.error("ERrrow "   + ex.getResponseBodyAsString());
    	}
        if ("0".equalsIgnoreCase(userId)) {
        	throw new EntityNotFoundException(User.class, "userId", userId);
        }
        log.info("Get user by id " + userId);
        return user;
     }

}
