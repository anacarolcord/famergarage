package model;

import java.util.List;
import java.util.Objects;

public class Employee {
    private Long idEmployee;
    private String nameEmployee;
    private String role;
    private List<Appointment> appointments;

    public Employee(){}
    public Employee(Long idEmployee, String nameEmployee, String role, List<Appointment> appointments) {
        this.idEmployee = idEmployee;
        this.nameEmployee = nameEmployee;
        this.role = role;
        this.appointments=appointments;

    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(idEmployee, employee.idEmployee) && Objects.equals(nameEmployee, employee.nameEmployee) && Objects.equals(role, employee.role) && Objects.equals(appointments, employee.appointments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee, nameEmployee, role, appointments);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", nameEmployee='" + nameEmployee + '\'' +
                ", role='" + role + '\'' +
                ", appointments=" + appointments +
                '}';
    }
}
