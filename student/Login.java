package student;

import admin.AdHome;
import Lecture.*;
import database.DatabaseCon;
import database.Session;
import javax.swing.*;
import java.sql.*;
import Technical_officer.toHome;
import java.net.URL;

public class Login extends JFrame {
    public JPanel mainPanel;
    private JTextField userNameTextField;
    private JPasswordField passwordPasswordField;
    private JButton cancelButton;
    private JButton logInButton;
    private JPanel insideMainPanel;
    private JPanel welcomePanel;
    private JLabel stuManSysLbl;
    private JLabel FoTLabel;
    private JLabel welcomeLabel;
    private JPanel loginFormMain;
    private JPanel loginFormPanel;
    private JPanel loginFormInsidePanel;
    private JLabel LoginLbl;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private DatabaseCon databaseConnection;

    public Login() {
        setContentPane(mainPanel);
        setTitle("Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(2000, 890);
        setVisible(true);
        getRootPane().setDefaultButton(logInButton);

        logInButton.setFocusPainted(false);
        cancelButton.setFocusPainted(false);

        logInButton.addActionListener(e -> loginAuthentication());
        cancelButton.addActionListener(e -> {
            userNameTextField.setText("");
            passwordPasswordField.setText("");
        });


    }


    public void loginAuthentication() {
        String inputUserName = userNameTextField.getText();
        String inputPassword = String.valueOf(passwordPasswordField.getPassword());

        if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
            JOptionPane.showMessageDialog(loginFormMain, "Please enter both username and password!");
            return;
        }

        databaseConnection = new DatabaseCon();
        connection = databaseConnection.connect();

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT U_name, Password FROM user WHERE U_name = ?");
            ps.setString(1, inputUserName);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String dbuserName = resultSet.getString("U_name");
                String dbpassword = resultSet.getString("Password");

                if (inputPassword.equals(dbpassword)) {
                    String userNamelower = inputUserName.toLowerCase();
                    Session.loggedInUsername = dbuserName;

                    if (userNamelower.startsWith("admin")) {
                        JOptionPane.showMessageDialog(null, "Admin Login Successful");
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        topFrame.dispose();
                        new AdHome().setVisible(true);

                    } else if (userNamelower.startsWith("tg")) {
                        JOptionPane.showMessageDialog(null, "Student Login Successful");
                        new StuHome();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        topFrame.dispose();

                    } else if (userNamelower.startsWith("lec")) {
                        JOptionPane.showMessageDialog(null, "Lecturer Login Successful");
                        new LecHome(dbuserName);
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        topFrame.dispose();

                    } else if (userNamelower.startsWith("to_")) {
                        JOptionPane.showMessageDialog(null, "Technical Officer Login Successful");
                        new toHome();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        topFrame.dispose();

                    } else {
                        JOptionPane.showMessageDialog(null, "Unknown user role!");
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFormMain, "Incorrect password!");
                }
            } else {
                JOptionPane.showMessageDialog(loginFormMain, "User not found!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.out.println("Error closing: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
