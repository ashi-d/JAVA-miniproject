package Lecture;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CourseMaterialPanel extends JPanel {

    JTextField courseCode, material;
    JButton addBtn;

    public CourseMaterialPanel() {

        setLayout(new GridLayout(4,2,10,10));

        courseCode = new JTextField();
        material = new JTextField();

        addBtn = new JButton("Add Material");

        add(new JLabel("Course Code"));
        add(courseCode);

        add(new JLabel("Material"));
        add(material);

        add(new JLabel(""));
        add(addBtn);

        addBtn.addActionListener(e -> addMaterial());
    }

    // ======================
    // ADD MATERIAL
    // ======================
    void addMaterial() {

        if (courseCode.getText().isEmpty() || material.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        try (Connection con = database.DatabaseCon.connect()) {

            // 🔍 Find course_id
            String find = "SELECT Course_id FROM course WHERE Course_code=?";
            PreparedStatement ps1 = con.prepareStatement(find);
            ps1.setString(1, courseCode.getText());

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int courseId = rs.getInt("Course_id");

                // 💾 Insert material
                String insert = "INSERT INTO course_material (Course_id, Material) VALUES (?,?)";
                PreparedStatement ps2 = con.prepareStatement(insert);

                ps2.setInt(1, courseId);
                ps2.setString(2, material.getText());

                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Material Added ✔");

                courseCode.setText("");
                material.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Course not found ❌");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage());
        }
    }
}