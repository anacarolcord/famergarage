package view;

import controller.CustomerController;
import controller.VehicleController;
import dao.CustomerDAO;
import dao.VehicleDAO;
import model.Customer;
import model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehiclePanel extends JPanel {
    private JTextField txtPlate, txtModel, txtBrand;
    private JComboBox<String> cmbCustomer;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private VehicleController vehicleController;
    private CustomerController customerController;
    private List<Customer> customers;

    public VehiclePanel() {
        vehicleController = new VehicleController(new VehicleDAO(), new CustomerController(new CustomerDAO()));
        customerController = new CustomerController(new CustomerDAO());
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadCustomers();
        loadVehicles();
    }

    private void initComponents() {
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Veículo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Placa
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPlate = new JTextField(20);
        formPanel.add(txtPlate, gbc);

        // Modelo
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtModel = new JTextField(20);
        formPanel.add(txtModel, gbc);

        // Marca
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBrand = new JTextField(20);
        formPanel.add(txtBrand, gbc);

        // Cliente
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbCustomer = new JComboBox<>();
        formPanel.add(cmbCustomer, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSave = new JButton("Salvar");
        btnUpdate = new JButton("Atualizar");
        btnDelete = new JButton("Excluir");
        btnClear = new JButton("Limpar");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Tabela
        String[] columns = {"ID", "Placa", "Modelo", "Marca", "Cliente"};
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
        btnSave.addActionListener(e -> saveVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
        btnClear.addActionListener(e -> clearFields());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedVehicle();
            }
        });
    }

    private void loadCustomers() {
        try {
            customers = customerController.listOfCustomers();
            cmbCustomer.removeAllItems();
            for (Customer c : customers) {
                cmbCustomer.addItem(c.getIdCustomer() + " - " + c.getName());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + ex.getMessage());
        }
    }

    private void saveVehicle() {
        try {
            if (cmbCustomer.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente!");
                return;
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setPlate(txtPlate.getText().trim());
            vehicle.setModel(txtModel.getText().trim());
            vehicle.setBrand(txtBrand.getText().trim());
            vehicle.setIdCustomer(customers.get(cmbCustomer.getSelectedIndex()));

            vehicleController.saveVehicle(vehicle);
            JOptionPane.showMessageDialog(this, "Veículo salvo com sucesso!");
            clearFields();
            loadVehicles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        try {
            if (cmbCustomer.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente!");
                return;
            }

            Vehicle vehicle = new Vehicle();
            vehicle.setPlate(txtPlate.getText().trim());
            vehicle.setModel(txtModel.getText().trim());
            vehicle.setBrand(txtBrand.getText().trim());
            vehicle.setIdCustomer(customers.get(cmbCustomer.getSelectedIndex()));

            vehicleController.updateVehicle(vehicle);
            JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso!");
            clearFields();
            loadVehicles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        try {
            String plate = txtPlate.getText().trim();
            if (plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um veículo para excluir!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir este veículo?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                vehicleController.deleteVehicle(plate);
                JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso!");
                clearFields();
                loadVehicles();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleController.listOfVehicles(1, 100);
            tableModel.setRowCount(0);
            for (Vehicle v : vehicles) {
                String customerName = "";
                for (Customer c : customers) {
                    if (c.getIdCustomer().equals(v.getIdCustomer().getIdCustomer())) {
                        customerName = c.getName();
                        break;
                    }
                }
                tableModel.addRow(new Object[]{
                    v.getIdVehicle(), v.getPlate(), v.getModel(), 
                    v.getBrand(), customerName
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + ex.getMessage());
        }
    }

    private void loadSelectedVehicle() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtPlate.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtModel.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtBrand.setText(tableModel.getValueAt(selectedRow, 3).toString());
            
            String customerName = tableModel.getValueAt(selectedRow, 4).toString();
            for (int i = 0; i < cmbCustomer.getItemCount(); i++) {
                if (cmbCustomer.getItemAt(i).contains(customerName)) {
                    cmbCustomer.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void clearFields() {
        txtPlate.setText("");
        txtModel.setText("");
        txtBrand.setText("");
        if (cmbCustomer.getItemCount() > 0) {
            cmbCustomer.setSelectedIndex(0);
        }
        table.clearSelection();
    }
}