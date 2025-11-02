package dao;

import model.Authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationDAO {

    public Authentication findByUsername(String user) {        
        // SQL query to find a user by their username
        String sql = "SELECT * FROM AUTHENTICATION WHERE USER = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        Authentication authData = null;

        try {
            // Establish a connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the username parameter for the WHERE clause
            pstm.setString(1, user);

            // Execute the query
            rset = pstm.executeQuery();

            // If a result is found, populate the Authentication object
            if (rset.next()) {
                authData = new Authentication();
                authData.setId(rset.getLong("ID"));
                authData.setUser(rset.getString("USER"));
                authData.setPassword(rset.getString("PASSWORD"));
                authData.setAccess(rset.getString("ACCESS"));
            }

        } catch (SQLException e) {
            // Error handling
            System.err.println("Error finding user: " + e.getMessage());
        } finally {
            // Close all resources
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Error handling for closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }

        // Return the found user data or null
        return authData;
    }
}