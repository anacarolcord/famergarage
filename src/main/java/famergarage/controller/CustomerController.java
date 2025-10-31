package main.java.famergarage.controller;

import main.java.famergarage.dao.CustomerDAO;
import main.java.famergarage.exception.CustomerNotFoundException;
import main.java.famergarage.model.Customer;


public class CustomerController {
    private final CustomerDAO customerDAO;

    //injeção de dependencia para uso dos metodos do banco de dados
    public CustomerController(CustomerDAO customerDAO){
        this.customerDAO=customerDAO;
    }

    public void createCustomer(Customer customer){
         customerDAO.save(customer);
    }

    public void listOfCustomers(){
        customerDAO.findAll();
    }

    public Customer findCustomerByCPF(Customer customer)throws CustomerNotFoundException{
        return customerDAO.findByCPF(customer.getCpf());
    }

    public void updateCustomer(Customer customer) {

            Customer customerExists = customerDAO.findByCPF(customer.getCpf());

            if (customerExists == null){
                throw new CustomerNotFoundException();
            }

            customerExists.setCpf(customer.getCpf());
            customerExists.setEmail(customer.getEmail());
            customerExists.setName(customer.getName());
            customerExists.setPhone(customer.getPhone());

            customerDAO.update(customerExists);

    }

    public void deleteCustomer(Long id)throws CustomerNotFoundException{
        customerDAO.delete(id);
    }
}
