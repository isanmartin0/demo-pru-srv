package com.ev.srv.demoarq;

import javax.servlet.ServletRequest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.evo.api.springboot.error.ApiError;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private CustomAuthenticator customFilter = new CustomAuthenticator();

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.authenticationManager(customFilter);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/swagger-ui.html").permitAll().antMatchers("/api/**").authenticated().and()
				.addFilterAfter(customFilter, AbstractPreAuthenticatedProcessingFilter.class).exceptionHandling()
				.authenticationEntryPoint(new CustomEntryPoint());
	}

	public class CustomEntryPoint implements AuthenticationEntryPoint {
		private MappingJackson2HttpMessageConverter messageConverter;

		public CustomEntryPoint() {
			Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
			builder.serializationInclusion(JsonInclude.Include.NON_NULL);
			messageConverter = new MappingJackson2HttpMessageConverter(builder.build());
		}

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			ApiError error = new ApiError(HttpStatus.UNAUTHORIZED);
			error.setMessage(authException.getMessage());
			error.setPath(request.getServletPath());
			ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
			outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
			messageConverter.write(error, null, outputMessage);
		}
	}

	@Slf4j
	protected static class CustomAuthenticator extends OAuth2AuthenticationManager implements Filter {
		@Override
		public Authentication authenticate(Authentication authentication) {
			try {
				return super.authenticate(authentication);
			} catch (Exception e) {
				return new CustomAuthentication(authentication.getPrincipal(), authentication.getCredentials());
			}
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication instanceof CustomAuthentication) {
				CustomAuthentication custom = (CustomAuthentication) authentication;
				log.info("Found custom authentication: " + custom.getPrincipal());
				authentication.setAuthenticated(true);
			}
			filterChain.doFilter(request, response);
		}

		@Override
		public void destroy() {
		}

		@Override
		public void init(FilterConfig arg0) throws ServletException {
		}

		@SuppressWarnings("serial")
		protected static class CustomAuthentication extends PreAuthenticatedAuthenticationToken {

			public CustomAuthentication(Object principal, Object credentials) {
				super(principal, credentials);
			}

		}

	}

}