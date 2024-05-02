package foro.guimero.api.services.user;
import foro.guimero.api.domain.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserShowData registerUser(UserAuthenticationData userData);
    UserShowData findByUsername(String username);
    Page<UserShowData> findAll(boolean active, Pageable paging);
    UserShowData findById(Long id);
    UserShowData update(UserUpdateData userUpdateData);
    boolean toggleUser(Long id);
    boolean delete (Long id);
}