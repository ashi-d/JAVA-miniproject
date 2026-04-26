package student;

import database.DatabaseCon;
import database.Session;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;

public class ShowAttendanceEligibility extends JFrame {
    private JPanel mainPanel;
    private JScrollPane attEligiScrollPane;
    private JPanel attEligiPanel;
    private JLabel attEligiLbl;
    private JTable attEligiTable;
    private JButton exitButton;

    public ShowAttendanceEligibility() {
        setTitle("Attendance Eligibility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 890);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);

        DefaultTableModel model = new DefaultTableModel(null, new String[]{"Course Code", "Course Name", "Eligibility Status"});
        attEligiTable.setModel(model);
        getAllAttendanceCounts();

        exitButton.addActionListener(e -> { new StuHome(); dispose(); });
    }

    public void checkAndShowAttendanceEligibility(String courseCode) {
        try (Connection con = DatabaseCon.connect()) {
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT count(*) FROM Attendance a JOIN Course c ON a.Course_code=c.Course_code JOIN Student s ON a.Stu_id=s.Stu_id JOIN User u ON s.UserName=u.UserName WHERE u.UserName=? AND a.Course_code=? AND a.Status='Present'");
            ps1.setString(1, Session.loggedInUsername); ps1.setString(2, courseCode);
            ResultSet rs1 = ps1.executeQuery();
            int presentCount = rs1.next() ? rs1.getInt(1) : 0;

            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT COUNT(*) FROM Attendance a JOIN Course c ON a.Course_code=c.Course_code JOIN Student s ON a.Stu_id=s.Stu_id JOIN User u ON s.UserName=u.UserName WHERE u.UserName=? AND a.Course_code=?");
            ps2.setString(1, Session.loggedInUsername); ps2.setString(2, courseCode);
            ResultSet rs2 = ps2.executeQuery();
            int totalCount = rs2.next() ? rs2.getInt(1) : 0;

            int medicalCount = getApprovedMediCount(courseCode);
            double pct = calculateAttendancePercentageAfterMedical(presentCount, totalCount, medicalCount);
            String status = pct >= 80 ? "Eligible" : "Not Eligible";

            ((DefaultTableModel) attEligiTable.getModel()).addRow(new Object[]{courseCode, getCourseName(courseCode), status});
        } catch (Exception e) { e.printStackTrace(); }
    }

    public int getApprovedMediCount(String courseCode) {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COUNT(*) FROM medical m JOIN Course c ON m.Course_code=c.Course_code JOIN Student s ON m.Stu_id=s.Stu_id JOIN User u ON s.UserName=u.UserName WHERE u.UserName=? AND m.Course_code=? AND m.Status='Approved'")) {
            ps.setString(1, Session.loggedInUsername); ps.setString(2, courseCode);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) { return 0; }
    }

    public double calculateAttendancePercentageAfterMedical(int present, int total, int medical) {
        return total == 0 ? 0.0 : ((double)(present + medical) / total) * 100;
    }

    public void getAllAttendanceCounts() {
        for (String code : getAllCourseCodes()) checkAndShowAttendanceEligibility(code);
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

    public String getCourseName(String courseCode) {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement("SELECT Course_name FROM Course WHERE Course_code=?")) {
            ps.setString(1, courseCode);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("Course_name") : "Unknown";
        } catch (Exception e) { return "Unknown"; }
    }
}
