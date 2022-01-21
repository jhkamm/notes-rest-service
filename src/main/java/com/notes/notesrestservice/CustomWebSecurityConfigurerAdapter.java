package com.notes.notesrestservice;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
            .passwordEncoder(this.passwordEncoder())
            .username("user1")
            .password("foo")
            .roles("ADMIN")
            .build();

        UserDetails user2 = User.builder()
            .passwordEncoder(this.passwordEncoder())
            .username("user2")
            .password("bar")
            .roles("ADMIN")
            .build();

        UserDetails user3 = User.builder()
            .passwordEncoder(this.passwordEncoder())
            .username("user3")
            .password("baz")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(new ArrayList<UserDetails> () {
            {
                add(user1);
                add(user2);
                add(user3);
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
