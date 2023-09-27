package foro.guimero.api.controllers;

import foro.guimero.api.domain.user.*;
import foro.guimero.api.services.authentication.AuthenticationService;
import foro.guimero.api.services.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<UserShowData> registerUser(@RequestBody @Valid
                                                     UserAuthenticationData userData) {
        var res = userService.registerUser(userData);
        return ResponseEntity.ok(res);
    }

    @GetMapping
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Page<UserShowData>> findUserList(@PageableDefault(size = 10)
                                                           Pageable paging) {
        return ResponseEntity.ok(userService.findAll(true, paging));
    }

    @GetMapping("/inactive")
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Page<UserShowData>> findInactiveUserList
            (@PageableDefault(size = 10) Pageable paging) {
        return ResponseEntity.ok(userService.findAll(false, paging));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserShowData> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("{username}")
    public ResponseEntity<UserShowData> findByUsername(@PathVariable
                                                       String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<UserShowData> update(@RequestBody @Valid
                                               UserUpdateData userUpdateData) {
        var result = this.userService.update(userUpdateData);
        if (result != null) {
            return ResponseEntity.ok(this.userService.update(userUpdateData));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("{id}")
    @Transactional
    @PostAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Boolean> toggleUser(@PathVariable Long id) {
        boolean toggledUser = userService.toggleUser(id);
        if (!toggledUser) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

}
