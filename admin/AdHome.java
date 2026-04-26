package admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseCon;
import database.DatabaseCon;
import database.Session;
import student.Login;

public class AdHome extends JFrame {
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel cardPanel;
    private JButton adminProfileButton;
    private JButton courseButton;
    private JButton timeTableButton;
    private JButton noticeButton;
    private JButton userProfileButton;
    private JButton logOutButton;
    private JPanel ProfileCard;
    private JPanel CourseCard;
    private JPanel TimeTableCard;
    private JPanel NoticeCard;
    private JPanel UpdateUprofileCard;
    private JTable CourseTbale;
    private JButton addCourseButton;
    private JButton UpdataeCourseButton;
    private JButton deleteButton;
    private JTable TimeTable1;
    private JButton addTimetableButton;
    private JButton UpdateTimetableButton;
    private JButton deleteButton1;
    private JTable NoticeTable;
    private JTextArea textArea1;
    private JButton AddNoticeButton;
    private JButton deleteNoticeButton;
    private JButton viewNoticeButton;
    private JLabel imgDisplayLbl;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton updateProfileButton;
    private JButton logOutButton1;
    private JTextField textField7;
    private JTextField fnametxt;
    private JTextField lNametxt;
    private JTextField dobtxt;
    private JTextField roletxt;
    private JTextField enrolltxt;
    private JTextField addresstxt;
    private JTextField emailtxt;
    private JTextField pNotxt;
    private JTextField pwTxt;
    private JTextField proPictxt;
    private JButton addPhotoButton;
    private JButton clearButton;
    private JButton updateProfileButton1;
    private JButton exitButton;
    private JButton viewAllUsersButton;
    private JButton addNewUserButton;
    private JButton deleteProfilePictureButton;

    public AdHome() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin Home");
        pack();
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setVisible(true);

        loadAdminProfile();

