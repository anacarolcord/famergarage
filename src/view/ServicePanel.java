package view;

import controller.ServiceController;
import dao.ServiceDAO;
import model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServicePanel extends JPanel {
    private JTextField txtName;
    private JTextArea txtDescription;
    private JButton btnSave, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private ServiceController controller;
    private Long selectedServiceId;

    public ServicePanel() {
        controller = new ServiceController(new ServiceDAO());
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadServices();
    }

    private void initComponents() {
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Serviço"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome do Serviço
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome do Serviço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        txtDescription = new JTextArea(5, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDescription = new JScrollPane(txtDescription);
        formPanel.add(scrollDescription, gbc);

        // Painel de botões
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
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
        String[] columns = {"ID", "Nome do Serviço", "Descrição"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnSave.addActionListener(e -> saveService());
        btnUpdate.addActionListener(e -> updateService());
        btnDelete.addActionListener(e -> deleteService());
        btnClear.addActionListener(e -> clearFields());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedService();
            }
        });
    }

    private void saveService() {
        try {
            Service service = new Service();
            service.setNameService(txtName.getText().trim());
            service.setDescription(txtDescription.getText().trim());

            controller.save(service);
            JOptionPane.showMessageDialog(this, "Serviço salvo com sucesso!");
            clearFields();
            loadServices();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateService() {
        try {
            if (selectedServiceId == null) {
                JOptionPane.showMessageDialog(this, "Selecione um serviço para atualizar!");
                return;
            }

            Service service = new Service();
            service.setIdService(selectedServiceId);
            service.setNameService(txtName.getText().trim());
            service.setDescription(txtDescription.getText().trim());

            controller.update(service);
            JOptionPane.showMessageDialog(this, "Serviço atualizado com sucesso!");
            clearFields();
            loadServices();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteService() {
        try {
            if (selectedServiceId == null) {
                JOptionPane.showMessageDialog(this, "Selecione um serviço para excluir!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir este serviço?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                controller.delete(selectedServiceId);
                JOptionPane.showMessageDialog(this, "Serviço excluído com sucesso!");
                clearFields();
                loadServices();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadServices() {
        try {
            List<Service> services = controller.findAll();
            tableModel.setRowCount(0);
            for (Service s : services) {
                tableModel.addRow(new Object[]{
                    s.getIdService(), s.getNameService(), s.getDescription()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar serviços: " + ex.getMessage());
        }
    }

    private void loadSelectedService() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            selectedServiceId = Long.parseLong(tableModel.getValueAt(selectedRow, 0).toString());
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtDescription.setText(tableModel.getValueAt(selectedRow, 2).toString());
        }
    }

    private void clearFields() {
        selectedServiceId = null;
        txtName.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }
}