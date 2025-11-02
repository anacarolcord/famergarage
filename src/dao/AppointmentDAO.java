package dao;

import model.Appointment;
import model.Employee;
import model.Vehicle;
import model.Service;
import enums.StatusAppointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private VehicleDAO vehicleDAO;
    private EmployeeDAO employeeDAO;

    public AppointmentDAO() {
        this.vehicleDAO = new VehicleDAO();
        this.employeeDAO = new EmployeeDAO();
    }

    public void save(Appointment appointment) {
        // SQL 1: Insert the main appointment details
        String sqlApp = "INSERT INTO APPOINTMENT (FK_VEHICLE, START_DATE, END_DATE, SERVICE, STATUS, PRICE) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // SQL 2: Insert the associated employees into the junction table
        String sqlComm = "INSERT INTO COMMISSIONERS (FK_APPOINTMENT, FK_EMPLOYEE) VALUES (?, ?)";

        // Database connection and statement objects
        Connection conn = null;
        PreparedStatement pstmApp = null;
        PreparedStatement pstmComm = null;
        ResultSet generatedKeys = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();

            // Turn off auto-commit to start the transaction
            conn.setAutoCommit(false);

            pstmApp = conn.prepareStatement(sqlApp, Statement.RETURN_GENERATED_KEYS);

            // Set the foreign key for the vehicle
            pstmApp.setLong(1, appointment.getVehicle().getIdVehicle());

            // Set the start date
            pstmApp.setTimestamp(2, Timestamp.valueOf(appointment.getStartDate()));

            // Handle nullable end date
            if (appointment.getEndDate() != null) {
                pstmApp.setTimestamp(3, Timestamp.valueOf(appointment.getEndDate()));
            } else {
                pstmApp.setNull(3, java.sql.Types.TIMESTAMP);
            }

            pstmApp.setLong(4, appointment.getService().getIdService());

            // Set Enum Status by its name (String)
            pstmApp.setString(5, appointment.getStatus().name());

            // Handle nullable Price
            if (appointment.getPrice() != null) {
                pstmApp.setDouble(6, appointment.getPrice());
            } else {
                pstmApp.setNull(6, java.sql.Types.DOUBLE);
            }

            // Execute the first insert
            pstmApp.executeUpdate();

            // Retrieve the auto-generated ID (PK) for the new appointment
            generatedKeys = pstmApp.getGeneratedKeys();
            long newAppointmentId = -1;

            if (generatedKeys.next()) {
                newAppointmentId = generatedKeys.getLong(1);
            } else {
                // If we don't get an ID, something is critically wrong.
                throw new SQLException("Failed to create appointment, could not obtain ID.");
            }

            pstmComm = conn.prepareStatement(sqlComm);

            // Iterate over the employee list from the appointment object
            for (Employee emp : appointment.getEmployees()) {
                pstmComm.setLong(1, newAppointmentId);
                pstmComm.setLong(2, emp.getIdEmployee());

                // Add this command to a batch for efficient execution
                pstmComm.addBatch();
            }

            // Execute all commands in the batch at once
            pstmComm.executeBatch();

            // If both inserts are successful, commit the transaction
            conn.commit();
            System.out.println("Appointment saved successfully!");

        } catch (SQLException e) {
            // If any exception occurs, roll back the entire transaction
            System.err.println("Error saving appointment. Initiating rollback...");
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Rollback successful.");
                }
            } catch (SQLException ex) {
                // This is a critical error (e.g., database connection lost during rollback)
                System.err.println("CRITICAL Error: Failed to rollback transaction: " + ex.getMessage());
            }
        } finally {
            try {
                // Close the ResultSet
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                // Close the Appointment statement
                if (pstmApp != null) {
                    pstmApp.close();
                }
                // Close the Commissioners statement
                if (pstmComm != null) {
                    pstmComm.close();
                }
                // Close the connection
                if (conn != null) {
                    // IMPORTANT: Re-enable auto-commit before closing
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    public void update(Appointment appointment) {
        // SQL 1: Update the main appointment table
        String sqlApp = "UPDATE APPOINTMENT SET FK_VEHICLE = ?, START_DATE = ?, END_DATE = ?, "
                + "SERVICE = ?, STATUS = ?, PRICE = ? WHERE ID = ?";

        // SQL 2: Delete old employee associations
        String sqlDelComm = "DELETE FROM COMMISSIONERS WHERE FK_APPOINTMENT = ?";

        // SQL 3: Insert new employee associations
        String sqlInsComm = "INSERT INTO COMMISSIONERS (FK_APPOINTMENT, FK_EMPLOYEE) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstmApp = null;
        PreparedStatement pstmDelComm = null;
        PreparedStatement pstmInsComm = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            pstmApp = conn.prepareStatement(sqlApp);

            pstmApp.setLong(1, appointment.getVehicle().getIdVehicle());
            pstmApp.setTimestamp(2, Timestamp.valueOf(appointment.getStartDate()));

            // Handle nullable end date
            if (appointment.getEndDate() != null) {
                pstmApp.setTimestamp(3, Timestamp.valueOf(appointment.getEndDate()));
            } else {
                pstmApp.setNull(3, java.sql.Types.TIMESTAMP);
            }

            pstmApp.setLong(4, appointment.getService().getIdService());

            pstmApp.setString(5, appointment.getStatus().name());

            if (appointment.getPrice() != null) {
                pstmApp.setDouble(6, appointment.getPrice());
            } else {
                pstmApp.setNull(6, java.sql.Types.DOUBLE);
            }

            pstmApp.setLong(7, appointment.getIdAppointment());

            pstmApp.executeUpdate();

            pstmDelComm = conn.prepareStatement(sqlDelComm);
            pstmDelComm.setLong(1, appointment.getIdAppointment());
            pstmDelComm.executeUpdate();

            pstmInsComm = conn.prepareStatement(sqlInsComm);
            for (Employee emp : appointment.getEmployees()) {
                pstmInsComm.setLong(1, appointment.getIdAppointment());
                pstmInsComm.setLong(2, emp.getIdEmployee());
                pstmInsComm.addBatch();
            }

            pstmInsComm.executeBatch();

            conn.commit();
            System.out.println("Appointment updated successfully!");
        } catch (SQLException e) {
            // Rollback if any error occurs
            System.err.println("Error updating appointment. Initiating rollback...");
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Rollback successful.");
                }
            } catch (SQLException ex) {
                System.err.println("CRITICAL Error: Failed to rollback transaction: " + ex.getMessage());
            }
        } finally {
            // Close all resources
            try {
                if (pstmApp != null) {
                    pstmApp.close();
                }
                if (pstmDelComm != null) {
                    pstmDelComm.close();
                }
                if (pstmInsComm != null) {
                    pstmInsComm.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    public void delete(Appointment appointment) {
        // SQL 1: Delete from the child table (COMMISSIONERS) first
        String sqlComm = "DELETE FROM COMMISSIONERS WHERE FK_APPOINTMENT = ?";

        // SQL 2: Delete from the parent table (APPOINTMENT)
        String sqlApp = "DELETE FROM APPOINTMENT WHERE ID = ?";

        Connection conn = null;
        PreparedStatement pstmComm = null;
        PreparedStatement pstmApp = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            pstmComm = conn.prepareStatement(sqlComm);
            pstmComm.setLong(1, appointment.getIdAppointment());
            pstmComm.executeUpdate();

            pstmApp = conn.prepareStatement(sqlApp);
            pstmApp.setLong(1, appointment.getIdAppointment());
            pstmApp.executeUpdate();

            conn.commit();
            System.out.println("Appointment deleted successfully!");

        } catch (SQLException e) {
            // Rollback if any error occurs
            System.err.println("Error deleting appointment. Initiating rollback...");
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Rollback successful.");
                }
            } catch (SQLException ex) {
                System.err.println("CRITICAL Error: Failed to rollback transaction: " + ex.getMessage());
            }
        } finally {
            // Close all resources
            try {
                if (pstmComm != null) {
                    pstmComm.close();
                }
                if (pstmApp != null) {
                    pstmApp.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    public List<Appointment> findByCustomer(long customerId, int page, int pageSize) {
        // SQL query joins Appointment with Vehicle to filter by customer ID
        String sql = "SELECT A.* FROM APPOINTMENT A " + "JOIN VEHICLE V ON A.FK_VEHICLE = V.ID "
                + "WHERE V.FK_CUSTOMER = ? " + "LIMIT ? OFFSET ?";

        // Object declaration
        List<Appointment> appointments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculate offset
            int offset = (page - 1) * pageSize;

            // Set parameters
            pstm.setLong(1, customerId);
            pstm.setInt(2, pageSize);
            pstm.setInt(3, offset);

            rset = pstm.executeQuery();

            // Iterate through results
            while (rset.next()) {
                Appointment app = new Appointment();

                // Populate appointment data
                app.setIdAppointment(rset.getLong("ID"));
                app.setStartDate(rset.getTimestamp("START_DATE").toLocalDateTime());

                // Handle nullable date
                if (rset.getTimestamp("END_DATE") != null) {
                    app.setEndDate(rset.getTimestamp("END_DATE").toLocalDateTime());
                }

                // Handle nullable price
                if (rset.getObject("PRICE") != null) {
                    app.setPrice(rset.getDouble("PRICE"));
                }

                long serviceId = rset.getLong("SERVICE");
                Service service = new Service();
                service.setIdService(serviceId);
                app.setService(service);

                String statusStr = rset.getString("STATUS");
                app.setStatus(StatusAppointment.valueOf(statusStr));

                // Create shallow Vehicle object
                Vehicle v = new Vehicle();
                v.setIdVehicle(rset.getLong("FK_VEHICLE"));
                app.setVehicle(v);

                // Create empty list for employees (shallow load)
                app.setEmployees(new ArrayList<>());

                appointments.add(app);
            }
        } catch (SQLException e) {
            System.err.println("Error searching for appointments by customer: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        return appointments;
    }

    public List<Appointment> findByEmployeePages(long employeeId, int page, int pageSize) {
        // SQL statement
        String sql = "SELECT A.* FROM APPOINTMENT A " + "JOIN COMMISSIONERS C ON A.ID = C.FK_APPOINTMENT " + "WHERE C.FK_EMPLOYEE = ? " + "LIMIT ? OFFSET ?";

        // List to hold the found appointments
        List<Appointment> appointments = new ArrayList<>();

        // Database connection objects
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculate the offset for pagination
            int offset = (page - 1) * pageSize;

            // Set the query parameters
            pstm.setLong(1, employeeId);
            pstm.setInt(2, pageSize);
            pstm.setInt(3, offset);

            // Execute the query
            rset = pstm.executeQuery();

            // Iterate through the result set
            while (rset.next()) {
                // Create a new Appointment object for each row
                Appointment app = new Appointment();

                // Populate the Appointment object with data from the ResultSet
                app.setIdAppointment(rset.getLong("ID"));
                app.setStartDate(rset.getTimestamp("START_DATE").toLocalDateTime());

                // Handle nullable fields (END_DATE and PRICE)
                if (rset.getTimestamp("END_DATE") != null) {
                    app.setEndDate(rset.getTimestamp("END_DATE").toLocalDateTime());
                }
                if (rset.getObject("PRICE") != null) {
                    app.setPrice(rset.getDouble("PRICE"));
                }

                long serviceId = rset.getLong("SERVICE");
                Service service = new Service();
                service.setIdService(serviceId);
                app.setService(service);

                String statusStr = rset.getString("STATUS");
                app.setStatus(StatusAppointment.valueOf(statusStr));

                // Vehicle object containing only the ID
                Vehicle v = new Vehicle();
                v.setIdVehicle(rset.getLong("FK_VEHICLE"));
                app.setVehicle(v);

                // Employee list (shallow load)
                app.setEmployees(new ArrayList<>());

                // Add the fully populated appointment to the list
                appointments.add(app);
            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for appointments by employee ID: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
        // Return the list of found appointments
        return appointments;
    }

    public List<Appointment> findByVehiclePages(long vehicleId, int page, int pageSize) {
        // SQL statement
        String sql = "SELECT * FROM APPOINTMENT " + "WHERE FK_VEHICLE = ? " + "LIMIT ? OFFSET ?";

        // List to hold the found appointments
        List<Appointment> appointments = new ArrayList<>();

        // Database connection objects
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculate the offset for pagination
            int offset = (page - 1) * pageSize;

            // Set the query parameters
            pstm.setLong(1, vehicleId);
            pstm.setInt(2, pageSize);
            pstm.setInt(3, offset);

            // Execute the query
            rset = pstm.executeQuery();

            // Iterate
            while (rset.next()) {
                // Create a new Appointment object for each row
                Appointment app = new Appointment();

                // Populate the Appointment object
                app.setIdAppointment(rset.getLong("ID"));
                app.setStartDate(rset.getTimestamp("START_DATE").toLocalDateTime());

                // Handle nullable end date
                if (rset.getTimestamp("END_DATE") != null) {
                    app.setEndDate(rset.getTimestamp("END_DATE").toLocalDateTime());
                }
                // Handle nullable price
                if (rset.getObject("PRICE") != null) {
                    app.setPrice(rset.getDouble("PRICE"));
                }

                long serviceId = rset.getLong("SERVICE");
                Service service = new Service();
                service.setIdService(serviceId);
                app.setService(service);

                String statusStr = rset.getString("STATUS");
                app.setStatus(StatusAppointment.valueOf(statusStr));

                // Vehicle object containing only the ID
                Vehicle v = new Vehicle();
                v.setIdVehicle(vehicleId);
                app.setVehicle(v);

                // Employee list (shallow load)
                app.setEmployees(new ArrayList<>());

                // Add the fully populated appointment to the list
                appointments.add(app);
            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for appointments by vehicle ID: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        // Return the list of found appointments
        return appointments;
    }

    public List<Appointment> findAll() {
        // SQL statement to retrieve all appointments
        String sql = "SELECT * FROM APPOINTMENT";

        // List to hold the appointments
        List<Appointment> appointments = new ArrayList<>();

        // Database connection objects
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Execute the query
            rset = pstm.executeQuery();

            // Iterate through the result set
            while (rset.next()) {
                // Create a new Appointment object for each row
                Appointment app = new Appointment();

                // Populate the Appointment object with data from the ResultSet
                app.setIdAppointment(rset.getLong("ID"));
                app.setStartDate(rset.getTimestamp("START_DATE").toLocalDateTime());

                // Handle nullable end date
                if (rset.getTimestamp("END_DATE") != null) {
                    app.setEndDate(rset.getTimestamp("END_DATE").toLocalDateTime());
                }
                // Handle nullable price
                if (rset.getObject("PRICE") != null) {
                    app.setPrice(rset.getDouble("PRICE"));
                }

                long serviceId = rset.getLong("SERVICE");
                Service service = new Service();
                service.setIdService(serviceId);
                app.setService(service);

                String statusStr = rset.getString("STATUS");
                app.setStatus(StatusAppointment.valueOf(statusStr));

                // Vehicle object containing only the ID
                Vehicle v = new Vehicle();
                v.setIdVehicle(rset.getLong("FK_VEHICLE"));
                app.setVehicle(v);

                // Employee list (shallow load)
                app.setEmployees(new ArrayList<>());

                // Add the fully populated appointment to the list
                appointments.add(app);
            }

        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for all appointments: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        // Return the list of found appointments
        return appointments;
    }

    public List<Appointment> findAllPages(int page, int pageSize) {
        // SQL statement to retrieve all appointments with pagination
        String sql = "SELECT * FROM APPOINTMENT LIMIT ? OFFSET ?";

        // List to hold the appointments
        List<Appointment> appointments = new ArrayList<>();

        // Database connection objects
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculate the offset for pagination
            int offset = (page - 1) * pageSize;

            // Set the query parameters
            pstm.setInt(1, pageSize);
            pstm.setInt(2, offset);

            // Execute the query
            rset = pstm.executeQuery();

            // Iterate through the result set
            while (rset.next()) {
                // Create a new Appointment object for each row
                Appointment app = new Appointment();

                // Populate the Appointment object with data from the ResultSet
                app.setIdAppointment(rset.getLong("ID"));
                app.setStartDate(rset.getTimestamp("START_DATE").toLocalDateTime());

                // Handle nullable end date
                if (rset.getTimestamp("END_DATE") != null) {
                    app.setEndDate(rset.getTimestamp("END_DATE").toLocalDateTime());
                }
                // Handle nullable price
                if (rset.getObject("PRICE") != null) {
                    app.setPrice(rset.getDouble("PRICE"));
                }

                long serviceId = rset.getLong("SERVICE");
                Service service = new Service();
                service.setIdService(serviceId);
                app.setService(service);

                String statusStr = rset.getString("STATUS");
                app.setStatus(StatusAppointment.valueOf(statusStr));

                // Vehicle object containing only the ID
                Vehicle v = new Vehicle();
                v.setIdVehicle(rset.getLong("FK_VEHICLE"));
                app.setVehicle(v);

                // Employee list (shallow load)
                app.setEmployees(new ArrayList<>());

                // Add the fully populated appointment to the list
                appointments.add(app);
            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for all appointments with pagination: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        // Return the list of found appointments
        return appointments;
    }

}