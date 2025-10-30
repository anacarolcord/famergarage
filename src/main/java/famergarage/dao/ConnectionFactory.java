package famergarage.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // URL de conexão com o banco de dados SQLite
    private static final String URL = "jdbc:sqlite:agendamento_servicos.db";

    public static Connection getConnection() {
        try {
            // Estabelecendo e retornando a conexão com o banco de dados
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            // Tratamento de erro na conexão
            throw new RuntimeException("Falha ao conectar com o Banco de Dados.", e);
        }
    }
}