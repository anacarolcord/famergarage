package model;


import enums.StatusAppointment;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class Appointment {
    private Long idAppointment;
    private List<Vehicle> vehicles;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Service service;
    private StatusAppointment status;
    private DecimalFormat price;


    public Appointment(){}

    public Appointment(Long idAppointment, List<Vehicle> vehicles, LocalDateTime startDate, LocalDateTime endDate, Service service, StatusAppointment status, DecimalFormat price) {
        this.idAppointment = idAppointment;
        this.vehicles = vehicles;
        this.startDate = startDate;
        this.endDate = endDate;
        this.service = service;
        this.status = status;
        this.price = price;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public DecimalFormat getPrice() {
        return price;
    }

    public void setPrice(DecimalFormat price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appointment that)) return false;
        return Objects.equals(idAppointment, that.idAppointment) && Objects.equals(vehicles, that.vehicles) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(service, that.service) && status == that.status && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAppointment, vehicles, startDate, endDate, service, status, price);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "idAppointment=" + idAppointment +
                ", vehicles=" + vehicles +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", service=" + service +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
