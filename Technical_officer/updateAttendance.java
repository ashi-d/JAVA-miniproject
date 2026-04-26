package Technical_officer;

import database.DatabaseCon;
import javax.swing.*;
import java.sql.*;

public class updateAttendance extends JFrame {
    private JPanel mainPanel;
    private JLabel title;
    private JPanel btnPanel;
    private JTextField sidField;
    private JTextField courseField;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField ctypeField;
    private JTextField statusField;
    private JLabel stuidLabel;
    private JLabel courseLabel;
    private JLabel weekLabel;
    private JLabel dayLabel;
    private JLabel ctypeLabel;
    private JLabel statusLabel;
    private JButton submit;

    Connection con;

    public updateAttendance() {
        setContentPane(mainPanel);
        setTitle("Update Attendance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setVisible(true);
        submit.addActionListener(e -> updateAttendanceStatus());
    }

    private void updateAttendanceStatus() {
        String sid=sidField.getText(),cc=courseField.getText(),wk=weekField.getText(),d=dayField.getText(),ct=ctypeField.getText(),st=statusField.getText();
        if(sid.isEmpty()||cc.isEmpty()||wk.isEmpty()||d.isEmpty()||ct.isEmpty()||st.isEmpty()){JOptionPane.showMessageDialog(this,"Please fill in all fields.");return;}
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("UPDATE attendance SET status=? WHERE Stu_id=? AND course_code=? AND week_no=? AND day_no=? AND course_type=?")){
            ps.setString(1,st);ps.setString(2,sid);ps.setString(3,cc);ps.setString(4,wk);ps.setString(5,d);ps.setString(6,ct);
            int rows=ps.executeUpdate();
            JOptionPane.showMessageDialog(this,rows>0?"Attendance updated successfully!":"No matching record found.");
        } catch(Exception e){JOptionPane.showMessageDialog(this,"Error: "+e.getMessage());}
    }
}
