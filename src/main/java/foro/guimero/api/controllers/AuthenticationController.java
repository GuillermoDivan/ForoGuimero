package foro.guimero.api.controllers;
import foro.guimero.api.security.JWTTokenData;
import foro.guimero.api.services.authentication.AuthenticationService;
import jakarta.validation.Valid;
import foro.guimero.api.domain.user.UserAuthenticationData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity authenticateUser(@RequestBody @Valid
                                           UserAuthenticationData userAuthenticationData) {
        try {
            JWTTokenData JWT = authenticationService.authenticate(userAuthenticationData);
            return ResponseEntity.ok(JWT);
        } catch (Exception ex) {
            return null;
        }
    }


}
