package studio.dboo.portfolio.services.account.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.dboo.portfolio.services.account.security.SecurityMessage;

@RestController
@RequestMapping("/v1/sec")
public class SecurityController {

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping("/user")
    public SecurityMessage userSecurityMessage(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("user_auth")
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public SecurityMessage adminSecurityMessage(){
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("admin_auth")
                .build();
    }
}
