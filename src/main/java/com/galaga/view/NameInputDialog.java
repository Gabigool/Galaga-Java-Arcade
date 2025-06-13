package main.java.com.galaga.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameInputDialog extends JDialog {
    private String playerName = null;
    private JTextField nameField;
    private boolean submitted = false;
    
    public NameInputDialog(JFrame parent, int score, int level) {
        super(parent, "¡Nuevo High Score!", true);
        
        setupDialog(score, level);
        setupComponents();
        setupLayout();
        setupEvents();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void setupDialog(int score, int level) {
        setSize(400, 250);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
    }
    
    private void setupComponents() {
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBackground(Color.DARK_GRAY);
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con mensaje
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        JLabel messageLabel = new JLabel("🎉 ¡FELICIDADES! 🎉");
        messageLabel.setForeground(Color.YELLOW);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(messageLabel);
        
        // Panel central con información y campo de texto
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel infoLabel = new JLabel("¡Lograste un High Score!", SwingConstants.CENTER);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(Color.BLACK);
        JLabel nameLabel = new JLabel("Ingresa tu nombre: ");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        
        centerPanel.add(infoLabel);
        centerPanel.add(inputPanel);
        
        // Panel inferior con botones
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.BLACK);
        
        JButton submitButton = new JButton("💾 Guardar");
        JButton cancelButton = new JButton("❌ Cancelar");
        
        styleButton(submitButton, Color.GREEN);
        styleButton(cancelButton, Color.RED);
        
        submitButton.addActionListener(e -> submitName());
        cancelButton.addActionListener(e -> dispose());
        
        bottomPanel.add(submitButton);
        bottomPanel.add(cancelButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
    
    private void setupEvents() {
        // Permitir envío con Enter
        nameField.addActionListener(e -> submitName());
        
        // Focus automático en el campo de texto
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                nameField.requestFocusInWindow();
            }
        });
    }
    
    private void submitName() {
        String name = nameField.getText().trim();
        if (!name.isEmpty() && name.length() <= 20) {
            playerName = name;
            submitted = true;
            dispose();
        } else {
            String message = name.isEmpty() ? 
                "Por favor ingresa un nombre válido" : 
                "El nombre debe tener máximo 20 caracteres";
            
            JOptionPane.showMessageDialog(this, 
                message, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocusInWindow();
        }
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public boolean wasSubmitted() {
        return submitted;
    }
}
