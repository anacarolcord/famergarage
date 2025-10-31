package controller;

import dao.VehicleDAO;
import exception.CustomerNotFoundException;
import exception.VehicleNotFoundException;
import model.Customer;
import model.Vehicle;

import java.util.List;

public class VehicleController {

    private final VehicleDAO vehicleDAO;
    private final CustomerController customerController;

    public VehicleController(VehicleDAO vehicleDAO, CustomerController customerController){
        this.vehicleDAO=vehicleDAO;
        this.customerController=customerController;
    }

    public void saveVehicle(Vehicle vehicle){
       vehicleDAO.save(vehicle); //retorna junto com o id
    }

    public List<Vehicle> listOfVehicles(int page, int pageSize){
        return vehicleDAO.findAll(page,pageSize);
    }

    //find vehicle by owner!! maybe a list???maybe a unic obj??? idont knowww
    public List <Vehicle> findByCustomer(Customer customer){
        Customer customerExists = customerController.findCustomerByCPF(customer);

        if (customerExists == null){
            throw new CustomerNotFoundException();
        }

        return vehicleDAO.findByCustomer(customerExists.getIdCustomer());
    }

    public Vehicle findByPlate(String plate) throws VehicleNotFoundException{

        return vehicleDAO.findByPlate(plate);
    }

    /*public Vehicle finByModel(String model){

        return vehicleDAO.findByModel(model);
    }

    public  Vehicle findByBrand(String brand){
        return vehicleDAO.findByBrand(brand);
    }

    public Vehicle findById(Long id) throws VehicleNotFoundException{
        return vehicleDAO.findById(id);
    }*/

    public void updateVehicle(Vehicle vehicle){
        Vehicle vehicleExists = vehicleDAO.findByPlate(vehicle.getPlate());

        if (vehicleExists==null){
            throw new VehicleNotFoundException();
        }

        vehicleExists.setModel(vehicle.getModel());
        vehicleExists.setBrand(vehicle.getBrand());
        vehicleExists.setPlate(vehicle.getPlate());
        vehicleExists.setIdCustomer(vehicle.getIdCustomer());

        vehicleDAO.update(vehicleExists);


    }

    public void deleteVehicle(Long id){
        vehicleDAO.delete(id);
    }

}
