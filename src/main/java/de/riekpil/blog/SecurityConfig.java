package de.riekpil.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	public void configG(AuthenticationManagerBuilder auth) throws Exception {
		
	}
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                        .mvcMatchers("/dashboard").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/api/task/**").authenticated()
                        .mvcMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                        .mvcMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .mvcMatchers("/api/users/**").permitAll()
                        .mvcMatchers("/**").authenticated()
                )
                .httpBasic();

    }
}
