package foro.guimero.api.domain.user;

public record UserShowData(String username) {
    public UserShowData(User user) {
        this(user.getUsername());
    }

}