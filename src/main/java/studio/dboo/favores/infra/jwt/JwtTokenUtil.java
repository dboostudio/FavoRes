package studio.dboo.favores.infra.jwt;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenUtil {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String SECRET_KEY = "auth";
    private static final long VALIDITY_TIME_IN_SECOND = 40000;

    public Optional<String> generateJwtToken(Authentication authentication){
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authentication.getAuthorities())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date().getTime()) + VALIDITY_TIME_IN_SECOND*1000))
                .compact();
        return Optional.ofNullable(token);
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public void validateJwtToken(String token){
        Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }


}
