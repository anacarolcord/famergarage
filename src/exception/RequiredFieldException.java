package exception;

public class RequiredFieldException extends RuntimeException {
    public RequiredFieldException() {
        super("This field is requires, please fill it in");
    }
}
