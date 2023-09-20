package foro.guimero.api.domain.user;

public record UserAuthenticationData(String username,
                                     String password,String email, Role role) {
    public UserAuthenticationData(User user){
        this(user.getUsername(), user.getPassword(),user.getEmail(),user.getRole());
    }
}