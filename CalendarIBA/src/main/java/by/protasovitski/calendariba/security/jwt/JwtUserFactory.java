package by.protasovitski.calendariba.security.jwt;


import by.protasovitski.calendariba.domain.entities.Role;
import by.protasovitski.calendariba.domain.entities.enums.Status;
import by.protasovitski.calendariba.domain.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(new HashSet<>(user.getRoles())),
                user.getStatus().equals(Status.ACTIVE),
                Date.from(user.getLastModifiedDate())
        );
    }

    private static Set<GrantedAuthority> mapToGrantedAuthorities(Set<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName().toString())
                ).collect(Collectors.toSet());
    }
}
