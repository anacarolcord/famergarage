package view;

import controller.AppointmentController;
import controller.EmployeeController;
import controller.ServiceController;
import controller.VehicleController;
import controller.CustomerController;
import dao.*;
import enums.StatusAppointment;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentPanel extends JPanel {
    private JComboBox<String> cmbVehicle, cmbService, cmbStatus;
    private JTextField txtStartDate, txtEndDate, txtPrice;
    private JList<String> listEmployees;
    private DefaultListModel<String> employeeListModel;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private AppointmentController appointmentController;
    private VehicleController vehicleController;
    private ServiceController serviceController;
    private EmployeeController employeeController;
    
    private List<Vehicle> vehicles;
    private List<Service> services;
    private List<Employee> employees;
    private Long selectedAppointmentId;

    public AppointmentPanel() {
        appointmentController = new AppointmentController(new AppointmentDAO());
        vehicleController = new VehicleController(new VehicleDAO(), new CustomerController(new CustomerDAO()));
        serviceController = new ServiceController(new ServiceDAO());
        employeeController = new EmployeeController(new EmployeeDAO());
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadData();
        loadAppointments();
    }

    private void initComponents() {
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Agendamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Veículo
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Veículo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbVehicle = new JComboBox<>();
        formPanel.add(cmbVehicle, gbc);

        // Serviço
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Serviço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbService = new JComboBox<>();
        formPanel.add(cmbService, gbc);

        // Data Início
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Data Início:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtStartDate = new JTextField(20);
        txtStartDate.setToolTipText("Formato: yyyy-MM-dd HH:mm");
        formPanel.add(txtStartDate, gbc);

        // Data Fim
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Data Fim:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEndDate = new JTextField(20);
        txtEndDate.setToolTipText("Formato: yyyy-MM-dd HH:mm");
        formPanel.add(txtEndDate, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbStatus = new JComboBox<>();
        for (StatusAppointment status : StatusAppointment.values()) {
            cmbStatus.addItem(status.getText());
        }
        formPanel.add(cmbStatus, gbc);

        // Preço
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPrice = new JTextField(20);
        formPanel.add(txtPrice, gbc);

        // Funcionários
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Funcionários:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        employeeListModel = new DefaultListModel<>();
        listEmployees = new JList<>(employeeListModel);
        listEmployees.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollEmployees = new JScrollPane(listEmployees);
        scrollEmployees.setPreferredSize(new Dimension(200, 100));
        formPanel.add(scrollEmployees, gbc);

        // Painel de botões
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSave = new JButton("Salvar");
        btnUpdate = new JButton("Atualizar");
        btnDelete = new JButton("Excluir");
        btnClear = new JButton("Limpar");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        formPanel.add(buttonPanel, gbc);

        // Tabela
        String[] columns = {"ID", "Veículo", "Serviço", "Data Início", "Status", "Preço"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnSave.addActionListener(e -> saveAppointment());
        btnUpdate.addActionListener(e -> updateAppointment());
        btnDelete.addActionListener(e -> deleteAppointment());
        btnClear.addActionListener(e -> clearFields());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedAppointment();
            }
        });
    }

    private void loadData() {
        try {
            // Carregar veículos
            vehicles = vehicleController.listOfVehicles(1, 100);
            cmbVehicle.removeAllItems();
            for (Vehicle v : vehicles) {
                cmbVehicle.addItem(v.getIdVehicle() + " - " + v.getPlate() + " - " + v.getModel());
            }

            // Carregar serviços
            services = serviceController.findAll();
            cmbService.removeAllItems();
            for (Service s : services) {
                cmbService.addItem(s.getIdService() + " - " + s.getNameService());
            }

            // Carregar funcionários
            employees = employeeController.listOfEmployees();
            employeeListModel.clear();
            for (Employee e : employees) {
                employeeListModel.addElement(e.getIdEmployee() + " - " + e.getNameEmployee());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
        }
    }

    private void saveAppointment() {
        try {
            if (cmbVehicle.getSelectedIndex() < 0 || cmbService.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Selecione veículo e serviço!");
                return;
            }

            if (listEmployees.getSelectedIndices().length == 0) {
                JOptionPane.showMessageDialog(this, "Selecione pelo menos um funcionário!");
                return;
            }

            Appointment appointment = new Appointment();
            appointment.setVehicle(vehicles.get(cmbVehicle.getSelectedIndex()));
            appointment.setService(services.get(cmbService.getSelectedIndex()));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            appointment.setStartDate(LocalDateTime.parse(txtStartDate.getText().trim(), formatter));
            
            if (!txtEndDate.getText().trim().isEmpty()) {
                appointment.setEndDate(LocalDateTime.parse(txtEndDate.getText().trim(), formatter));
            }
            
            appointment.setStatus(StatusAppointment.values()[cmbStatus.getSelectedIndex()]);
            
            if (!txtPrice.getText().trim().isEmpty()) {
                appointment.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            }

            // Adicionar funcionários selecionados
            List<Employee> selectedEmployees = new ArrayList<>();
            for (int index : listEmployees.getSelectedIndices()) {
                selectedEmployees.add(employees.get(index));
            }
            appointment.setEmployees(selectedEmployees);

            appointmentController.saveAppointment(appointment);
            JOptionPane.showMessageDialog(this, "Agendamento salvo com sucesso!");
            clearFields();
            loadAppointments();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAppointment() {
        try {
            if (selectedAppointmentId == null) {
                JOptionPane.showMessageDialog(this, "Selecione um agendamento para atualizar!");
                return;
            }

            Appointment appointment = new Appointment();
            appointment.setIdAppointment(selectedAppointmentId);
            appointment.setVehicle(vehicles.get(cmbVehicle.getSelectedIndex()));
            appointment.setService(services.get(cmbService.getSelectedIndex()));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            appointment.setStartDate(LocalDateTime.parse(txtStartDate.getText().trim(), formatter));
            
            if (!txtEndDate.getText().trim().isEmpty()) {
                appointment.setEndDate(LocalDateTime.parse(txtEndDate.getText().trim(), formatter));
            }
            
            appointment.setStatus(StatusAppointment.values()[cmbStatus.getSelectedIndex()]);
            
            if (!txtPrice.getText().trim().isEmpty()) {
                appointment.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            }

            List<Employee> selectedEmployees = new ArrayList<>();
            for (int index : listEmployees.getSelectedIndices()) {
                selectedEmployees.add(employees.get(index));
            }
            appointment.setEmployees(selectedEmployees);

            appointmentController.updateAppointment(appointment);
            JOptionPane.showMessageDialog(this, "Agendamento atualizado com sucesso!");
            clearFields();
            loadAppointments();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAppointment() {
        try {
            if (selectedAppointmentId == null) {
                JOptionPane.showMessageDialog(this, "Selecione um agendamento para excluir!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir este agendamento?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Appointment appointment = new Appointment();
                appointment.setIdAppointment(selectedAppointmentId);
                appointmentController.delete(appointment);
                JOptionPane.showMessageDialog(this, "Agendamento excluído com sucesso!");
                clearFields();
                loadAppointments();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAppointments() {
        try {
            List<Appointment> appointments = appointmentController.findAll();
            tableModel.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            for (Appointment a : appointments) {
                String vehicleInfo = "";
                for (Vehicle v : vehicles) {
                    if (v.getIdVehicle().equals(a.getVehicle().getIdVehicle())) {
                        vehicleInfo = v.getPlate();
                        break;
                    }
                }
                
                String serviceInfo = "";
                for (Service s : services) {
                    if (s.getIdService().equals(a.getService().getIdService())) {
                        serviceInfo = s.getNameService();
                        break;
                    }
                }
                
                tableModel.addRow(new Object[]{
                    a.getIdAppointment(),
                    vehicleInfo,
                    serviceInfo,
                    a.getStartDate().format(formatter),
                    a.getStatus().getText(),
                    a.getPrice() != null ? String.format("R$ %.2f", a.getPrice()) : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar agendamentos: " + ex.getMessage());
        }
    }

    private void loadSelectedAppointment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                selectedAppointmentId = Long.parseLong(tableModel.getValueAt(selectedRow, 0).toString());
                Appointment appointment = appointmentController.findById(selectedAppointmentId);
                
                // Selecionar veículo
                for (int i = 0; i < vehicles.size(); i++) {
                    if (vehicles.get(i).getIdVehicle().equals(appointment.getVehicle().getIdVehicle())) {
                        cmbVehicle.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Selecionar serviço
                for (int i = 0; i < services.size(); i++) {
                    if (services.get(i).getIdService().equals(appointment.getService().getIdService())) {
                        cmbService.setSelectedIndex(i);
                        break;
                    }
                }
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                txtStartDate.setText(appointment.getStartDate().format(formatter));
                if (appointment.getEndDate() != null) {
                    txtEndDate.setText(appointment.getEndDate().format(formatter));
                }
                
                cmbStatus.setSelectedIndex(appointment.getStatus().ordinal());
                
                if (appointment.getPrice() != null) {
                    txtPrice.setText(appointment.getPrice().toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
            }
        }
    }

    private void clearFields() {
        selectedAppointmentId = null;
        if (cmbVehicle.getItemCount() > 0) cmbVehicle.setSelectedIndex(0);
        if (cmbService.getItemCount() > 0) cmbService.setSelectedIndex(0);
        txtStartDate.setText("");
        txtEndDate.setText("");
        cmbStatus.setSelectedIndex(0);
        txtPrice.setText("");
        listEmployees.clearSelection();
        table.clearSelection();
    }
}