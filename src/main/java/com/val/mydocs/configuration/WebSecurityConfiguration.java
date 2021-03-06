package com.val.mydocs.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .csrf()
               .csrfTokenRepository(csrfTokenRepository())
        .and()
               .authorizeRequests()
               .antMatchers("/login", "/register").anonymous()
               .antMatchers("/", "/js/**", "/css/**", "/images/**").permitAll()
               .antMatchers("/users/**").hasAuthority("ADMIN")
               .antMatchers("/subject-types/**").hasAuthority("MODERATOR")
               .antMatchers("/document-types/**").hasAuthority("MODERATOR")
               .anyRequest().authenticated()
       .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/home", true)
       .and()
                .logout()
                .logoutSuccessUrl("/")
       ;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }

}
