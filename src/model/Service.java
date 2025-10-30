package model;

import java.util.Objects;

public class Service {
    private Long idService;
    private String nameService;
    private String description;
    private Double price;
    private Employee employee;
    private Commissioners commissioners;

    public Service(){}
    public Service(Long idService, String nameService, String description, Double price, Employee employee, Commissioners commissioners) {
        this.idService = idService;
        this.nameService = nameService;
        this.description = description;
        this.price = price;
        this.employee = employee;
        this.commissioners = commissioners;
    }

    public Long getIdService() {
        return idService;
    }

    public void setIdService(Long idService) {
        this.idService = idService;
    }

    public String getNameService() {
        return nameService;
    }

    public void setNameService(String nameService) {
        this.nameService = nameService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Commissioners getCommissioners() {
        return commissioners;
    }

    public void setCommissioners(Commissioners commissioners) {
        this.commissioners = commissioners;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Service service)) return false;
        return Objects.equals(idService, service.idService) && Objects.equals(nameService, service.nameService) && Objects.equals(description, service.description) && Objects.equals(price, service.price) && Objects.equals(employee, service.employee) && Objects.equals(commissioners, service.commissioners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idService, nameService, description, price, employee, commissioners);
    }

    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", nameService='" + nameService + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", employee=" + employee +
                ", commissioners=" + commissioners +
                '}';
    }
}
