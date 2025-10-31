package main.java.famergarage.model;


import main.java.famergarage.enums.StatusAppointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class Appointment {
    private Long idAppointment;
    private LocalDateTime dateTime;
    private Service service;
    private StatusAppointment status;
    private List<Vehicle> vehicles;

    public Appointment(){}
    public Appointment(Long idAppointment, LocalDateTime dateTime, Service service, List<Vehicle> vehicles, StatusAppointment status) {
        this.idAppointment = idAppointment;
        this.dateTime = dateTime;
        this.service = service;
        this.vehicles = vehicles;
        this.status = status;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public StatusAppointment getStatus() {
        return status;
    }

    public void setStatus(StatusAppointment status) {
        this.status = status;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appointment that)) return false;
        return Objects.equals(idAppointment, that.idAppointment) && Objects.equals(dateTime, that.dateTime) && Objects.equals(service, that.service) && status == that.status && Objects.equals(vehicles, that.vehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAppointment, dateTime, service, status, vehicles);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "idAppointment=" + idAppointment +
                ", dateTime=" + dateTime +
                ", service=" + service +
                ", status=" + status +
                ", vehicles=" + vehicles +
                '}';
    }

}
