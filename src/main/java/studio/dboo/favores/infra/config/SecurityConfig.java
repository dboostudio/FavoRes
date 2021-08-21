package studio.dboo.favores.infra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import studio.dboo.favores.infra.jwt.JwtAccessDeniedHandler;
import studio.dboo.favores.infra.jwt.JwtAuthenticationEntryPoint;
import studio.dboo.favores.infra.jwt.JwtFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** Bean injection */
    private final PasswordEncoder passwordEncoder;
    //https://sup2is.github.io/2020/03/05/spring-security-login-with-jwt.html 참고
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtFilter jwtFilter;
    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    /**
     * SecurityConfig내에서 등록한 Bean의 경우 순환참조가 발생하는 경우가 많다.
     * 여기서 등록한 빈을 주입받아 사용하는 경우에는 setter주입으로 사용하는것이 좋다.
     * ex) AccountService 클래스의 AuthenticationManager를 참고.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        web.ignoring().antMatchers("/node_modules/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Jwt 적용
        http.csrf().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler);

        // 세션 사용하지 않음
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 접근경로별 권한설정
        http.authorizeRequests()
                //view
                .antMatchers("/").permitAll()
                .antMatchers("/view/**").permitAll()
                //api
                .antMatchers(HttpMethod.PUT, "/api/account").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/account").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/api/account/login", "api/account/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/api/account").permitAll()
                .antMatchers(HttpMethod.POST, "/api/account").permitAll()

                .antMatchers("api/account/authenticate").permitAll()
                .anyRequest().authenticated();

        //폼 로그인 설정
        http.formLogin()
            .loginPage("/login").permitAll();

        //로그아웃 후 리다이렉션 설정
        http.logout()
                .logoutSuccessUrl("/");
    }
}

