package com.example.todoappmicroserviceuserapi.config.audit;
;
import com.example.todoappmicroserviceuserapi.config.security.AuthUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditingListener implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        AuthUserDetails authUser = (AuthUserDetails) authentication.getPrincipal();
        return Optional.of(authUser.getId());
    }
}
