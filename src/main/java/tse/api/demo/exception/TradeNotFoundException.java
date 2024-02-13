package tse.api.demo.exception;

public class TradeNotFoundException extends RuntimeException {

    public TradeNotFoundException(Long id) {
        super("Could not find Trade " + id);
    }
}