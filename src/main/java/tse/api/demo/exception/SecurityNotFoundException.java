package tse.api.demo.exception;

public class SecurityNotFoundException extends RuntimeException {

    public SecurityNotFoundException(Long id) {
        super("Could not find Security " + id);
    }
}