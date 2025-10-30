package famergarage.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.management.RuntimeErrorException;

public class ConnectionFactory {
    private static final String URL = "jdbc:sqlite:agendamento_servicos.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao conectar com o Banco de Dados.", e);
        }
    }
}