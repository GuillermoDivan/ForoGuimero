package foro.guimero.api.services.user;
import foro.guimero.api.domain.user.*;
import foro.guimero.api.repositories.UserRepository;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Override
    public UserShowData registerUser(UserAuthenticationData userData) {
        var user = new User(userData);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new UserShowData(user);
    }

    @Override
    public UserShowData findByUsername(String username) {
        var user = userRepository.findByUsername(username);
        return new UserShowData(user);
    }

    @Override
    public Page<UserShowData> findAll(boolean active, Pageable paging) {
        return this.userRepository.findAllByActive(active, paging).map(UserShowData::new);
    }

    @Override
    public UserShowData findById(Long id) {
        User user = this.userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException());
        return new UserShowData(user);
    }

    @Override
    public UserShowData update(UserUpdateData userUpdateData) {
        User user = new User(userUpdateData);
        User userToUpdate = this.userRepository.findById(user.getId()).orElse(null);

        if (userToUpdate.isActive()){
            if (user.getUsername() != null) {
                userToUpdate.setUsername(user.getUsername());
            }
            if (user.getPassword() != null) {
                userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            this.userRepository.save(userToUpdate);
            authenticationService.revokeAllUserTokens(user);
        }

        return new UserShowData(userToUpdate);
    }

    @Override
    public boolean toggleUser(Long id) {
        User userToToggle = this.userRepository.findById(id).orElse(null);
        userToToggle.setActive(!userToToggle.isActive());
        this.userRepository.save(userToToggle);
        return userToToggle.isActive();
    }
}