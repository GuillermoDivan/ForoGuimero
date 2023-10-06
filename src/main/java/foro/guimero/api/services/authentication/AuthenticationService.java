package foro.guimero.api.services.authentication;

import foro.guimero.api.domain.user.User;
import foro.guimero.api.domain.user.UserAuthenticationData;
import foro.guimero.api.security.JWTTokenData;

public interface AuthenticationService {

    JWTTokenData authenticate(UserAuthenticationData userAuthenticationData);
    void savedUserToken(User savedUser, String jwtToken);
    void revokeAllUserTokens(User user);
    boolean isSelf(Long userId);
    boolean isAdminOrSelf(Long userId);
    boolean isAdminModOrSelf(Long userId);
}