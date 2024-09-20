/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package examenlab2;

/**
 *
 * @author Administrator
 */

import examenlab2.PSNUsers.Trophy;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MainGUI {
    private PSNUsers USERS;
    private JFrame frame;
    private JTextField txtUser, txtGame, txtName;
    private JComboBox<PSNUsers.Trophy> comboBox;
    private JTextArea infoUser;

    public MainGUI() throws IOException {
        USERS = new PSNUsers();
        frame = new JFrame("Gestión de Usuarios PSN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500); 
        frame.setLocationRelativeTo(null); 
        frame.getContentPane().setBackground(new Color(240, 240, 240)); 

        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 18);
        Font fuenteContenido = new Font("Segoe UI", Font.PLAIN, 16);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 255, 230)); 
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        frame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Nombre de Usuario:");
        userLabel.setFont(fuenteTitulo); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);

        txtUser = new JTextField(20);
        txtUser.setFont(fuenteContenido); 
        txtUser.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(5, 10, 5, 10))); 
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3; 
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtUser, gbc);

        JButton addButton = new JButton("Agregar Nuevo Usuario"); 
        addButton.setFont(fuenteContenido); 
        addButton.setBackground(new Color(255, 178, 102)); 
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(new LineBorder(new Color(255, 178, 102), 2, true)); 
        addButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                try {
                    if (USERS.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        USERS.addUser(username);
                        JOptionPane.showMessageDialog(null, "Usuario creado con éxito!", "Creación", 1);
                        limpiarCampos();
                        actualizarInfo();
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al agregar usuario.");
                }
            }
        });

        JButton deactivateButton = new JButton("Desactivar Usuario Existente"); 
        deactivateButton.setFont(fuenteContenido); 
        deactivateButton.setBackground(new Color(255, 178, 102));
        deactivateButton.setForeground(Color.WHITE);
        deactivateButton.setFocusPainted(false);
        deactivateButton.setBorder(new LineBorder(new Color(255, 178, 102), 2, true)); 
        deactivateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(deactivateButton, gbc);
        deactivateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                try {
                    if (!USERS.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        USERS.deactivateUser(username);
                        JOptionPane.showMessageDialog(null, "El usuario ha sido desactivado", "Desactivación", 1);
                        limpiarCampos();
                        actualizarInfo();
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al desactivar usuario.");
                }
            }
        });

        JButton searchButton = new JButton("Buscar Información de Usuario"); 
        searchButton.setFont(fuenteContenido); 
        searchButton.setBackground(new Color(255, 178, 102));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(new LineBorder(new Color(255, 178, 102), 2, true)); 
        searchButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(searchButton, gbc);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                String info = "";
                try {
                    if (!USERS.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        info = USERS.playerInfo(username);
                        infoUser.setText(info);
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al buscar usuario.");
                }
            }
        });

        JButton addTrophyButton = new JButton("Agregar Trofeo al Usuario"); 
        addTrophyButton.setFont(fuenteContenido); 
        addTrophyButton.setBackground(new Color(255, 178, 102));
        addTrophyButton.setForeground(Color.WHITE);
        addTrophyButton.setFocusPainted(false);
        addTrophyButton.setBorder(new LineBorder(new Color(255, 178, 102), 2, true)); 
        addTrophyButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); 
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(addTrophyButton, gbc);
        addTrophyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUser.getText();
                String trophyGame = txtGame.getText();
                String trophyName = txtName.getText();
                PSNUsers.Trophy type = (PSNUsers.Trophy) comboBox.getSelectedItem();
                try {
                    if (!USERS.buscarUsername(username)) {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!USERS.playerInfo(username).contains("Activo: Sí")) {
                        JOptionPane.showMessageDialog(null, "Usuario no está activo", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        USERS.addTrophieTo(username, trophyGame, trophyName, type);
                        JOptionPane.showMessageDialog(null, "El trofeo se ha agregado al usuario", "Creación", 1);
                        limpiarCampos();
                        actualizarInfo();
                    }
                } catch (IOException ex) {
                    mostrarError(ex, "Error al agregar trofeo.");
                }
            }
        });

        JLabel trophyGameLabel = new JLabel("Juego:");
        trophyGameLabel.setFont(fuenteTitulo); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(trophyGameLabel, gbc);

        txtGame = new JTextField(20);
        txtGame.setFont(fuenteContenido); 
        txtGame.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(5, 10, 5, 10))); 
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3; 
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtGame, gbc);

        JLabel trophyNameLabel = new JLabel("Nombre del Trofeo:");
        trophyNameLabel.setFont(fuenteTitulo); 
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(trophyNameLabel, gbc);

        txtName = new JTextField(20);
        txtName.setFont(fuenteContenido); 
        txtName.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(5, 10, 5, 10))); 
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3; 
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtName, gbc);

        JLabel trophyTypeLabel = new JLabel("Tipo de Trofeo:");
        trophyTypeLabel.setFont(fuenteTitulo); 
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(trophyTypeLabel, gbc);

        comboBox = new JComboBox<>(PSNUsers.Trophy.values());
        comboBox.setFont(fuenteContenido); 
        comboBox.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(5, 10, 5, 10))); 
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3; 
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(comboBox, gbc);

        infoUser = new JTextArea();
        infoUser.setEditable(false);
        infoUser.setLineWrap(true); 
        infoUser.setWrapStyleWord(true); 
        infoUser.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1, true), new EmptyBorder(10, 10, 10, 10))); 

        JScrollPane scrollPane = new JScrollPane(infoUser);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), 
                "Información de Usuarios", 0, 0, fuenteTitulo)); 
        scrollPane.setBackground(new Color(255, 255, 230)); 
        gbc.gridwidth = 4; 
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        frame.setVisible(true);
    }

    private void limpiarCampos() {
        txtUser.setText("");
        txtGame.setText("");
        txtName.setText("");
    }

    private void actualizarInfo() {
        infoUser.setText("");
    }

    private void mostrarError(Exception ex, String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje + "\nDetalles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            new MainGUI();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}