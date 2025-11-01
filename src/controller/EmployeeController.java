package controller;

import dao.EmployeeDAO;
import exception.EmployeeNotFoundException;
import exception.RequiredFieldException;
import model.Employee;

import java.lang.module.ResolutionException;
import java.util.List;

public class EmployeeController {

    private final EmployeeDAO employeeDAO;

    public EmployeeController(EmployeeDAO employeeDAO){
        this.employeeDAO = employeeDAO;
    }


    public void saveEmployee(Employee employee){
        if(employee.getCpf().isBlank()){
            throw new RequiredFieldException();
        }
        employeeDAO.save(employee);
    }

    public List<Employee> listOfEmployees(){
       return employeeDAO.findAll();
    }

    public List<Employee> findAllPage(int page, int pageSize){
        return employeeDAO.findAllPage(page,pageSize);
    }

    public Employee findEmployeeByCpf(Employee employee)throws EmployeeNotFoundException{
        return employeeDAO.findByCpf(employee.getCpf());
    }

    public void updateEmployee(Employee employee){
        Employee empployeeExists = employeeDAO.findByCpf(employee.getCpf());

        if(empployeeExists == null){
            throw new EmployeeNotFoundException();
        }

        empployeeExists.setNameEmployee(employee.getNameEmployee());
        empployeeExists.setRole(employee.getRole());
        empployeeExists.setAppointments(employee.getAppointments());

        employeeDAO.update(empployeeExists);
    }

    public void delete(String cpf) throws EmployeeNotFoundException{
        employeeDAO.delete(cpf);
    }

}
