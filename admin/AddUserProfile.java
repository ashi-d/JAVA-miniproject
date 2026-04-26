package admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import database.DatabaseCon;

public class AddUserProfile extends JFrame {
    private JPanel MainPanel;
    private JPanel DetailPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JButton addDetailsToProfileButton;
    private JButton clearButton;
    private JButton addPhotoButton;
    private JButton exitBtn;

    public AddUserProfile() {
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/Images/black-background-concept-free-vector.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        MainPanel.setOpaque(false);
        backgroundPanel.add(MainPanel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);

        setTitle("Add User Profile");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        addDetailsToProfileButton.addActionListener(e -> addUserProfile());
        clearButton.addActionListener(e -> clearFields());
        addPhotoButton.addActionListener(e -> uploadProfilePicture());
        exitBtn.addActionListener(e -> { new AdHome(); dispose(); });
    }

    private void addUserProfile() {
        String sql = "INSERT INTO user (UserName, Fname, Lname, DoB, Role, Enrollment_Date, " +
                "Address, Email, Phone_No, Password, Profile_pic) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseCon.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, textField1.getText());
            ps.setString(2, textField2.getText());
            ps.setString(3, textField3.getText());
            ps.setString(4, textField4.getText());
            ps.setString(5, textField5.getText());
            ps.setString(6, textField6.getText());
            ps.setString(7, textField7.getText());
            ps.setString(8, textField8.getText());
            try { ps.setInt(9, Integer.parseInt(textField9.getText())); }
            catch (NumberFormatException ex) { ps.setNull(9, java.sql.Types.INTEGER); }
            ps.setString(10, textField10.getText());
            ps.setString(11, textField11.getText());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "User profile added successfully.");
                clearFields();
            } else JOptionPane.showMessageDialog(null, "Failed to add user profile.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void uploadProfilePicture() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select Profile Picture");
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            textField11.setText(fc.getSelectedFile().getName());
    }

    private void clearFields() {
        textField1.setText(""); textField2.setText(""); textField3.setText("");
        textField4.setText(""); textField5.setText(""); textField6.setText("");
        textField7.setText(""); textField8.setText(""); textField9.setText("");
        textField10.setText(""); textField11.setText("");
    }
}