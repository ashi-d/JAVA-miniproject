package Lecture;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MarksPanel extends JPanel {

    JTextField sid,cid,exam,marks;

    public MarksPanel(){

        setLayout(new GridLayout(5,2));

        sid=new JTextField();
        cid=new JTextField();
        exam=new JTextField();
        marks=new JTextField();

        JButton btn=new JButton("Upload");

        add(new JLabel("Student ID")); add(sid);
        add(new JLabel("Course ID")); add(cid);
        add(new JLabel("Exam")); add(exam);
        add(new JLabel("Marks")); add(marks);
        add(new JLabel("")); add(btn);

        btn.addActionListener(e->save());
    }

    void save(){

        try(Connection con = database.DatabaseCon.connect()){

            String col="";
            switch(exam.getText().toLowerCase()){
                case "assignment": col="Assignment"; break;
                case "quiz": col="Quiz"; break;
                case "mid": col="Mid"; break;
                case "end": col="End"; break;
                default:
                    JOptionPane.showMessageDialog(this,"Invalid exam");
                    return;
            }

            String sql="UPDATE marks SET "+col+"=? WHERE Stu_id=? AND Course_id=?";
            PreparedStatement ps=con.prepareStatement(sql);

            ps.setInt(1,Integer.parseInt(marks.getText()));
            ps.setInt(2,Integer.parseInt(sid.getText()));
            ps.setInt(3,Integer.parseInt(cid.getText()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Uploaded ✔");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
}