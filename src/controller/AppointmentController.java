package controller;

import exception.AppointmentNotFoundException;
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

    public Appointment findById(Long id) throws AppointmentNotFoundException {
        return appointmentDAO.findById(id);
    }

    public void updateAppointment(Appointment appointment){
        Appointment appointmentExits = findById(appointment.getIdAppointment());

        if(appointmentExits == null){
            throw new AppointmentNotFoundException();
        }
        appointmentExits.setVehicles(appointment.getVehicles());
        appointmentExits.setStartDate(appointment.getStartDate());
        appointmentExits.setEndDate(appointment.getEndDate());
        appointmentExits.setService(appointment.getService());
        appointmentExits.setStatus(appointment.getStatus());
        appointmentExits.setPrice(appointment.getPrice());


        appointmentDAO.update(appointmentExits);
    }

    public void delete(Long id){
        return appointmentDAO.delete(id);
    }
}
