package com.yarou.book.config;

import com.yarou.book.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


//Bech tjibli el sayed li ekher wehed aaml modif aal book , wala choun createh
public class ApplicationAuditAware implements AuditorAware <Integer>{


    @Override
    public Optional<Integer> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication ==null ||
            !authentication.isAuthenticated() ||
                // user ama mahoush authenticated -> anonymous , kif compte invité fil chess / pubg
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        User userPrincipal = (User) authentication.getPrincipal();  // behs traja3 l infos mtaa l users -> eli howa l id
        return Optional.ofNullable(userPrincipal.getId());  // ofNuallable in case l user null
    }
}
