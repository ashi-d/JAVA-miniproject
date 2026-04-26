package Technical_officer;

import database.DatabaseCon;
import javax.swing.*;
import java.sql.*;

public class updateMedical extends JFrame {
    private JPanel mainPanel;
    private JLabel title;
    private JPanel btnPanel;
    private JTextField courseField;
    private JTextField sidField;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField ctypeField;
    private JTextField statusField;
    private JButton submit;
    private JLabel sidLabel;
    private JLabel courseLabel;
    private JLabel weekLable;
    private JLabel dayLabel;
    private JLabel ctypeLabel;
    private JLabel statusLabel;

    Connection con;

    public updateMedical() {
        setContentPane(mainPanel);
        setTitle("Update Medical Card");
        setSize(2000, 890);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        submit.addActionListener(e -> updateMedicalStatus());
    }

    private void updateMedicalStatus() {
        String sid=sidField.getText().trim(),cc=courseField.getText().trim(),wk=weekField.getText().trim(),d=dayField.getText().trim(),ct=ctypeField.getText().trim(),st=statusField.getText().trim();
        if(sid.isEmpty()||cc.isEmpty()||wk.isEmpty()||d.isEmpty()||ct.isEmpty()||st.isEmpty()){JOptionPane.showMessageDialog(this,"Please fill in all fields.");return;}
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("UPDATE medical SET status=? WHERE stu_id=? AND course_code=? AND week_no=? AND day_no=? AND course_type=?")){
            ps.setString(1,st);ps.setString(2,sid);ps.setString(3,cc);ps.setString(4,wk);ps.setString(5,d);ps.setString(6,ct);
            int rows=ps.executeUpdate();
            JOptionPane.showMessageDialog(this,rows>0?"Medical status updated successfully.":"No record found to update.");
        } catch(Exception e){JOptionPane.showMessageDialog(this,"Update failed: "+e.getMessage());}
    }
}
