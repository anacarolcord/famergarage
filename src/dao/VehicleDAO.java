package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

import model.Customer;
import model.Vehicle;

public class VehicleDAO {

    // Save vehicle to the database
    public void save(Vehicle vehicle) {
        // SQL statement to insert a new vehicle
        String sql = "INSERT INTO VEHICLE (PLATE, MODEL, BRAND, FK_CUSTOMER) VALUES (?, ?, ?, ?)";

        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Create a connection to the database
            conn = ConnectionFactory.getConnection();
            // Prepare the SQL statement
            pstm = conn.prepareStatement(sql);

            // Set the values for the SQL statement
            pstm.setString(1, vehicle.getPlate());
            pstm.setString(2, vehicle.getModel());
            pstm.setString(3, vehicle.getBrand());
            pstm.setLong(4, vehicle.getIdCustomer().getIdCustomer());

            // Execute the SQL statement
            pstm.executeUpdate();

            System.out.println("Vehicle saved successfully!");

        } catch (SQLException e) {
            // Error handling
            System.err.println("Error saving the vehicle: " + e.getMessage());
        } finally {
            // Close the connections
            try {
                // Close the prepared statement
                if (pstm != null) {
                    pstm.close();
                }
                // Close the connection
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Update vehicle in the database
    public void update(Vehicle vehicle) {
        // Sql statement to update an existing vehicle
        String sql = "UPDATE VEHICLE SET PLATE = ?, MODEL = ?, BRAND = ? WHERE ID = ?";

        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Create a connection to the database
            conn = ConnectionFactory.getConnection();
            // Prepare the SQL statement
            pstm = conn.prepareStatement(sql);

            // Set the values for the SQL statement
            pstm.setString(1, vehicle.getPlate());
            pstm.setString(2, vehicle.getModel());
            pstm.setString(3, vehicle.getBrand());
            // Set the ID for the WHERE clause
            pstm.setLong(4, vehicle.getIdVehicle());

            // Execute the SQL statement
            pstm.executeUpdate();

            System.out.println("Vehicle updated successfully!");

        } catch (SQLException e) {
            // Error handling
            System.err.println("Error updating the vehicle: " + e.getMessage());
        } finally {
            // Close the connections
            try {
                // Close the prepared statement
                if (pstm != null) {
                    pstm.close();
                }
                // Close the connection
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Delete vehicle from the database
    public void delete(Long id) {
        // SQL statement to delete a vehicle
        String sql = "DELETE FROM VEHICLE WHERE ID = ?";
        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;

        // Try to execute the delete operation
        try {
            // Create a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the ID for the WHERE clause
            pstm.setLong(1, id);

            // Execute the SQL statement
            pstm.executeUpdate();
            System.out.println("Vehicle deleted successfully!");
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error deleting the vehicle: " + e.getMessage());
        } finally {
            // Close the connections
            try {
                // Close the prepared statement
                if (pstm != null) {
                    pstm.close();
                }
                // Close the connection
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Find vehicle by plate
    public Vehicle findByPlate(String plate) {
        // sql statement to find a vehicle by plate
        String sql = "SELECT * FROM VEHICLE WHERE PLATE = ?";

        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        Vehicle vehicle = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the plate parameter
            pstm.setString(1, plate);

            // Execute the query
            rset = pstm.executeQuery();

            // Process the result set
            if (rset.next()) {
                vehicle = new Vehicle();
                vehicle.setIdVehicle(rset.getLong("ID"));
                vehicle.setPlate(rset.getString("PLATE"));
                vehicle.setModel(rset.getString("MODEL"));
                vehicle.setBrand(rset.getString("BRAND"));
                // Set the associated customer
                long customerId = rset.getLong("FK_CUSTOMER");
                Customer customer = new Customer();
                customer.setIdCustomer(customerId);
                vehicle.setIdCustomer(customer);
            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for vehicle by plate: " + e.getMessage());
        } finally {
            // Close the connections
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
        return vehicle;
    }

    // Find vehicles by customer CPF
    public List<Vehicle> findByCustomer(long customerId) {
        String sql = "SELECT * FROM VEHICLE WHERE FK_CUSTOMER = ?";

        List<Vehicle> vehicles = new ArrayList<>();

        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the customer ID parameter
            pstm.setLong(1, customerId);

            // Execute the query
            rset = pstm.executeQuery();

            while (rset.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setIdVehicle(rset.getLong("ID"));
                vehicle.setPlate(rset.getString("PLATE"));
                vehicle.setModel(rset.getString("MODEL"));
                vehicle.setBrand(rset.getString("BRAND"));
                // Set the associated customer (by CPF)
                Customer customer = new Customer();
                customer.setIdCustomer(customerId);
                vehicle.setIdCustomer(customer);

                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for vehicles by customer ID: " + e.getMessage());
        } finally {
            // Close the connections
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
        return vehicles;
    }

    // Find all vehicles with pagination
    public List<Vehicle> findAll(int page, int pageSize) {
        // SQL statement to retrieve all vehicles with pagination
        String sql = "SELECT * FROM VEHICLE LIMIT ? OFFSET ?";

        // List to hold the vehicles
        List<Vehicle> vehicles = new ArrayList<>();

        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculate the offset
            int offset = (page - 1) * pageSize;

            // Set the pagination parameters
            pstm.setInt(1, pageSize);
            pstm.setInt(2, offset);

            // Execute the query
            rset = pstm.executeQuery();

            // Iterate through the result set and populate the vehicles list
            while (rset.next()) {
                Vehicle vehicle = new Vehicle();

                vehicle.setIdVehicle(rset.getLong("ID"));
                vehicle.setPlate(rset.getString("PLATE"));
                vehicle.setModel(rset.getString("MODEL"));
                vehicle.setBrand(rset.getString("BRAND"));

                // Set the associated customer
                long customerId = rset.getLong("FK_CUSTOMER");
                Customer customer = new Customer();
                customer.setIdCustomer(customerId);
                vehicle.setIdCustomer(customer);

                // Add the vehicle to the list
                vehicles.add(vehicle);

            }
        } catch (SQLException e) {
            // Error handling
            System.err.println("Error searching for vehicles in paginated format: " + e.getMessage());
        } finally {
            // Close the connections
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
        return vehicles;
    }

}
