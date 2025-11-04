package controller;

import dao.ServiceDAO;
import exception.RequiredFieldException;
import exception.ServNotFoundException;
import model.Service;

import javax.management.ServiceNotFoundException;
import java.util.List;

public class ServiceController {
    private final ServiceDAO serviceDAO;

    public ServiceController(ServiceDAO serviceDAO){
        this.serviceDAO=serviceDAO;
    }

    public void save(Service service){
        //tratando execao de campo obrigatorio em branco
        if (service.getNameService().isBlank() || service.getDescription().isBlank()){
            throw new RequiredFieldException();
        }

        serviceDAO.save(service);
    }

    public void delete(Long id) throws ServiceNotFoundException{
        serviceDAO.delete(id);
    }

    public List <Service> findAll(){
        return serviceDAO.getAll();
    }

    public Service findById(Long id) throws ServNotFoundException {
        return serviceDAO.findById(id);
    }

    public void update(Service service){
        Service serviceExists = serviceDAO.findById(service.getIdService());

        if (serviceExists == null){
            throw new ServNotFoundException();
        }

        serviceExists.setNameService(service.getNameService());
        serviceExists.setDescription(service.getDescription());

        serviceDAO.update(serviceExists);
    }
}
