package Lecture;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AttendancePanel extends JPanel {

    JTextArea area = new JTextArea();

    public AttendancePanel() {

        setLayout(new BorderLayout());

        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        load();
    }

    void load(){

        area.setText("");

        try(Connection con = database.DatabaseCon.connect()){

            // Attendance
            String sql1 =
                    "SELECT a.Stu_id, u.`Name`, a.Status " +
                            "FROM attendance a " +
                            "JOIN student s ON a.Stu_id=s.Stu_id " +
                            "JOIN user u ON s.User_id=u.User_id";

            ResultSet rs = con.createStatement().executeQuery(sql1);

            area.append("=== ATTENDANCE ===\n");

            while(rs.next()){
                area.append(
                        rs.getInt("Stu_id")+" | "+
                                rs.getString("Name")+" | "+
                                rs.getString("Status")+"\n"
                );
            }

            // Medical
            String sql2 =
                    "SELECT m.Stu_id, u.`Name`, m.Document, m.Status " +
                            "FROM medical m " +
                            "JOIN student s ON m.Stu_id=s.Stu_id " +
                            "JOIN user u ON s.User_id=u.User_id";

            ResultSet rs2 = con.createStatement().executeQuery(sql2);

            area.append("\n=== MEDICAL ===\n");

            while(rs2.next()){
                area.append(
                        rs2.getInt("Stu_id")+" | "+
                                rs2.getString("Name")+" | "+
                                rs2.getString("Document")+" | "+
                                rs2.getString("Status")+"\n"
                );
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
}