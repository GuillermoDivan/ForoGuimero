package foro.guimero.api.security;

import foro.guimero.api.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import foro.guimero.api.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenRepository tokenRepository,
                          TokenService tokenService, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var subject = tokenService.getSubject(token);
            if (subject != null){ //Cotejando a base de datos.
                var isTokenValid = tokenRepository.findByToken(token)
                        .map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
                if (isTokenValid) {
                    var user = userRepository.findByUsername(subject);
                    var authentication = new UsernamePasswordAuthenticationToken
                            (user, null, user.getAuthorities()); //En caso de devolver contraseña va en "credentials".
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response); //Sigue la cadena de filtros.
    }
}