package view;

import controller.CustomerController;
import dao.CustomerDAO;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerPanel extends JPanel {
    private JTextField txtName, txtPhone, txtEmail, txtCPF;
    private JButton btnSave, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private CustomerController controller;

    public CustomerPanel() {
        controller = new CustomerController(new CustomerDAO());
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
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

        // Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSave = new JButton("Salvar");
        btnUpdate = new JButton("Atualizar");
        btnDelete = new JButton("Excluir");
        btnClear = new JButton("Limpar");
        btnSearch = new JButton("Buscar por Nome");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Tabela
        String[] columns = {"ID", "Nome", "CPF", "Telefone", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Adicionar componentes ao painel principal
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnSave.addActionListener(e -> saveCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchByName());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCustomer();
            }
        });
    }

    private void saveCustomer() {
        try {
            Customer customer = new Customer();
            customer.setName(txtName.getText().trim());
            customer.setCpf(txtCPF.getText().trim());
            customer.setPhone(txtPhone.getText().trim());
            customer.setEmail(txtEmail.getText().trim());

            controller.createCustomer(customer);
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            clearFields();
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        try {
            Customer customer = new Customer();
            customer.setName(txtName.getText().trim());
            customer.setCpf(txtCPF.getText().trim());
            customer.setPhone(txtPhone.getText().trim());
            customer.setEmail(txtEmail.getText().trim());

            controller.updateCustomer(customer);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            clearFields();
            loadCustomers();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        try {
            String cpf = txtCPF.getText().trim();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir este cliente?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteCustomer(cpf);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                clearFields();
                loadCustomers();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchByName() {
        String name = JOptionPane.showInputDialog(this, "Digite o nome para buscar:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                List<Customer> customers = controller.findByName(name);
                tableModel.setRowCount(0);
                for (Customer c : customers) {
                    tableModel.addRow(new Object[]{
                        c.getIdCustomer(), c.getName(), c.getCpf(), 
                        c.getPhone(), c.getEmail()
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro na busca: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = controller.listOfCustomers();
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                    c.getIdCustomer(), c.getName(), c.getCpf(), 
                    c.getPhone(), c.getEmail()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtCPF.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtPhone.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtCPF.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }
}