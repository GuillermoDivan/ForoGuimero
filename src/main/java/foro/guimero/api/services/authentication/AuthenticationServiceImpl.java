package foro.guimero.api.services.authentication;
import foro.guimero.api.domain.token.Token;
import foro.guimero.api.domain.user.Role;
import foro.guimero.api.domain.user.UserAuthenticationData;
import foro.guimero.api.domain.user.UserShowData;
import foro.guimero.api.security.JWTTokenData;
import foro.guimero.api.security.TokenService;
import foro.guimero.api.repositories.TokenRepository;
import foro.guimero.api.domain.token.TokenType;
import foro.guimero.api.domain.user.User;
import foro.guimero.api.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(TokenRepository tokenRepository,
                                     AuthenticationManager authenticationManager,
                                     TokenService tokenService, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public JWTTokenData authenticate(UserAuthenticationData userAuthenticationData) {
        var user = userRepository.findByUsername(userAuthenticationData.username());
        if (user.isActive()) {
            Authentication authToken = new UsernamePasswordAuthenticationToken(userAuthenticationData.username(),
                    userAuthenticationData.password());
            var authenticatedUser = authenticationManager.authenticate(authToken);
            var JWTToken = tokenService.generateToken((User) authenticatedUser.getPrincipal());
            revokeAllUserTokens(user);
            savedUserToken(user, JWTToken);
            return new JWTTokenData(JWTToken);
        } else return null;
    }

    public void savedUserToken(User savedUser, String jwtToken) {
        var token = Token.builder().user(savedUser).token(jwtToken)
                .tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public boolean isAdminOrSelf(Long userId) {
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (loggedUser.getId() == userId || loggedUser.getRole() == Role.ADMIN);
    }

    public boolean isAdminModOrSelf(Long userId) {
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (loggedUser.getId() == userId || loggedUser.getRole() == Role.ADMIN || loggedUser.getRole() == Role.MODERATOR);
    }
}