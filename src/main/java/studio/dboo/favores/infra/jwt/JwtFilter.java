package studio.dboo.favores.infra.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import studio.dboo.favores.modules.accounts.AccountService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    /** Bean Injection */
    private final JwtTokenUtil jwtTokenUtil;

    /** Constant */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private List<String> EXCLUDE_URL = List.of(
                    "/","/view/**", "/static/**", "/css/**", "/js/**", "/images/**", "/node_modules/**", "/favicon.ico"
                    ,"/api/account"
                    ,"/api/account/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request.getRequestURI()" + request.getRequestURI());
        String jwt = subStringPrefix(request);
        jwtTokenUtil.validateJwtToken(jwt);
        if(StringUtils.hasText(jwt)){
            Authentication authentication = jwtTokenUtil.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String subStringPrefix(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(AUTHORIZATION_HEADER) && bearer.startsWith(BEARER_PREFIX)){
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for(String urlPattern : EXCLUDE_URL){
            if(antPathMatcher.match(urlPattern, request.getServletPath())){
                return true;
            }
        }
        return false;
    }
}
