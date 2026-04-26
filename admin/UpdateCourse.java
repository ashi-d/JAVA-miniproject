package admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import database.DatabaseCon;

public class UpdateCourse extends JFrame {
    public JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton UPDATEButton;
    private JButton CLEARButton;
    private JButton exitButton;

    public UpdateCourse() {
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

        setTitle("Update Course");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 420);
        setLocationRelativeTo(null);
        setVisible(true);

        UPDATEButton.addActionListener(e -> updateCourseInDB());
        CLEARButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> { new AdHome(); dispose(); });
    }

    private void clearFields() {
        textField1.setText(""); textField2.setText(""); textField3.setText("");
        textField4.setText(""); textField5.setText(""); textField6.setText("");
    }

    private void updateCourseInDB() {
        String courseId   = textField1.getText();
        String courseCode = textField2.getText();
        String lecId      = textField3.getText();
        String courseType = textField4.getText();
        String courseName = textField5.getText();
        String credit     = textField6.getText();

        String query = "UPDATE course SET course_code=?, lec_id=?, course_type=?, " +
                "course_name=?, credit=? WHERE course_id=?";
        try {
            Connection conn = DatabaseCon.connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, courseCode);
            ps.setString(2, lecId);
            ps.setString(3, courseType);
            ps.setString(4, courseName);
            ps.setString(5, credit);
            ps.setString(6, courseId);

            int rows = ps.executeUpdate();
            if (rows > 0) JOptionPane.showMessageDialog(null, "Course updated successfully.");
            else JOptionPane.showMessageDialog(null, "Course ID not found.");

            ps.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }
}