        // ── Sidebar buttons ──
        adminProfileButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "Profile");
            loadAdminProfile();
        });

        courseButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "Course");
            loadCourseData();
        });

        timeTableButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "TimeTableCard");
            loadTimeTableData();
        });

        noticeButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "Notice");
            displayNoticeContent();
        });

        userProfileButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "updateUProfilesPanel");
        });

        logOutButton.addActionListener(e -> {
            new Login();
            dispose();
        });

        // ── Course buttons ──
        addCourseButton.addActionListener(e -> {
            new AddCourse();
            dispose();
        });

        UpdataeCourseButton.addActionListener(e -> {
            new UpdateCourse();
            dispose();
        });

        deleteButton.addActionListener(e -> {
            int row = CourseTbale.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(null, "Please select a course to delete."); return; }
            String courseId = CourseTbale.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Delete Course ID: " + courseId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) { deleteCourseFromDB(courseId); loadCourseData(); }
        });

        // ── TimeTable buttons ──
        addTimetableButton.addActionListener(e -> {
            new AddTimeTable();
            dispose();
        });

        UpdateTimetableButton.addActionListener(e -> {
            new UpdateTimeTable();
            dispose();
        });

        deleteButton1.addActionListener(e -> deleteSelectedTimetable());

        // ── Notice buttons ──
        AddNoticeButton.addActionListener(e -> {
            new AddNotices();
            dispose();
        });

        deleteNoticeButton.addActionListener(e -> {
            int row = NoticeTable.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(null, "Please select a notice to delete."); return; }
            String noticeId = NoticeTable.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Delete Notice ID: " + noticeId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) { deleteNotice(noticeId); displayNoticeContent(); }
        });

        viewNoticeButton.addActionListener(e -> loadSelectedNotice());

        // ── Profile buttons ──
        updateProfileButton.addActionListener(e -> {
            new ProfileUpdate();
            dispose();
        });

        logOutButton1.addActionListener(e -> {
            new Login();
            dispose();
        });

        // ── UpdateUprofileCard buttons ──
        addPhotoButton.setEnabled(false);
        clearButton.addActionListener(e -> clearFields());
        updateProfileButton1.addActionListener(e -> updateProfile());
        exitButton.addActionListener(e -> dispose());

        viewAllUsersButton.addActionListener(e -> {
            ViewUserProfiles viewPanel = new ViewUserProfiles(cardPanel, "updateUProfilesPanel");
            cardPanel.add(viewPanel.Mainpanel, "ViewUsersCard");
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "ViewUsersCard");
        });

        addNewUserButton.addActionListener(e -> {
            new AddUserProfile();
            dispose();
        });

        deleteProfilePictureButton.setEnabled(false);
    }

    private void loadAdminProfile() {
        try (Connection conn = DatabaseCon.connect();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE role = 'Admin' LIMIT 1");

            if (rs.next()) {
                // since you only have "name"
                textField1.setText(rs.getString("name")); // Full name

                textField2.setText("U_name"); // No lname, so leave empty OR remove this field

                textField3.setText(rs.getString("Address"));
                textField4.setText(rs.getString("Email"));
                textField5.setText("");
                textField6.setText("Role");

            } else {
                JOptionPane.showMessageDialog(null, "No Admin profile found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfile() {

        String sql = "UPDATE user SET Name=?, Address=?, Email=?, Password=?, Role=? WHERE U_name=?";

        try (Connection conn = DatabaseCon.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fnametxt.getText());
            ps.setString(2, addresstxt.getText());
            ps.setString(3, emailtxt.getText());
            ps.setString(4, pwTxt.getText());
            ps.setString(5, roletxt.getText());
            ps.setString(6, textField7.getText());

            int rows = ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    rows > 0 ? "Profile updated successfully." : "No profile found.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }



    private void clearFields() {
        textField7.setText(""); fnametxt.setText(""); lNametxt.setText("");
        dobtxt.setText(""); roletxt.setText(""); enrolltxt.setText("");
        addresstxt.setText(""); emailtxt.setText(""); pNotxt.setText("");
        pwTxt.setText(""); proPictxt.setText("");
    }

    private void loadCourseData() {
        try (Connection conn = DatabaseCon.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM course");
            DefaultTableModel model = new DefaultTableModel() {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            model.addColumn("Course ID"); model.addColumn("Course Code");
            model.addColumn("Lecturer ID"); model.addColumn("Course Type");
            model.addColumn("Course Name"); model.addColumn("Credit");
            while (rs.next()) model.addRow(new Object[]{
                    rs.getString("course_id"), rs.getString("course_code"),
                    rs.getString("lec_id"), rs.getString("course_type"),
                    rs.getString("course_name"), rs.getInt("credit")});
            CourseTbale.setModel(model);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteCourseFromDB(String courseId) {
        try (Connection conn = DatabaseCon.connect();
             Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate("DELETE FROM course WHERE course_id = '" + courseId + "'");
            JOptionPane.showMessageDialog(null, rows > 0 ?
                    "Course deleted." : "Course not found.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTimeTableData() {
        try (Connection conn = DatabaseCon.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM timetable");
            DefaultTableModel model = new DefaultTableModel() {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            model.addColumn("TimeTable_ID"); model.addColumn("Admin ID");
            model.addColumn("Department"); model.addColumn("Course Code");
            model.addColumn("Course Name"); model.addColumn("Time"); model.addColumn("Day");
            while (rs.next()) model.addRow(new Object[]{
                    rs.getString("timetable_id"), rs.getString("ad_id"),
                    rs.getString("department"), rs.getString("course_code"),
                    rs.getString("course_name"), rs.getString("time"),
                    convertDay(rs.getString("day"))});
            TimeTable1.setModel(model);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteSelectedTimetable() {
        int row = TimeTable1.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(null, "Please select a timetable entry."); return; }
        String id = TimeTable1.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(null,
                "Delete ID: " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseCon.connect();
                 Statement stmt = conn.createStatement()) {
                int rows = stmt.executeUpdate("DELETE FROM timetable WHERE timetable_id = '" + id + "'");
                JOptionPane.showMessageDialog(null, rows > 0 ? "Deleted." : "Not found.");
                loadTimeTableData();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private String convertDay(String d) {
        switch (d) {
            case "1": return "Monday"; case "2": return "Tuesday";
            case "3": return "Wednesday"; case "4": return "Thursday";
            default: return "Unknown";
        }
    }

    private void displayNoticeContent() {
        try (Connection conn = DatabaseCon.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM notice");
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Notice ID", "Admin ID", "Title", "Date"}, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            while (rs.next()) model.addRow(new Object[]{
                    rs.getString("Notice_id"), rs.getString("Ad_id"),
                    rs.getString("Title"), rs.getDate("Date")});
            NoticeTable.setModel(model);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadSelectedNotice() {
        int row = NoticeTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(null, "Please select a notice."); return; }
        String noticeId = NoticeTable.getValueAt(row, 0).toString();
        File file = new File("notices/notice_" + noticeId + ".txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            textArea1.setText(sb.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot load: " + file.getName());
        }
    }

    private void deleteNotice(String noticeId) {
        try (Connection conn = DatabaseCon.connect();
             Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate("DELETE FROM notice WHERE Notice_id = '" + noticeId + "'");
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Notice deleted.");
                DefaultTableModel model = (DefaultTableModel) NoticeTable.getModel();
                int row = NoticeTable.getSelectedRow();
                if (row != -1) model.removeRow(row);
            } else JOptionPane.showMessageDialog(null, "Notice not found.");
        } catch (Exception e) { e.printStackTrace(); }
    }
}