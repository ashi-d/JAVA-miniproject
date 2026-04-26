package student;

import database.DatabaseCon;
import database.Session;
import noticesViewing.NoticeViewing;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class StuHome extends JFrame implements NoticeViewing {
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JPanel btnPanel;
    private JPanel cardMainPanel;
    private JButton profileButton;
    private JButton coursesButton;
    private JButton gradeAndGPAButton;
    private JButton attendanceButton;
    private JButton medicalButton;
    private JButton timeTableButton;
    private JTextField fNameTxt;
    private JTextField lNameTxt;
    private JTextField addressTxt;
    private JTextField emailTxt;
    private JTextField pNoTxt;
    private JTextField roleTxt;
    private JPanel imgMainPanel;
    private JPanel imgPanel;
    private JLabel roleLbl;
    private JLabel pNoLbl;
    private JLabel emailLbl;
    private JLabel addressLbl;
    private JLabel lNameLbl;
    private JLabel fNameLbl;
    private JPanel detailPanel;
    private JLabel profileHeadingLbl;
    private JPanel profileCard;
    private JLabel imageLbl;
    private JButton noticeButton;
    private JLabel stuManaSysLbl;
    private JLabel FoTLbl;
    private JPanel coursesCard;
    private JLabel CoursesHeadingLbl;
    private JComboBox comboBoxCourses;
    private JLabel selectCourseLbl;
    private JPanel gradeGPACard;
    private JLabel gradeGPAHeadingLbl;
    private JComboBox selectCrsComboBox;
    private JTextField yourGradeTxt;
    private JTextField sgpaTxt;
    private JTextArea gradetxtArea;
    private JPanel gradeTxtAreaPanel;
    private JLabel yourGradeLbl;
    private JLabel selectCrsLbl;
    private JLabel sgpaLbl;
    private JButton logOutButton;
    private JPanel attendanceCard;
    private JPanel medicalCard;
    private JPanel timeTableCard;
    private JPanel noticeCard;
    private JLabel attendanceHeadingLbl;
    private JLabel medicalHeadingLbl;
    private JLabel noticeHeadingLbl;
    private JComboBox selectAttCourseCombo;
    private JComboBox selectCrsTypeCombo;
    private JLabel selectAttCourseLbl;
    private JButton clearButton;
    private JButton OKButton;
    private JPanel attViewPanel;
    private JTable mediDetailsTable;
    private JPanel mediDetailsTblPanel;
    private JComboBox selectTitleCombo;
    private JLabel selectTitleLbl;
    private JLabel timeTableHeadingLbl;
    private JPanel noticeTxtAreaPanel;
    private JTextArea noticeTxtArea;
    private JPanel timeTablePanel;
    private JTable timeTableTable;
    private JTable attTable;
    private JScrollPane attScrollPane;
    private JScrollPane mediScrollPane;
    private JScrollPane noticeScrollPane;
    private JScrollPane timeTableScrollPane;
    private JButton updateProfileButton;
    private JTable Course_materials;
    private JButton updateTimeTableButton;
    private JButton addButton;
    private JButton checkEligibilityButton;
    private JButton checkAttendanceEligibilityButton;
    private JButton deleteProfilePictureButton;
    private JButton okButtonCourses;
    private JPanel displayDetailsPanel;
    private JButton downloadButton;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public StuHome() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Home");
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setVisible(true);

        displayProfileDetails();
        showProfilePicture(imageLbl);

        selectTitleCombo.setSelectedIndex(-1);
        noticeTxtArea.setText("");

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

        profileButton.setFocusPainted(false);
        coursesButton.setFocusPainted(false);
        attendanceButton.setFocusPainted(false);
        medicalButton.setFocusPainted(false);
        timeTableButton.setFocusPainted(false);
        noticeButton.setFocusPainted(false);
        gradeAndGPAButton.setFocusPainted(false);

        profileButton.addActionListener(e -> cardLayout.show(cardMainPanel, "profileCard"));
        coursesButton.addActionListener(e -> cardLayout.show(cardMainPanel, "coursesCard"));
        gradeAndGPAButton.addActionListener(e -> { cardLayout.show(cardMainPanel, "gradeGPACard"); calculateGPA(); });
        attendanceButton.addActionListener(e -> cardLayout.show(cardMainPanel, "attendanceCard"));
        medicalButton.addActionListener(e -> {
            cardLayout.show(cardMainPanel, "medicalCard");
            DefaultTableModel model = new DefaultTableModel(null, new String[]{"Medical_id", "Course_code", "Course_name", "Week_No", "Day_No", "Status"});
            mediDetailsTable.setModel(model);
            viewMedicalStatus();
        });
        timeTableButton.addActionListener(e -> { cardLayout.show(cardMainPanel, "timeTableCard"); showTimeTable(); });
        noticeButton.addActionListener(e -> {
            cardLayout.show(cardMainPanel, "noticeCard");
            addNoticeTitlesToComboBox();
            selectTitleCombo.setSelectedIndex(-1);
            noticeTxtArea.setText("");
        });
        updateProfileButton.addActionListener(e -> { new UpdateStudentProfile(); dispose(); });
        logOutButton.addActionListener(e -> { new Login(); dispose(); });

        selectCrsComboBox.addActionListener(e -> {
            int idx = selectCrsComboBox.getSelectedIndex();
            List<String> codes = getAllCourseCodes();
            if (idx >= 0 && idx < codes.size()) getGrade(codes.get(idx));
        });

        selectTitleCombo.addActionListener(e -> {
            if (selectTitleCombo.getSelectedIndex() != -1)
                displayNoticeContent((String) selectTitleCombo.getSelectedItem());
            else noticeTxtArea.setText("");
        });

        checkEligibilityButton.addActionListener(e -> { new ShowCAEligibility(); dispose(); });

        OKButton.addActionListener(e -> {
            int idx = selectAttCourseCombo.getSelectedIndex();
            if (idx >= 0) viewAttendance(getAllCourseCodes().get(idx));
            else JOptionPane.showMessageDialog(null, "Please select a course");
        });

        clearButton.addActionListener(e -> {
            selectAttCourseCombo.setSelectedIndex(0);
            attTable.setModel(new DefaultTableModel(new String[]{"Attendance Id", "Lecture Hour", "Week No", "Day No", "Status", "Course Type"}, 0));
        });

        checkAttendanceEligibilityButton.addActionListener(e -> { new ShowAttendanceEligibility(); dispose(); });

        deleteProfilePictureButton.addActionListener(e -> {
            deleteProfilePicture(imageLbl);
            deleteProfilePictureButton.setEnabled(false);
        });

        okButtonCourses.addActionListener(e -> {
            int idx = comboBoxCourses.getSelectedIndex();
            if (idx >= 0) viewCourseMaterials(getAllCourseCodes().get(idx));
            else JOptionPane.showMessageDialog(null, "Please select a course");
        });

        downloadButton.addActionListener(e -> {
            int row = Course_materials.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(mainPanel, "Please select a material."); return; }
            downloadMaterial(Session.loggedInUsername, Course_materials.getValueAt(row, 1).toString(), Course_materials.getValueAt(row, 3).toString());
        });
    }

    public double calculateGPA() {
        double totalGradePoints = 0; int totalCredits = 0;
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT g.Final_Exam_Mark, c.Credit FROM grade_letters g JOIN course c ON g.Course_code = c.Course_code WHERE g.Stu_id = ?")) {
            ps.setString(1, Session.loggedInUsername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double mark = rs.getDouble("Final_Exam_Mark");
                int credits = rs.getInt("Credit");
                totalGradePoints += convertMarkToPoints(mark) * credits;
                totalCredits += credits;
            }
            if (totalCredits > 0) { double gpa = totalGradePoints / totalCredits; sgpaTxt.setText(String.format("%.2f", gpa)); return gpa; }
            else { JOptionPane.showMessageDialog(null, "No grades found"); return 0.0; }
        } catch (Exception e) { e.printStackTrace(); return 0.0; }
    }

    private double convertMarkToPoints(double m) {
        if (m >= 90) return 4.0; if (m >= 84) return 4.0; if (m >= 75) return 3.7;
        if (m >= 70) return 3.3; if (m >= 65) return 3.0; if (m >= 60) return 2.7;
        if (m >= 55) return 2.3; if (m >= 50) return 2.0; if (m >= 45) return 1.7;
        if (m >= 40) return 1.3; if (m >= 35) return 1.0; return 0.0;
    }

    public void getGrade(String courseCode) {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT Grade FROM Grade_Letters WHERE Course_code=? AND Stu_id=?")) {
            ps.setString(1, courseCode); ps.setString(2, Session.loggedInUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) yourGradeTxt.setText(rs.getString("Grade"));
            else JOptionPane.showMessageDialog(null, "No Grades Found");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void displayProfileDetails() {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT FName,LName,Address,Email,Phone_No,Role FROM User WHERE UserName=?")) {
            ps.setString(1, Session.loggedInUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fNameTxt.setText(rs.getString("FName")); lNameTxt.setText(rs.getString("LName"));
                addressTxt.setText(rs.getString("Address")); emailTxt.setText(rs.getString("Email"));
                pNoTxt.setText(rs.getString("Phone_No")); roleTxt.setText(rs.getString("Role"));
            } else JOptionPane.showMessageDialog(null, "No Profile Found");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void showTimeTable() {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM TimeTable")) {
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(null, new String[]{"Department", "Course_Code", "Course_Name", "Time", "Day"});
            while (rs.next()) model.addRow(new Object[]{rs.getString("Department"), rs.getString("Course_Code"), rs.getString("Course_Name"), rs.getString("Time"), mapDayNumberToName(rs.getInt("Day"))});
            timeTableTable.setModel(model);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String mapDayNumberToName(int d) {
        switch (d) { case 1: return "Monday"; case 2: return "Tuesday"; case 3: return "Wednesday"; case 4: return "Thursday"; case 5: return "Friday"; default: return "Unknown"; }
    }

    @Override
    public void addNoticeTitlesToComboBox() {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Notice")) {
            ResultSet rs = ps.executeQuery();
            selectTitleCombo.removeAllItems();
            selectTitleCombo.addItem("-- Select a Notice --");
            while (rs.next()) selectTitleCombo.addItem(rs.getString("Title"));
            selectTitleCombo.setSelectedIndex(-1);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void displayNoticeContent(String title) {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT Notice_id FROM Notice WHERE Title=?")) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                File f = new File("notices/notice_" + rs.getString("Notice_id") + ".txt");
                if (!f.exists()) { noticeTxtArea.setText("Notice file not found."); return; }
                try (BufferedReader r = new BufferedReader(new FileReader(f))) {
                    StringBuilder sb = new StringBuilder(); String line;
                    while ((line = r.readLine()) != null) sb.append(line).append("\n");
                    noticeTxtArea.setText(sb.toString());
                }
            } else noticeTxtArea.setText("Notice not found.");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void viewMedicalStatus() {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT med.Medical_id, med.Course_code, med.Week_No, med.Day_No, med.Status, c.Course_Name FROM Medical med JOIN Course c ON med.Course_code = c.Course_code JOIN Student s ON med.Stu_id = s.Stu_id JOIN User u ON s.UserName = u.UserName WHERE u.UserName=?")) {
            ps.setString(1, Session.loggedInUsername);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) mediDetailsTable.getModel();
            model.setRowCount(0);
            boolean found = false;
            while (rs.next()) { found = true; model.addRow(new Object[]{rs.getString("Medical_id"), rs.getString("Course_code"), rs.getString("Course_Name"), rs.getString("Week_No"), rs.getString("Day_No"), rs.getString("Status")}); }
            if (!found) JOptionPane.showMessageDialog(null, "No Medical Found");
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void viewAttendance(String courseCode) {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT a.Attendance_id, a.Lec_hour, a.Week_No, a.Day_No, a.Status, a.Course_type FROM Attendance a JOIN Course c ON c.Course_code = a.Course_code JOIN Student s ON a.Stu_id = s.Stu_id JOIN User u ON s.UserName = u.UserName WHERE u.UserName=? AND a.Course_code=?")) {
            ps.setString(1, Session.loggedInUsername); ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Attendance Id", "Lecture Hour", "Week No", "Day No", "Status", "Course Type"}, 0);
            while (rs.next()) model.addRow(new Object[]{rs.getString("Attendance_id"), rs.getString("Lec_hour"), rs.getString("Week_No"), rs.getString("Day_No"), rs.getString("Status"), rs.getString("Course_type")});
            attTable.setModel(model);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void showProfilePicture(JLabel imageLbl) {
        try (Connection con = DatabaseCon.connect()) {
            PreparedStatement ps = con.prepareStatement("SELECT Profile_pic FROM User WHERE UserName=?");
            ps.setString(1, Session.loggedInUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String fn = rs.getString("Profile_pic");
                if (fn == null || fn.trim().isEmpty()) fn = "default.png";
                String path = "user_Pro_Pic/" + fn;
                if (!new File(path).exists()) path = "user_Pro_Pic/default.png";
                int w = imageLbl.getWidth() == 0 ? 150 : imageLbl.getWidth();
                int h = imageLbl.getHeight() == 0 ? 150 : imageLbl.getHeight();
                imageLbl.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
                imageLbl.repaint();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteProfilePicture(JLabel imageLbl) {
        try (Connection con = DatabaseCon.connect()) {
            PreparedStatement ps = con.prepareStatement("UPDATE User SET Profile_pic=NULL WHERE UserName=?");
            ps.setString(1, Session.loggedInUsername);
            if (ps.executeUpdate() > 0) {
                int w = imageLbl.getWidth() == 0 ? 150 : imageLbl.getWidth();
                int h = imageLbl.getHeight() == 0 ? 150 : imageLbl.getHeight();
                imageLbl.setIcon(new ImageIcon(new ImageIcon("user_Pro_Pic/default.png").getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
                imageLbl.repaint();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getAllCourseCodes() {
        List<String> codes = new ArrayList<>();
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT Course_code FROM Course")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) codes.add(rs.getString("Course_code"));
        } catch (Exception e) { e.printStackTrace(); }
        return codes;
    }

    private boolean listenerAdded = false;

    public void viewCourseMaterials(String courseCode) {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT Material_id,Course_code,Lec_id,file_path,uploaded_on FROM lecture_materials WHERE Course_code=?")) {
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel() { public boolean isCellEditable(int r, int c) { return false; } };
            model.addColumn("Material ID"); model.addColumn("Course Code"); model.addColumn("Lecturer ID"); model.addColumn("Material"); model.addColumn("Uploaded On");
            while (rs.next()) model.addRow(new Object[]{rs.getString("Material_id"), rs.getString("Course_code"), rs.getString("Lec_id"), rs.getString("file_path"), rs.getTimestamp("uploaded_on")});
            Course_materials.setModel(model);
            if (!listenerAdded) {
                Course_materials.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        int row = Course_materials.rowAtPoint(e.getPoint());
                        int col = Course_materials.columnAtPoint(e.getPoint());
                        if (row >= 0 && col == 3) openMaterial("course_materials" + File.separator + Course_materials.getValueAt(row, 1) + File.separator + Course_materials.getValueAt(row, col));
                    }
                });
                listenerAdded = true;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void openMaterial(String filePath) {
        File file = new File(filePath);
        if (file.exists()) { try { Desktop.getDesktop().open(file); } catch (IOException e) { JOptionPane.showMessageDialog(mainPanel, "Error: " + e.getMessage()); } }
        else JOptionPane.showMessageDialog(mainPanel, "File does not exist.");
    }

    private void downloadMaterial(String user, String courseCode, String fileName) {
        File src = new File("course_materials" + File.separator + courseCode + File.separator + fileName);
        if (!src.exists()) { JOptionPane.showMessageDialog(mainPanel, "File not found: " + src.getPath()); return; }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(fileName));
        if (fc.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            File dest = fc.getSelectedFile();
            if (dest.exists()) { if (JOptionPane.showConfirmDialog(null, "Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return; }
            try { Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING); JOptionPane.showMessageDialog(mainPanel, "Downloaded successfully"); }
            catch (IOException e) { JOptionPane.showMessageDialog(mainPanel, "Error: " + e.getMessage()); }
        }
    }
}
