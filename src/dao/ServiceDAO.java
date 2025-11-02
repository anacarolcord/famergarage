package dao;

import model.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    // Save Service
    public void save(Service service) {
        String sql = "INSERT INTO SERVICES (SERVICE, DESCRIPTION) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the values for the prepared statement
            pstm.setString(1, service.getNameService());
            pstm.setString(2, service.getDescription());

            pstm.executeUpdate();
            System.out.println("Service saved successfully!");

        } catch (SQLException e) {
            System.err.println("Error saving service: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Update Service by id
    public void update(Service service) {
        String sql = "UPDATE SERVICES SET SERVICE = ?, DESCRIPTION = ? WHERE ID = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the new values
            pstm.setString(1, service.getNameService());
            pstm.setString(2, service.getDescription());
            // Set the ID for the WHERE clause
            pstm.setLong(3, service.getIdService());

            pstm.executeUpdate();
            System.out.println("Service updated successfully!");

        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Delete a Service by id
    public void delete(long id) {
        String sql = "DELETE FROM SERVICES WHERE ID = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the ID for the WHERE clause
            pstm.setLong(1, id);

            pstm.executeUpdate();
            System.out.println("Service deleted successfully!");

        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Find a Service by Id  
    public Service findById(long id) {
        String sql = "SELECT * FROM SERVICES WHERE ID = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        Service service = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);

            rset = pstm.executeQuery();

            // Check if a result was found
            if (rset.next()) {
                service = new Service();
                service.setIdService(rset.getLong("ID"));
                service.setNameService(rset.getString("SERVICE"));
                service.setDescription(rset.getString("DESCRIPTION"));
            }

        } catch (SQLException e) {
            System.err.println("Error finding service by ID: " + e.getMessage());
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
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        return service;
    }

    // Find All Services
    public List<Service> getAll() {
        String sql = "SELECT * FROM SERVICES";

        List<Service> services = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            // Iterate through all results
            while (rset.next()) {
                Service service = new Service();
                service.setIdService(rset.getLong("ID"));
                service.setNameService(rset.getString("SERVICE"));
                service.setDescription(rset.getString("DESCRIPTION"));

                services.add(service);
            }

        } catch (SQLException e) {
            System.err.println("Error finding all services: " + e.getMessage());
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
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        return services;
    }

}