package main.java.famergarage.enums;

public enum StatusAppointment {
    APPROVED(1,"Appointment approved"),
    CANCELED(2,"Appointment canceled"),
    IN_PROGRESS(3, "Appointment in progress"),
    PAYMENT_PENDING(4, "Appointment with pending payment"),
    COMPLETED(5, "Appointment completed");

    private final int code;
    private final String text;

    StatusAppointment(int code, String text){
        this.code=code;
        this.text=text;
    }

    public int getCode(){
        return code;
    }
    public String getText(){
        return text;
    }

    public static StatusAppointment fromCode(int code){
        for(StatusAppointment statusAppointment : StatusAppointment.values()){
            if(statusAppointment.getCode()==code){
                return statusAppointment;
            }
        }
        throw new IllegalArgumentException("Ivalid code of status"+ code);

    }





}
