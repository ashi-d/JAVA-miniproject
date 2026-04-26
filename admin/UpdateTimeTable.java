package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseCon;
import java.awt.*;

public  class UpdateTimeTable extends JFrame {
    public JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton updateButton;
    private JButton clearButton;
    private JButton exitButton;

    public UpdateTimeTable() {
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/Images/black-background-concept-free-vector.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel1.setOpaque(false);
        backgroundPanel.add(panel1, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
        setTitle("Update Time Table");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 420);
        setLocationRelativeTo(null);
        setVisible(true);

        updateButton.addActionListener(e -> updateTimeTableInDB());
        clearButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> {
            new AdHome();
            dispose();
        });
    }

    private void updateTimeTableInDB() {
        String timetableId = textField1.getText();
        String adId        = textField2.getText();
        String department  = textField3.getText();
        String courseCode  = textField4.getText();
        String courseName  = textField5.getText();
        String time        = textField6.getText();
        String day         = textField7.getText();

        String query = "UPDATE timetable SET ad_id=?, department=?, course_code=?, " +
                "course_name=?, time=?, day=? WHERE timetable_id=?";
        try {
            Connection conn = DatabaseCon.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, adId);
            ps.setString(2, department);
            ps.setString(3, courseCode);
            ps.setString(4, courseName);
            ps.setString(5, time);
            ps.setString(6, day);
            ps.setString(7, timetableId);

            int rows = ps.executeUpdate();
            if (rows > 0) JOptionPane.showMessageDialog(null, "Time table updated successfully!");
            else JOptionPane.showMessageDialog(null, "Time table ID not found.");

            ps.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        textField1.setText(""); textField2.setText(""); textField3.setText("");
        textField4.setText(""); textField5.setText(""); textField6.setText("");
        textField7.setText("");
    }
}
