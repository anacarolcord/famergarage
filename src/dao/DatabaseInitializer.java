package dao;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Tabelas sem dependências
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "NAME VARCHAR NOT NULL, " +
                "PHONE VARCHAR NOT NULL, " +
                "EMAIL VARCHAR NOT NULL, " +
                "CPF VARCHAR UNIQUE)"
            );
            
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS EMPLOYEE (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "NAME VARCHAR NOT NULL, " +
                "ROLE VARCHAR NOT NULL, " +
                "CPF VARCHAR UNIQUE)"
            );
            
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS SERVICES (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "SERVICE VARCHAR NOT NULL, " +
                "DESCRIPTION VARCHAR NOT NULL)"
            );
            
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS AUTHENTICATION (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "USER VARCHAR UNIQUE, " +
                "PASSWORD VARCHAR, " +
                "ACCESS VARCHAR)"
            );
            
            // Tabelas com dependências
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS VEHICLE (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "PLATE VARCHAR NOT NULL, " +
                "MODEL VARCHAR NOT NULL, " +
                "BRAND VARCHAR, " +
                "FK_CUSTOMER INTEGER NOT NULL, " +
                "FOREIGN KEY (FK_CUSTOMER) REFERENCES CUSTOMER(ID) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)"
            );
            
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS APPOINTMENT (" +
                "ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "FK_VEHICLE INTEGER NOT NULL, " +
                "START_DATE DATETIME NOT NULL, " +
                "END_DATE DATETIME, " +
                "SERVICE INTEGER NOT NULL, " +
                "STATUS VARCHAR NOT NULL, " +
                "PRICE REAL, " +
                "FOREIGN KEY (SERVICE) REFERENCES SERVICES(ID) " +
                "ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                "FOREIGN KEY (FK_VEHICLE) REFERENCES VEHICLE(ID) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)"
            );
            
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS COMMISSIONERS (" +
                "FK_APPOINTMENT INTEGER NOT NULL, " +
                "FK_EMPLOYEE INTEGER NOT NULL, " +
                "PRIMARY KEY(FK_APPOINTMENT, FK_EMPLOYEE), " +
                "FOREIGN KEY (FK_EMPLOYEE) REFERENCES EMPLOYEE(ID) " +
                "ON UPDATE NO ACTION ON DELETE SET NULL, " +
                "FOREIGN KEY (FK_APPOINTMENT) REFERENCES APPOINTMENT(ID) " +
                "ON UPDATE NO ACTION ON DELETE CASCADE)"
            );
            
            System.out.println("✅ Banco de dados criado/verificado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para testar a conexão
    public static boolean testConnection() {
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("✅ Conexão com banco de dados OK!");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Erro na conexão: " + e.getMessage());
            return false;
        }
    }
}