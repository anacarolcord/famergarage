package controller;

import exception.RequiredFieldException;
import model.Service;

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
}
