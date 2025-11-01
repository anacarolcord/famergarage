package model;

import java.util.Objects;

public class Service {
    private Long idService;
    private String nameService;
    private String description;


    public Service(){}
    public Service(Long idService, String nameService, String description) {
        this.idService = idService;
        this.nameService = nameService;
        this.description = description;

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




    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Service service)) return false;
        return Objects.equals(idService, service.idService) && Objects.equals(nameService, service.nameService) && Objects.equals(description, service.description)  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idService, nameService, description);
    }

    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", nameService='" + nameService + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
