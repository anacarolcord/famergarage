package main.java.famergarage.exception;

import main.java.famergarage.model.Customer;

public class CustomerNotFoundException extends BusinessException {

    public CustomerNotFoundException() {
        super("The customer's CPF is not found, please verify the information");
    }




}
