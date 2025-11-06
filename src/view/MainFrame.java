package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private CustomerPanel customerPanel;
    private VehiclePanel vehiclePanel;
    private EmployeePanel employeePanel;
    private ServicePanel servicePanel;
    private AppointmentPanel appointmentPanel;

    public MainFrame() {
        setTitle("Sistema de Agendamento - Mecânica");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        customerPanel = new CustomerPanel();
        vehiclePanel = new VehiclePanel();
        employeePanel = new EmployeePanel();
        servicePanel = new ServicePanel();
        appointmentPanel = new AppointmentPanel();
        
        tabbedPane.addTab("Clientes", new JScrollPane(customerPanel));
        tabbedPane.addTab("Veículos", new JScrollPane(vehiclePanel));
        tabbedPane.addTab("Funcionários", new JScrollPane(employeePanel));
        tabbedPane.addTab("Serviços", new JScrollPane(servicePanel));
        tabbedPane.addTab("Agendamentos", new JScrollPane(appointmentPanel));
        
        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Inicializar banco de dados
        dao.DatabaseInitializer.initializeDatabase();
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame().setVisible(true);
        });
    }
}