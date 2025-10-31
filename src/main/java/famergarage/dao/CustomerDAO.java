package main.java.famergarage.dao;

import main.java.famergarage.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Save customer to the database 
    public void save(Customer customer) {
        // SQL statement to insert a new customer
        String sql = "INSERT INTO CUSTOMER (NAME, PHONE, EMAIL) VALUES (?, ?, ?)";

        // Objects for database connection and statement
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Create a connection and prepare the SQL statement
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the values for the SQL statement
            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getPhone());
            pstm.setString(3, customer.getEmail());

            // Execute the SQL statement
            pstm.executeUpdate();

            System.out.println("Cliente salvo com sucesso!");
        } catch (SQLException e) {
            // Handle errors during save operation
            System.err.println("Error saving customer: " + e.getMessage());
        } finally {
            // Close the connections
            try {
                // Closing the prepared statement
                if (pstm != null)
                    pstm.close();
                // Closing the connection
                if (conn != null)
                    conn.close();

            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Find customer by CPF
    public Customer findByCPF(String cpf) {
        // SQL Select statement
        String sql = "SELECT * FROM CUSTOMER WHERE CPF = ?";

        // Objects for database connection, statement, and result set
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        Customer customer = null;

        try {
            // Establishing the connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Setting the value for the query parameter
            pstm.setString(1, cpf);

            // Executing the query and obtaining the result
            rset = pstm.executeQuery();

            // Verifying if a customer was found
            if (rset.next()) {
                customer = new Customer();
                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));
                customer.setCpf(rset.getString("CPF"));
            }
        } catch (SQLException e) {
            // Handle errors during search operation
            System.err.println("Error searching for customer by CPF: " + e.getMessage());
        } finally {
            try {
                // Closing the result set
                if (rset != null)
                    rset.close();
                // Closing the prepared statement
                if (pstm != null)
                    pstm.close();
                // Closing the connection
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
        // Returning the found customer
        return customer;
    }

    // Find customers by name
    public List<Customer> findByName(String name) {
        // SQL Select statement
        String sql = "SELECT * FROM CUSTOMER WHERE UPPER(NAME) LIKE UPPER(?)";

        // List to store found customers
        List<Customer> customers = new ArrayList<>();

        // Database connection
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Setting the value for the query parameter
            pstm.setString(1, "%" + name + "%");
            
            // Executing the query and obtaining the result
            rset = pstm.executeQuery();

            while (rset.next()) {
                // Creating a Customer object for each found record
                Customer customer = new Customer();
                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));

                customers.add(customer);
            }

        } catch (SQLException e) {
            // Handle errors during search operation
            System.err.println("Error searching for customers by name: " + e.getMessage());
        } finally {
            try {
                // Closing the resources
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
        // Returning the list of found customers
        return customers;

    }

    // Find all customers
    public List<Customer> findAll() {
        // SQL Select statement
        String sql = "SELECT * FROM CUSTOMER";

        // List to store found customers
        List<Customer> customers = new ArrayList<>();

        // Database connection  
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establishing the connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Executing the query and obtaining the result
            rset = pstm.executeQuery();

            while (rset.next()) {
                // Creating a Customer object for each found record
                Customer customer = new Customer();

                // Setting the customer attributes
                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));

                // Adding the customer to the list 
                customers.add(customer);
            }

        } catch (SQLException e) {
            // Handle errors during search operation
            System.err.println("Error searching for customers: " + e.getMessage());
        } finally {
            try {
                // Fechando os recursos
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }

        }
        // Returning the list of found customers
        return customers;
    }

    // Find all customers with pagination
    public List<Customer> findAllPages(int page, int pageSize) {
        // SQL Select statement with pagination
        String sql = "SELECT * FROM CUSTOMER LIMIT ? OFFSET ?";
        // List to store found customers  
        List<Customer> customers = new ArrayList<>();

        // Database connection
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establishing the connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculating the offset for pagination
            int offset = (page - 1) * pageSize;

            // Setting the values for the query parameters
            pstm.setInt(1, pageSize);
            pstm.setInt(2, offset);

            // Executing the query and obtaining the result
            rset = pstm.executeQuery();

            // Iterating through the result set
            while (rset.next()) {
                Customer customer = new Customer();

                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));

                // Adding the customer to the list
                customers.add(customer);
            }
        } catch (SQLException e) {
            // Handle errors during search operation
            System.err.println("Error searching for customers in paginated format: " + e.getMessage());
        } finally {
            try {
                // Closing the resources
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
        // Returning the list of found customers
        return customers;
    }

    // Update customer in the database
    public void update(Customer customer) {
        // SQL statement to update an existing customer
        String sql = "UPDATE CUSTOMER SET NAME = ?, PHONE = ?, EMAIL = ? WHERE ID = ?";

        // Database connection  
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Establishing the connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Setting the values for the SQL statement
            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getPhone());
            pstm.setString(3, customer.getEmail());

            // Setting the ID for the WHERE clause
            pstm.setLong(4, customer.getIdCustomer());

            // Executing the SQL statement
            pstm.executeUpdate();
        } catch (SQLException e) {
            // Handle errors during update operation
            System.err.println("Error updating customers: " + e.getMessage());
        } finally {
            try {
                // Closing the resources
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

    // Delete customer from the database
    public void delete(long id) {
        // Sql statement to delete a customer
        String sql = "DELETE FROM CUSTOMER WHERE ID = ?";

        // Database connection
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Establishing the connection to the database
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Setting the ID for the WHERE clause
            pstm.setLong(1, id);

            // Executing the SQL statement
            pstm.executeUpdate();

            System.out.println("Cliente deletado com sucesso!");
        } catch (SQLException e) {
            // Handle errors during delete operation
            System.err.println("Error deleting customer: " + e.getMessage());
        } finally {
            // Closing the connections
            try {
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Handle errors when closing connections
                System.err.println("Error closing connections: " + e.getMessage());
            }
        }
    }

}