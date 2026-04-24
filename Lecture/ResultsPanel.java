package Lecture;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ResultsPanel extends JPanel {

    JTable table;
    DefaultTableModel model;

    public ResultsPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{
                        "Stu ID","Course ID","Assignment","Quiz","Mid","End",
                        "CA Total","Final","Grade","GPA"
                },0
        ){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        table = new JTable(model);
        add(new JScrollPane(table),BorderLayout.CENTER);

        load();
    }

    void load(){

        model.setRowCount(0);

        try(Connection con = database.DatabaseCon.connect()){

            String sql = "SELECT * FROM marks";
            ResultSet rs = con.createStatement().executeQuery(sql);

            while(rs.next()){

                double finalM = rs.getDouble("Final_Total");

                model.addRow(new Object[]{
                        rs.getInt("Stu_id"),
                        rs.getInt("Course_id"),
                        rs.getDouble("Assignment"),
                        rs.getDouble("Quiz"),
                        rs.getDouble("Mid"),
                        rs.getDouble("End"),
                        rs.getDouble("CA_Total"),
                        rs.getDouble("Final_Total"),
                        grade(finalM),
                        gpa(finalM)
                });
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    String grade(double m){
        if(m>=75) return "A";
        else if(m>=65) return "B";
        else if(m>=50) return "C";
        else if(m>=40) return "D";
        else return "F";
    }

    double gpa(double m){
        if(m>=75) return 4;
        else if(m>=65) return 3;
        else if(m>=50) return 2;
        else if(m>=40) return 1;
        else return 0;
    }
}
