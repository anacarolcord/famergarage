package view;

import controller.EmployeeController;
import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeePanel extends JPanel {
    private JTextField txtName, txtCPF, txtRole;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private EmployeeController controller;

    public EmployeePanel() {
        controller = new EmployeeController(new EmployeeDAO());
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadEmployees();
    }

    private void initComponents() {
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCPF = new JTextField(20);
        formPanel.add(txtCPF, gbc);

        // Cargo
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtRole = new JTextField(20);
        formPanel.add(txtRole, gbc);

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

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Tabela
        String[] columns = {"ID", "Nome", "CPF", "Cargo"};
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
        btnSave.addActionListener(e -> saveEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearFields());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedEmployee();
            }
        });
    }

    private void saveEmployee() {
        try {
            Employee employee = new Employee();
            employee.setNameEmployee(txtName.getText().trim());
            employee.setCpf(txtCPF.getText().trim());
            employee.setRole(txtRole.getText().trim());

            controller.saveEmployee(employee);
            JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
            clearFields();
            loadEmployees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEmployee() {
        try {
            Employee employee = new Employee();
            employee.setNameEmployee(txtName.getText().trim());
            employee.setCpf(txtCPF.getText().trim());
            employee.setRole(txtRole.getText().trim());

            controller.updateEmployee(employee);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            clearFields();
            loadEmployees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        try {
            String cpf = txtCPF.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário para excluir!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir este funcionário?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                controller.delete(cpf);
                JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
                clearFields();
                loadEmployees();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = controller.listOfEmployees();
            tableModel.setRowCount(0);
            for (Employee e : employees) {
                tableModel.addRow(new Object[]{
                    e.getIdEmployee(), e.getNameEmployee(), 
                    e.getCpf(), e.getRole()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar funcionários: " + ex.getMessage());
        }
    }

    private void loadSelectedEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtCPF.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtRole.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtCPF.setText("");
        txtRole.setText("");
        table.clearSelection();
    }
}