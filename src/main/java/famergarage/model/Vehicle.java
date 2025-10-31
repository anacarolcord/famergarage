package main.java.famergarage.model;

import java.util.Objects;

public class Vehicle {
    private Long idVehicle;
    private String plate;
    private String model;
    private String brand;
    private Customer idCustomer;

    public Vehicle(){}
    public Vehicle(Long idVehicle, String plate, String model, String brand, Customer idCustomer) {
        this.idVehicle = idVehicle;
        this.plate = plate;
        this.model = model;
        this.brand = brand;
        this.idCustomer = idCustomer;
    }


    public Long getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Long idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Customer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Customer idCustomer) {
        this.idCustomer = idCustomer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle vehicle)) return false;
        return Objects.equals(idVehicle, vehicle.idVehicle) && Objects.equals(plate, vehicle.plate) && Objects.equals(model, vehicle.model) && Objects.equals(brand, vehicle.brand) && Objects.equals(idCustomer, vehicle.idCustomer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicle, plate, model, brand, idCustomer);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "idVehicle=" + idVehicle +
                ", plate='" + plate + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", idCustomer=" + idCustomer +
                '}';
    }
}
