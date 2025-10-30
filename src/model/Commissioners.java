package model;

import java.util.Objects;

public class Commissioners {
    private Double value;
    private Service service;
    private Employee employee;

    public Commissioners(){}
    public Commissioners(Double value, Service service, Employee employee) {
        this.value = value;
        this.service = service;
        this.employee = employee;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Commissioners that)) return false;
        return Objects.equals(value, that.value) && Objects.equals(service, that.service) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, service, employee);
    }

    @Override
    public String toString() {
        return "Commissioners{" +
                "value=" + value +
                ", service=" + service +
                ", employee=" + employee +
                '}';
    }

}
