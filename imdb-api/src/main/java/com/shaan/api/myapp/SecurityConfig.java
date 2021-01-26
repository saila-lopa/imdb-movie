package com.shaan.api.myapp;

import com.shaan.api.myapp.authentication.AuthenticationProvider;
import com.shaan.api.myapp.authentication.TokenAuthenticationService;
import com.shaan.api.myapp.exceptions.ApiSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;

@ComponentScan
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                csrf().disable()
                .anonymous().and()
                .servletApi().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/authentication/login").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/release/notes").permitAll()
                .antMatchers(HttpMethod.GET,"/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
                .anyRequest().access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')").
                and().
                addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class).
                exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws ApiSystemException {
        try {
            return super.authenticationManagerBean();
        } catch (Exception ex) {
            throw new ApiSystemException("Authentication failed");
        }
    }

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws ApiSystemException {
        try {
            auth.authenticationProvider(authenticationProvider);
        } catch (Exception ex) {
            throw new ApiSystemException("Authentication failed");
        }
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(
                null,
                null,
                null, // realm Needed for authenticate button to work
                null, // appName Needed for authenticate button to work
                "accessTokenValue",// apiKeyValue
                ApiKeyVehicle.HEADER,
                "accessToken", //apiKeyName
                ",");
    }

    @Bean
    public StatelessAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        StatelessAuthenticationFilter authenticationTokenFilter = new StatelessAuthenticationFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }
}
