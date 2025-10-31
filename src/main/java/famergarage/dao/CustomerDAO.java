package famergarage.dao;

import famergarage.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Método para salvar um novo cliente no banco de dados
    public void save(Customer customer) {
        // Comando SQL de Insert
        String sql = "INSERT INTO CUSTOMER (NAME, PHONE, EMAIL) VALUES (?, ?, ?)";

        // Objetos para conexão e execução do comando SQL
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            // Estabelecendo a conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            // Preparando a query com PreparedStatement (evitando SQL Injection)
            pstm = conn.prepareStatement(sql);

            // Atribuindo os valores aos parâmetros da query
            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getPhone());
            pstm.setString(3, customer.getEmail());

            // Executando o comando SQL
            pstm.executeUpdate();

            System.out.println("Cliente salvo com sucesso!");
        } catch (SQLException e) {
            // Tratamento de erro na operação de salvamento
            System.err.println("Erro ao salvar o cliente: " + e.getMessage());
        } finally {
            // Fechar recursos (se os objetos não forem nulos)
            try {
                // Fechando o PreparedStatement
                if (pstm != null)
                    pstm.close();

                // Fechando a conexão com o banco de dados
                if (conn != null)
                    conn.close();

            } catch (SQLException e) {
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }
    }

    // Método para buscar um cliente pelo CPF
    public Customer findByCPF(String cpf) {
        // Comando SQL de Select
        String sql = "SELECT * FROM CUSTOMER WHERE CPF = ?";

        // Conexão com o banco de dados
        Connection conn = null;
        // Preparando a query com PreparedStatement
        PreparedStatement pstm = null;
        // Objeto para armazenar o resultado da query
        ResultSet rset = null;
        // Objeto Customer para armazenar o cliente encontrado
        Customer customer = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Atribuindo o valor ao parâmetro da query
            pstm.setString(1, cpf);

            // Executando a query e obtendo o resultado
            rset = pstm.executeQuery();

            // Verificando se um registro foi encontrado
            if (rset.next()) {
                
                customer = new Customer();
                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));
                customer.setCpf(rset.getString("CPF"));
            }
        } catch (SQLException e) {
            // Tratamento de erro na operação de busca
            System.err.println("Erro ao buscar cliente por CPF: " + e.getMessage());
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
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }
        // Retornando o cliente encontrado (ou null se não encontrado)
        return customer;
    }

    // Método para buscar clientes pelo nome
    public List<Customer> findByName(String name) {
        // Comando SQL de Select
        String sql = "SELECT * FROM CUSTOMER WHERE UPPER(NAME) LIKE UPPER(?)";

        // Lista para armazenar os clientes encontrados
        List<Customer> customers = new ArrayList<>();

        // Conexão com o banco de dados
        Connection conn = null;
        // Preparando a query com PreparedStatement
        PreparedStatement pstm = null;
        // Objeto para armazenar o resultado da query
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Atribuindo o valor ao parâmetro da query
            pstm.setString(1, "%" + name + "%");
            // Executando a query e obtendo o resultado
            rset = pstm.executeQuery();

            while (rset.next()) {
                // Criando um objeto Customer para cada registro encontrado
                Customer customer = new Customer();
                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));

                customers.add(customer);
            }

        } catch (SQLException e) {
            // Tratamento de erro na operação de busca
            System.err.println("Erro ao buscar clientes por nome: " + e.getMessage());
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
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }
        // Retornando a lista de clientes encontrados
        return customers;

    }

    // Método para obter todos os clientes do banco de dados
    public List<Customer> findAll() {
        // Comando SQL de Select
        String sql = "SELECT * FROM CUSTOMER";

        // Lista para armazenar os clientes encontrados
        List<Customer> customers = new ArrayList<>();

        // Conexão com o banco de dados
        Connection conn = null;
        // Preparando a query com PreparedStatement
        PreparedStatement pstm = null;
        // Objeto para armazenar o resultado da query
        ResultSet rset = null;

        try {
            // Estabelecendo a conexão com o banco de dados
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Executando a query e obtendo o resultado
            rset = pstm.executeQuery();

            while (rset.next()) {
                // Criando um objeto Customer para cada registro encontrado
                Customer customer = new Customer();

                // Setando os atributos do cliente
                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));

                // Adicionando o cliente à lista
                customers.add(customer);
            }

        } catch (SQLException e) {
            // Tratamento de erro na operação de busca
            System.err.println("Erro ao buscar os clientes: " + e.getMessage());
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
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }

        }
        // Retornando a lista de clientes
        return customers;
    }

    // Método para obter clientes com paginação
    public List<Customer> findAllPages(int page, int pageSize) {
        // Comando SQL de Select com paginação
        String sql = "SELECT * FROM CUSTOMER LIMIT ? OFFSET ?";
        // Lista para armazenar os clientes encontrados
        List<Customer> customers = new ArrayList<>();

        // Conexão com o banco de dados
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Logica para calcular o OFFSET      
            int offset = (page - 1) * pageSize;

            // Atribuindo os valores aos parâmetros da query 
            pstm.setInt(1, pageSize);
            pstm.setInt(2, offset);

            // Executando a query e obtendo o resultado
            rset = pstm.executeQuery();
            
            // Iterando sobre os resultados
            while (rset.next()) {
                Customer customer = new Customer();

                customer.setIdCustomer(rset.getLong("ID"));
                customer.setName(rset.getString("NAME"));
                customer.setPhone(rset.getString("PHONE"));
                customer.setEmail(rset.getString("EMAIL"));

                // Adicionando o cliente à lista  
                customers.add(customer);
            }
        } catch (SQLException e) {
            // Tratamento de erro na operação de busca
            System.err.println("Erro ao buscar os clientes de forma paginada: " + e.getMessage());
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
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }
        // Retornando a lista de clientes paginados
        return customers;
    }

    // Método para atualizar um cliente existente no banco de dados
    public void update(Customer customer) {
        // Comando SQL de Update
        String sql = "UPDATE CUSTOMER SET NAME = ?, PHONE = ?, EMAIL = ? WHERE ID = ?";
        
        // Objetos para conexão e execução do comando SQL
        Connection conn = null;
        PreparedStatement pstm = null;
        
        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Atribuindo os valores aos parâmetros da query
            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getPhone());
            pstm.setString(3, customer.getEmail());

            // Atribuindo o ID para o placeholder do WHERE
            pstm.setLong(4, customer.getIdCustomer());

            // Executando o comando SQL
            pstm.executeUpdate();
        } catch (SQLException e) {
            // Tratamento de erro na operação de atualização
            System.err.println("Erro ao atualizar o cliente: " + e.getMessage());
        } finally {
            try {
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }
    }

    // Método para deletar um cliente do banco de dados
    public void delete(long id) {
        // Comando SQL de Delete
        String sql = "DELETE FROM CUSTOMER WHERE ID = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try { 
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Atribuindo o valor ao parâmetro da query
            pstm.setLong(1, id);

            // Executando o comando SQL
            pstm.executeUpdate();

            System.out.println("Cliente deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o cliente: " + e.getMessage());
        } finally {
            try {
                if (pstm != null)
                    pstm.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // Tratamento de erro ao fechar as conexões
                System.err.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }
    }

}