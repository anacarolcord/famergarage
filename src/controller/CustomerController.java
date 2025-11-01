package controller;

import dao.CustomerDAO;
import exception.CustomerNotFoundException;
import exception.RequiredFieldException;
import model.Customer;

import java.util.List;


public class CustomerController {
    private final CustomerDAO customerDAO;

    //injeção de dependencia para uso dos metodos do banco de dados
    public CustomerController(CustomerDAO customerDAO){
        this.customerDAO=customerDAO;
    }

    public void createCustomer(Customer customer){
        if(customer.getCpf().isBlank() || customer.getName().isBlank() || customer.getPhone().isBlank()){
            throw new RequiredFieldException();
        }
         customerDAO.save(customer);
    }

    public List<Customer> listOfCustomers(){
        return customerDAO.findAll();
    }

    public List<Customer> findAllPages(int page, int pageSize){
      return  customerDAO.findAllPages(page, pageSize);
    }

    public Customer findCustomerByCPF(String cpf)throws CustomerNotFoundException{
        return customerDAO.findByCPF(cpf);
    }

    public List<Customer> findByName(String name)throws CustomerNotFoundException{
        return customerDAO.findByName(name);
    }

    public void updateCustomer(Customer customer)throws CustomerNotFoundException {

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

    public void deleteCustomer(String cpf)throws CustomerNotFoundException{
        customerDAO.delete(cpf);
    }
}
