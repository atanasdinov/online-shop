package project.exception;

public class CheckoutEmptyCartException extends RuntimeException {
    public CheckoutEmptyCartException(String message) {
        super(message);
    }
}
