package studio.dboo.favores.infra.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().anyRequest();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().permitAll();

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                // permitAll
                .antMatchers(HttpMethod.GET, "/api/account/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/account/**").permitAll()
//                .antMatchers(HttpMethod.PUT).permitAll()
//                .antMatchers(HttpMethod.DELETE).permitAll()
                // authenticated
//                .mvcMatchers(HttpMethod.GET).authenticated()
//                .mvcMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT, "/api/account/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/account/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();
        http.formLogin();
        http.httpBasic();
    }
}

