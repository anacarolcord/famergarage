package main.java.famergarage.controller;

import main.java.famergarage.exception.AppointmentNotFoundException;
import main.java.famergarage.model.Appointment;

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

        appointmentExits.setDateTime(appointment.getDateTime());
        appointmentExits.setService(appointment.getService());
        appointmentExits.setStatus(appointment.getStatus());
        appointmentExits.setVehicles(appointment.getVehicles());

        appointmentDAO.save(appointmentExits);
    }
}
