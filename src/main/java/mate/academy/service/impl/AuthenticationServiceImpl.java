package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(
                HashUtil.hashPassword(password, userOptional.get().getSalt()))) {
            throw new AuthenticationException("Incorrect email or password");
        }
        return userOptional.get();
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with email " + email + " already exists");
        }
        if (email.isEmpty() || password.isEmpty()) {
            throw new RegistrationException("Email and password can't be empty");
        }
        User user = new User(email, password);
        return userService.add(user);
    }
}
