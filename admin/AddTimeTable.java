package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseCon;

public class AddTimeTable extends JFrame {
    public JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton addDetailsButton;
    private JButton CLEARButton;
    private JButton exitButton;

    public AddTimeTable() {
        setContentPane(panel1);
        setTitle("Add Time Table");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 420);
        setLocationRelativeTo(null);
        setVisible(true);

        addDetailsButton.addActionListener(e -> insertTimeTableIntoDB());
        CLEARButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> {
            new AdHome();
            dispose();
        });
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

    private void insertTimeTableIntoDB() {
        String timetableId = textField1.getText();
        String adId        = textField2.getText();
        String department  = textField3.getText();
        String courseCode  = textField4.getText();
        String courseName  = textField5.getText();
        String time        = textField6.getText();
        String day         = textField7.getText();

        String query = "INSERT INTO timetable (timetable_id, ad_id, department, course_code, course_name, time, day) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseCon.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, timetableId);
            ps.setString(2, adId);
            ps.setString(3, department);
            ps.setString(4, courseCode);
            ps.setString(5, courseName);
            ps.setString(6, time);
            ps.setString(7, day);

            int rows = ps.executeUpdate();
            if (rows > 0) JOptionPane.showMessageDialog(null, "Time table added successfully!");
            else JOptionPane.showMessageDialog(null, "Failed to add details.");

            ps.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }
}
