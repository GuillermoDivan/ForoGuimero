package foro.guimero.api.domain.user;
import jakarta.validation.constraints.NotNull;

public record UserUpdateData(@NotNull Long id, String email, String password) {
}