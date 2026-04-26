package Technical_officer;

import database.DatabaseCon;
import javax.swing.*;
import java.sql.*;

public class addmedical extends JFrame {
    private JPanel MainPanel;
    private JLabel titleLabel;
    private JPanel btnPanel;
    private JTextField stuidField;
    private JTextField midField;
    private JTextField courseField;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField statusField;
    private JLabel midLabel;
    private JLabel stuidLabel;
    private JLabel courseLabel;
    private JLabel weekLabel;
    private JLabel dayLabel;
    private JLabel statusLabel;
    private JTextField aidField;
    private JLabel aidLabel;
    private JButton addButton;
    private JTextField ctypeField;
    private JLabel ctype;
    private JButton findbutton;

    Connection con;

    public addmedical() {
        setTitle("Add Medical");
        setContentPane(MainPanel);
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        addButton.addActionListener(e -> insertMedical());
        findbutton.addActionListener(e -> fetchAttendanceId());
    }

    private void fetchAttendanceId() {
        String sid=stuidField.getText(),week=weekField.getText(),day=dayField.getText(),course=courseField.getText(),ct=ctypeField.getText();
        if(sid.isEmpty()||week.isEmpty()||day.isEmpty()||course.isEmpty()||ct.isEmpty()){JOptionPane.showMessageDialog(this,"Please fill in all required fields.");return;}
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("SELECT Attendance_id FROM attendance WHERE Stu_id=? AND week_No=? AND day_No=? AND Course_code=? AND Course_type=?")){
            ps.setString(1,sid);ps.setString(2,week);ps.setString(3,day);ps.setString(4,course);ps.setString(5,ct);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){aidField.setText(rs.getString("Attendance_id"));JOptionPane.showMessageDialog(this,"Attendance ID fetched.");}
            else JOptionPane.showMessageDialog(this,"No matching attendance record found.");
        } catch(Exception e){JOptionPane.showMessageDialog(this,"Error: "+e.getMessage());}
    }

    private void insertMedical() {
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("INSERT INTO medical (Medical_id,Stu_id,Course_code,week_No,day_No,Status,Attendance_id,Course_type) VALUES (?,?,?,?,?,?,?,?)")){
            ps.setString(1,midField.getText());ps.setString(2,stuidField.getText());ps.setString(3,courseField.getText());
            ps.setString(4,weekField.getText());ps.setString(5,dayField.getText());ps.setString(6,statusField.getText());
            ps.setString(7,aidField.getText());ps.setString(8,ctypeField.getText());
            if(ps.executeUpdate()>0){JOptionPane.showMessageDialog(this,"Medical record added successfully!");updateAttendanceStatus(aidField.getText());}
            else JOptionPane.showMessageDialog(this,"Failed to add record.");
        } catch(Exception e){JOptionPane.showMessageDialog(this,"Database error: "+e.getMessage());}
    }

    private void updateAttendanceStatus(String aid) {
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("UPDATE attendance SET status='Medical' WHERE Attendance_id=?")){
            ps.setString(1,aid); ps.executeUpdate();
        } catch(Exception e){e.printStackTrace();}
    }
}
