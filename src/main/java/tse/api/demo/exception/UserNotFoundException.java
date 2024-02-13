package tse.api.demo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Could not find User " + id);
    }
    public UserNotFoundException(String username) {
        super("Could not find User by " + username);
    }
}