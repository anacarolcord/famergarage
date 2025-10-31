package main.java.famergarage.exception;

public class EmployeeNotFoundException extends BusinessException{
    public EmployeeNotFoundException(){
        super("The employee's ID is not found, please verify the information");
    }
}
