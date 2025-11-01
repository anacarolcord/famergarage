package dao;

import model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    // Save a new employee
    public void save(Employee employee) {
        // SQL Statement to insert a new employee
        String sql = "INSERT INTO EMPLOYEE (NAME, ROLE, CPF) VALUES (?, ?, ?)";

        // Objects for connection, prepared statement
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Get a connection
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set parameters
            pstm.setString(1, employee.getNameEmployee());
            pstm.setString(2, employee.getRole());
            pstm.setString(3, employee.getCpf());

            // Execute the insert
            pstm.executeUpdate();

            System.out.println("Employee saved successfully!");
        } catch (Exception e) {
            // Handle exceptions
            System.err.println("Error saving employee: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Handle closing exceptions
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Update a existing employee
    public void update(Employee employee) {
        // Sql statement to update an existing employee
        String sql = "UPDATE EMPLOYEE SET NAME = ?, ROLE = ? WHERE CPF = ?";

        // Database connection and prepared statement
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Establish connection
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set parameters
            pstm.setString(1, employee.getNameEmployee());
            pstm.setString(2, employee.getRole());

            // Set the CPF parameter for the WHERE clause
            pstm.setString(3, employee.getCpf());

            // Execute the update
            pstm.executeUpdate();
        } catch (Exception e) {
            // Handle exceptions
            System.err.println("Error updating employee: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Handle closing exceptions
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Delete an employee by CPF
    public void delete(String cpf) {
        // SQL Statement to delete an employee
        String sql = "DELETE FROM EMPLOYEE WHERE CPF = ?";

        // Database connection and prepared statement
        Connection conn = null;
        PreparedStatement pstm = null;

        // Establish connection and execute delete
        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the CPF parameter
            pstm.setString(1, cpf);

            // Execute the delete
            pstm.executeUpdate();

            // Success message
            System.out.println("Employee deleted successfully!");
        } catch (Exception e) {
            // Handle exceptions
            System.err.println("Error deleting employee: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Handle closing exceptions
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Find an employee by CPF
    public Employee findByCpf(String cpf) {
        // Statement SQL to find an employee by CPF
        String sql = "SELECT * FROM EMPLOYEE WHERE CPF = ?";

        // Objects for connection, prepared statement, and result set
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        Employee employee = null;

        try {
            // Establish connection
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Set the CPF parameter
            pstm.setString(1, cpf);

            // Execute the query
            rset = pstm.executeQuery();

            // Process the result set
            if (rset.next()) {
                employee = new Employee();
                employee.setIdEmployee(rset.getLong("ID"));
                employee.setNameEmployee(rset.getString("NAME"));
                employee.setCpf(rset.getString("CPF"));
                employee.setRole(rset.getString("ROLE"));
                return employee;
            }
        } catch (Exception e) {
            // Handle exceptions
            System.err.println("Error finding employee: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Handle closing exceptions
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        // Returning the found employee
        return employee;
    }

    // Find all employees with pagination
    public List<Employee> findAllPage(int page, int pageSize) {
        // Sql statement to get all employees with pagination
        String sql = "SELECT * FROM EMPLOYEE LIMIT ? OFFSET ?";

        // List to hold employees
        List<Employee> employees = new ArrayList<>();

        // Database connection
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish connection
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Calculate offset
            int offset = (page - 1) * pageSize;

            // Set parameters for pagination
            pstm.setInt(1, pageSize);
            pstm.setInt(2, offset);

            // Execute the query
            rset = pstm.executeQuery();

            // Process the result set
            while (rset.next()) {
                Employee employee = new Employee();

                employee.setIdEmployee(rset.getLong("ID"));
                employee.setNameEmployee(rset.getString("NAME"));
                employee.setCpf(rset.getString("CPF"));
                employee.setRole(rset.getString("ROLE"));

                employees.add(employee);
            }
        } catch (SQLException e) {
            // Handle exceptions
            System.err.println("Error searching for employees: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Handle closing exceptions
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return employees;
    }

    // Get all employees without pagination
    public List<Employee> findAll() {
        // SQL: SELECT * FROM EMPLOYEE
        String sql = "SELECT * FROM EMPLOYEE";

        // List to hold employees
        List<Employee> employees = new ArrayList<>();

        // Database connection and prepared statement
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            // Establish connection
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Execute the query
            rset = pstm.executeQuery();

            while (rset.next()) {
                Employee employee = new Employee();

                employee.setIdEmployee(rset.getLong("ID"));
                employee.setNameEmployee(rset.getString("NAME"));
                employee.setCpf(rset.getString("CPF"));
                employee.setRole(rset.getString("ROLE"));

                employees.add(employee);
            }
        } catch (SQLException e) {
            // Handle exceptions
            System.err.println("Error retrieving employees: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rset != null)
                    rset.close();
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                // Handle closing exceptions
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        return employees;
    }
}