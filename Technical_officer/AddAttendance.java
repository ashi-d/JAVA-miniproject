package Technical_officer;

import database.DatabaseCon;
import javax.swing.*;
import java.sql.*;

public class AddAttendance extends JFrame {
    private JPanel mainPanel;
    private JLabel title;
    private JPanel btnPanel;
    private JTextField sidLabel;
    private JTextField aidLabel;
    private JTextField courseLabel;
    private JTextField lechourLabel;
    private JTextField weekLabel;
    private JTextField dayLabel;
    private JTextField statusLabel;
    private JTextField ctypeLabel;
    private JButton addbutton;
    private JLabel aid;
    private JLabel sid;
    private JLabel lechours;
    private JLabel week;
    private JLabel day;
    private JLabel status;
    private JLabel ctype;

    Connection con;

    public AddAttendance() {
        setTitle("Add Attendance");
        setContentPane(mainPanel);
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        aidLabel.setText(generateAttendanceId());
        aidLabel.setEditable(false);
        addbutton.addActionListener(e -> insertAttendance());
    }

    private String generateAttendanceId() {
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("SELECT Attendance_id FROM attendance ORDER BY CAST(SUBSTRING(Attendance_id, 2) AS UNSIGNED) DESC LIMIT 1")){
            ResultSet rs=ps.executeQuery();
            if(rs.next()){int n=Integer.parseInt(rs.getString("Attendance_id").substring(1))+1;return String.format("A%03d",n);}
        } catch(Exception e){e.printStackTrace();} return "A001";
    }

    private void insertAttendance() {
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("INSERT INTO attendance (Attendance_id,Stu_id,Course_code,Lec_hour,Week_No,Day_No,Status,Course_Type) VALUES (?,?,?,?,?,?,?,?)")){
            ps.setString(1,aidLabel.getText()); ps.setString(2,sidLabel.getText()); ps.setString(3,courseLabel.getText());
            ps.setString(4,lechourLabel.getText()); ps.setString(5,weekLabel.getText()); ps.setString(6,dayLabel.getText());
            ps.setString(7,statusLabel.getText()); ps.setString(8,ctypeLabel.getText());
            if(ps.executeUpdate()>0){JOptionPane.showMessageDialog(this,"Attendance Added Successfully!");clearFields();aidLabel.setText(generateAttendanceId());}
            else JOptionPane.showMessageDialog(this,"Failed to Add Attendance.");
        } catch(Exception e){JOptionPane.showMessageDialog(this,"Database Error: "+e.getMessage());}
    }

    private void clearFields() {
        sidLabel.setText(""); courseLabel.setText(""); lechourLabel.setText("");
        weekLabel.setText(""); dayLabel.setText(""); statusLabel.setText(""); ctypeLabel.setText("");
    }
}
