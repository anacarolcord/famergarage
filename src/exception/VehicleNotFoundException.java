package exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException() {
        super("The vehicle is not found, please verify the information");
    }
}
