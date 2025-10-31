package exception;

public class CustomerNotFoundException extends BusinessException {

    public CustomerNotFoundException() {
        super("The customer's CPF is not found, please verify the information");
    }




}
