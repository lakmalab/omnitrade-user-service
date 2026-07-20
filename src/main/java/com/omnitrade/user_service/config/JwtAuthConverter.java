package com.omnitrade.user_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts a Keycloak JWT into a Spring Security Authentication token.
 *
 * Keycloak stores realm-level roles under the claim:
 *   realm_access.roles = ["ADMIN", "USER", ...]
 *
 * This converter extracts those roles and maps them to Spring Security
 * GrantedAuthority objects with the standard "ROLE_" prefix, so that
 * annotations like @PreAuthorize("hasRole('ADMIN')") work as expected.
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> defaultAuthorities = defaultConverter.convert(jwt);

        Collection<GrantedAuthority> realmRoles = extractRealmRoles(jwt);

        Collection<GrantedAuthority> allAuthorities = Stream
                .concat(defaultAuthorities.stream(), realmRoles.stream())
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, allAuthorities, jwt.getSubject());
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return List.of();
        }
        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}
