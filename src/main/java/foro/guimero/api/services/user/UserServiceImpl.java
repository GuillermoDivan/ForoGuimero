package foro.guimero.api.services.user;
import foro.guimero.api.domain.user.*;
import foro.guimero.api.repositories.UserRepository;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public UserShowData findById(Long id) throws EntityNotFoundException {
        User user = this.userRepository.findById(id)
        .orElseThrow(EntityNotFoundException::new);
        return new UserShowData(user);
    }

    @Override
    public UserShowData update(UserUpdateData userUpdateData) throws EntityNotFoundException {
        if (authenticationService.isAdminOrSelf(userUpdateData.id())) {
        User userToUpdate = this.userRepository.findById(userUpdateData.id())
                .orElseThrow(EntityNotFoundException::new);
            if (userToUpdate.isActive()) {
                if (userUpdateData.email() != null) {
                    userToUpdate.setEmail(userUpdateData.email());
                }
                if (userUpdateData.password() != null) {
                    userToUpdate.setPassword(passwordEncoder.encode(userUpdateData.password()));
                }
                this.userRepository.save(userToUpdate);
                authenticationService.revokeAllUserTokens(userToUpdate);
            }
            return new UserShowData(userToUpdate);
        }
        return null;
    }

    @Override
    public boolean toggleUser(Long id) throws EntityNotFoundException {
        if (authenticationService.isAdminOrSelf(id)) {
            User userToToggle = this.userRepository.findById(id)
                    .orElseThrow(EntityNotFoundException::new);
            userToToggle.setActive(!userToToggle.isActive());
            if (!userToToggle.isActive()) {
                authenticationService.revokeAllUserTokens(userToToggle);
            }
            this.userRepository.save(userToToggle);
            return userToToggle.isActive();
        }
        return false;
    }

    @Override
    public boolean delete (Long id){
        this.userRepository.deleteById(id);
        return true;
    }
}