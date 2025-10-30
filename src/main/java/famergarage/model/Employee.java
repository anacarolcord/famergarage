package famergarage.model;

import java.util.Objects;

public class Employee {
    private Long idEmployee;
    private String nameEmployee;
    private String role;
    private Commissioners commissioners;

    public Employee(){}
    public Employee(Long idEmployee, String nameEmployee, String role, Commissioners commissioners) {
        this.idEmployee = idEmployee;
        this.nameEmployee = nameEmployee;
        this.role = role;
        this.commissioners = commissioners;
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

    public Commissioners getCommissioners() {
        return commissioners;
    }

    public void setCommissioners(Commissioners commissioners) {
        this.commissioners = commissioners;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(idEmployee, employee.idEmployee) && Objects.equals(nameEmployee, employee.nameEmployee) && Objects.equals(role, employee.role) && Objects.equals(commissioners, employee.commissioners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee, nameEmployee, role, commissioners);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", nameEmployee='" + nameEmployee + '\'' +
                ", role='" + role + '\'' +
                ", commissioners=" + commissioners +
                '}';
    }
}
