package student;

import database.DatabaseCon;
import database.Session;
import javax.swing.*;
import java.io.File;
import java.sql.*;

public class UpdateStudentProfile extends JFrame {
    private JPanel mainPanel;
    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JTextField addressTxt;
    private JTextField emailTxt;
    private JTextField pNoTxt;
    private JTextField proPicTxt;
    private JButton uploadAnImageButton;
    private JButton updateProfileButton;
    private JPanel detailPanel;
    private JLabel updateProfileMainLbl;
    private JLabel firstNameLbl;
    private JLabel lastNameLbl;
    private JLabel addressLbl;
    private JLabel pNoLbl;
    private JLabel emailLbl;
    private JButton cancelButton;

    public UpdateStudentProfile() {
        setContentPane(mainPanel);
        setTitle("Update Student Profile");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setVisible(true);

        uploadAnImageButton.addActionListener(e -> uploadProfilePicture());
        updateProfileButton.addActionListener(e -> updateStudentDetails());
        cancelButton.addActionListener(e -> { new StuHome(); dispose(); });
    }

    public void updateStudentDetails() {
        if (!validateFields()) return;

        File file = new File(proPicTxt.getText());
        String proPic = file.getName();

        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE User SET FName=?, LName=?, Address=?, Email=?, Phone_No=?, Profile_pic=? WHERE UserName=?")) {
            ps.setString(1, firstNameTxt.getText());
            ps.setString(2, lastNameTxt.getText());
            ps.setString(3, addressTxt.getText());
            ps.setString(4, emailTxt.getText());
            ps.setString(5, pNoTxt.getText());
            ps.setString(6, proPic);
            ps.setString(7, Session.loggedInUsername);

            int rows = ps.executeUpdate();
            JOptionPane.showMessageDialog(null, rows > 0 ? "Student Profile Updated" : "Student Profile Not Updated");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Update Failed");
            e.printStackTrace();
        }
    }

    public void uploadProfilePicture() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select Profile Picture");
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            proPicTxt.setText(fc.getSelectedFile().getName());
    }

    public boolean validateFields() {
        String email = emailTxt.getText().trim();
        String phone = pNoTxt.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid Email Address!");
            return false;
        }
        if (!phone.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid Phone Number! Should be 10 digits.");
            return false;
        }
        return true;
    }
}