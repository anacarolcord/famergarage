package main.java.famergarage.exception;

public class AppointmentNotFoundException extends BusinessException {
    public AppointmentNotFoundException() {
        super("The appointment's ID is not found, please verify the information");
    }
}
