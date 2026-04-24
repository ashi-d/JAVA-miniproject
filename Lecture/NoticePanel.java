package Lecture;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NoticePanel extends JPanel {

    JTextArea area = new JTextArea();

    public NoticePanel() {

        setLayout(new BorderLayout());

        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        load();
    }

    void load(){

        try(Connection con = database.DatabaseCon.connect()){

            String sql = "SELECT Title,Content,Date FROM notice ORDER BY Date DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);

            area.setText("");

            while(rs.next()){

                area.append("📢 "+rs.getString("Title")+"\n");
                area.append(rs.getString("Content")+"\n");
                area.append("📅 "+rs.getDate("Date")+"\n");
                area.append("---------------------\n");
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
}