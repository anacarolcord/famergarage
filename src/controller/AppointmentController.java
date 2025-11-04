package controller;

import dao.AppointmentDAO;
import exception.AppointmentNotFoundException;
import exception.CustomerNotFoundException;
import exception.EmployeeNotFoundException;
import exception.VehicleNotFoundException;
import model.Appointment;

import java.util.List;

public class AppointmentController {
    private final AppointmentDAO appointmentDAO;

    public AppointmentController(AppointmentDAO appointmentDAO){
        this.appointmentDAO=appointmentDAO;
    }

    public Appointment saveAppointment(Appointment appointment){
        return appointmentDAO.save(appointment);
    }

    public List <Appointment> findAll(){
        return appointmentDAO.findAll();
    }

    public List<Appointment> findAllPages(int page, int pageSize){
        return appointmentDAO.findAllPages(page,pageSize);
    }

    public Appointment findById(Long id) throws AppointmentNotFoundException {
        return appointmentDAO.findById(id);
    }

    public List<Appointment> findByCustomer(Long customerId, int page, int pageSize)throws CustomerNotFoundException {
        return appointmentDAO.findByCustomer(customerId,page,pageSize);
    }

    public List<Appointment> findByEmployeePages(long employeeId, int page, int pageSize)throws EmployeeNotFoundException {
        return appointmentDAO.findByEmployeePages(employeeId,page,pageSize);
    }

    public List<Appointment> findByVehiclePages(Long vehicleId, int page, int pageSize)throws VehicleNotFoundException {
        return appointmentDAO.findByVehiclePages(vehicleId,page,pageSize);

    }

    public void updateAppointment(Appointment appointment){
        Appointment appointmentExits = findById(appointment.getIdAppointment());

        if(appointmentExits == null){
            throw new AppointmentNotFoundException();
        }
        appointmentExits.setVehicle(appointment.getVehicle());
        appointmentExits.setStartDate(appointment.getStartDate());
        appointmentExits.setEndDate(appointment.getEndDate());
        appointmentExits.setService(appointment.getService());
        appointmentExits.setStatus(appointment.getStatus());
        appointmentExits.setPrice(appointment.getPrice());


        appointmentDAO.update(appointmentExits);
    }

    public void delete(Appointment appointment){
         appointmentDAO.delete(appointment);
    }
}
