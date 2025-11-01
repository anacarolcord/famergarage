package exception;

public class ServNotFoundException extends RuntimeException {
    public ServNotFoundException() {
        super("The service's ID is not found, please verify the information");
    }
}
