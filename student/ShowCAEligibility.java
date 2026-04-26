package student;

import database.DatabaseCon;
import database.Session;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Arrays;

public class ShowCAEligibility extends JFrame {
    private JLabel eligibilityLbl;
    private JPanel mainPanel;
    private JPanel eligibilityPanel;
    private JTable eligibilityTable;
    private JButton exitBtn;
    private JScrollPane eligibilityScrollPane;

    public ShowCAEligibility() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("CA Eligibility");
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setVisible(true);

        DefaultTableModel model = new DefaultTableModel(null, new String[]{"Course Code", "Course Name", "Eligibility Status"});
        eligibilityTable.setModel(model);
        checkCAEligibility();

        exitBtn.addActionListener(e -> { new StuHome(); dispose(); });
    }

    public void checkCAEligibility() {
        try (Connection con = DatabaseCon.connect();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT m.Stu_id, m.Course_code, c.Course_name, m.Quiz_01, m.Quiz_02, m.Quiz_03, m.Quiz_04, m.Assignment_01, m.Assignment_02, m.Mid_theory, m.Mid_practical FROM Marks m JOIN Course c ON m.Course_code=c.Course_code WHERE m.Stu_id=?")) {
            ps.setString(1, Session.loggedInUsername);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) eligibilityTable.getModel();

            while (rs.next()) {
                String courseCode = rs.getString("Course_code");
                String courseName = rs.getString("Course_name");

                double q1 = rs.getDouble("Quiz_01"), q2 = rs.getDouble("Quiz_02");
                double q3 = rs.getDouble("Quiz_03"), q4 = rs.getDouble("Quiz_04");

                double[] quizArray = courseCode.equals("ICT2122") ?
                        new double[]{q1, q2, q3, q4} : new double[]{q1, q2, q3};
                Arrays.sort(quizArray);

                double quizTotal;
                if (courseCode.equals("ICT2122")) {
                    quizTotal = (quizArray[3] + quizArray[2] + quizArray[1]) / 3 * 0.10;
                } else {
                    quizTotal = (quizArray[2] + quizArray[1]) / 2 * 0.10;
                }

                double ass1 = rs.getDouble("Assignment_01"), ass2 = rs.getDouble("Assignment_02");
                double assMarks = courseCode.equals("ICT2122") ?
                        ((ass1 + ass2) / 2) * 0.10 : ((ass1 + ass2) / 2) * 0.20;

                double midMarks = ((rs.getDouble("Mid_theory") + rs.getDouble("Mid_practical")) / 2) * 0.20;
                double totalCA = quizTotal + assMarks + midMarks;

                double fullCA = (courseCode.equals("ICT2122") || courseCode.equals("ICT2133") || courseCode.equals("ICT2152")) ? 30 : 40;
                boolean eligible = totalCA >= (fullCA / 2) - 0.5;

                model.addRow(new Object[]{courseCode, courseName, eligible ? "Eligible" : "Not Eligible"});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}