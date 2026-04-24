package Lecture;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class EligibilityPanel extends JPanel {

    JTable table;
    DefaultTableModel model;

    public EligibilityPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"Student ID","Attendance %","Eligibility"},0
        );

        table = new JTable(model);
        add(new JScrollPane(table),BorderLayout.CENTER);

        load();
    }

    void load(){

        model.setRowCount(0);

        try(Connection con = database.DatabaseCon.connect()){

            String sql =
                    "SELECT Stu_id, COUNT(*) total, " +
                            "SUM(CASE WHEN Status='Present' THEN 1 ELSE 0 END) present " +
                            "FROM attendance GROUP BY Stu_id";

            ResultSet rs = con.createStatement().executeQuery(sql);

            while(rs.next()){

                double per = rs.getInt("present")*100.0/rs.getInt("total");

                model.addRow(new Object[]{
                        rs.getInt("Stu_id"),
                        String.format("%.2f",per)+"%",
                        per>=80?"ELIGIBLE ✔":"NOT ELIGIBLE ❌"
                });
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
}