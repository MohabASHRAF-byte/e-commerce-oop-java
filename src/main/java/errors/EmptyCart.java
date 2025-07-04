package errors;

public class EmptyCart extends Exception {
    public EmptyCart() {
        super("Cart cannot be empty");
    }
}
