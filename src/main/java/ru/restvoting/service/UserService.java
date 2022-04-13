package ru.restvoting.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.restvoting.AuthorizedUser;
import ru.restvoting.model.User;
import ru.restvoting.repository.UserRepository;


@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

}