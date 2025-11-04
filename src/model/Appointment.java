package model;


import enums.StatusAppointment;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


public class Appointment {
    private Long idAppointment;
    private Vehicle vehicle;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Service service;
    private StatusAppointment status;
    private Double price;
    private List<Employee> employees;


    public Appointment(){}

    public Appointment(Long idAppointment, Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate,
                       Service service, StatusAppointment status, Double price, List<Employee>employees) {
        this.idAppointment = idAppointment;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.service = service;
        this.status = status;
        this.price = price;
        this.employees=employees;
    }

    public Long getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(Long idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appointment that)) return false;
        return Objects.equals(idAppointment, that.idAppointment) && Objects.equals(vehicle, that.vehicle)
                && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate)
                && Objects.equals(service, that.service) && status == that.status && Objects.equals(price, that.price)
                && Objects.equals(employees, that.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAppointment, vehicle, startDate, endDate, service, status, price, employees);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "idAppointment=" + idAppointment +
                ", vehicle=" + vehicle +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", service=" + service +
                ", status=" + status +
                ", price=" + price +
                ", employees=" + employees +
                '}';
    }
}
