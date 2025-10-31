package controller;

import exception.EmployeeNotFoundException;
import model.Employee;

public class EmployeeController {

    private final EmployeeDAO employeeDAO;

    public EmployeeController(EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
    }


    public void saveEmployee(Employee employee){
        employeeDAO.save(employee);
    }

    public void listOfEmployees(){
        employeeDAO.findAll();
    }

    public Employee findEmployeeByID(Employee employee)throws EmployeeNotFoundException{
        return employeeDAO.findById(employee.getIdEmployee());
    }

    public void updateEmployee(Employee employee){
        Employee empployeeExists = employeeDAO.findById(employee.getIdEmployee());

        if(empployeeExists == null){
            throw new EmployeeNotFoundException();
        }

        empployeeExists.setNameEmployee(employee.getNameEmployee());
        empployeeExists.setCommissioners(employee.getCommissioners());
        empployeeExists.setRole(employee.getRole());

        employeeDAO.update(empployeeExists);
    }

}
