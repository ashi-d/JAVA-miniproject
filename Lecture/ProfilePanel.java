package Lecture;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProfilePanel extends JPanel {

    JTextField name, email, dept;
    JButton updateBtn;

    int userId; // 🔥 store user id

    // 🔥 CONSTRUCTOR WITH PARAMETER
    public ProfilePanel(int userId) {

        this.userId = userId;

        setLayout(new GridLayout(4,2,10,10));

        name = new JTextField();
        email = new JTextField();
        dept = new JTextField();

        updateBtn = new JButton("Update Profile");

        add(new JLabel("Name"));
        add(name);

        add(new JLabel("Email"));
        add(email);

        add(new JLabel("Department"));
        add(dept);

        add(new JLabel(""));
        add(updateBtn);

        loadProfile();

        updateBtn.addActionListener(e -> updateProfile());
    }

    // ======================
    // LOAD PROFILE
    // ======================
    void loadProfile() {

        try (Connection con = database.DatabaseCon.connect()) {

            String sql =
                    "SELECT u.`Name`, u.Email, l.Department " +
                            "FROM lecture l " +
                            "JOIN user u ON l.User_id = u.User_id " +
                            "WHERE u.User_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                name.setText(rs.getString("Name"));
                email.setText(rs.getString("Email"));
                dept.setText(rs.getString("Department"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Load Error: " + e.getMessage());
        }
    }

    // ======================
    // UPDATE PROFILE
    // ======================
    void updateProfile() {

        try (Connection con = database.DatabaseCon.connect()) {

            // update user table
            String sql1 = "UPDATE user SET Name=?, Email=? WHERE User_id=?";
            PreparedStatement ps1 = con.prepareStatement(sql1);

            ps1.setString(1, name.getText());
            ps1.setString(2, email.getText());
            ps1.setInt(3, userId);

            ps1.executeUpdate();

            // update lecture table
            String sql2 = "UPDATE lecture SET Department=? WHERE User_id=?";
            PreparedStatement ps2 = con.prepareStatement(sql2);

            ps2.setString(1, dept.getText());
            ps2.setInt(2, userId);

            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Profile Updated ✔");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Update Error: " + e.getMessage());
        }
    }
}