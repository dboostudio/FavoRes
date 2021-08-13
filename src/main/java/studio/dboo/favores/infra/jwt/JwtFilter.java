package studio.dboo.favores.infra.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    /** Bean Injection */
    private AccountService accountService;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /** Constant */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private List<String> EXCLUDE_URL = List.of(
                    "/","/view/**", "/static/**", "/css/**", "/js/**", "/images/**", "/node_modules/**"
                    ,"/api/account"
                    ,"/api/account/authenticate");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = subStringPrefix(request);
        jwtTokenUtil.validateJwtToken(jwt);
        if(StringUtils.hasText(jwt)){
            String username = jwtTokenUtil.getUsernameFromJwtToken(jwt);

            UserDetails userDetails = accountService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String subStringPrefix(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(AUTHORIZATION_HEADER) && bearer.startsWith(BEARER_PREFIX)){
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
