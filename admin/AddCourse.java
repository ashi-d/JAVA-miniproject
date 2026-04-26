package admin;

import javax.swing.*;

public class AddCourse extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton addCourseButton;
    private JButton clearButton;
    private JButton exitButton;

    public AddCourse() {
        setContentPane(panel1);
        setTitle("Add Course");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addCourseButton.addActionListener(e -> insertCourseToDB());
        clearButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> dispose());
    }

    private void insertCourseToDB() {
        // DB code මෙතට දාන්න
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField1.requestFocus();
    }

    public static void main(String[] args) {
        new AddCourse();
    }
}