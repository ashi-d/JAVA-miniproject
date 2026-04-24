package Lecture;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class StudentDetailsPanel extends JPanel {

    JTable table;
    DefaultTableModel model;

    public StudentDetailsPanel() {

        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"Student ID", "Name", "Reg Number", "Batch", "Contact"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // center align
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh");
        add(refresh, BorderLayout.SOUTH);

        refresh.addActionListener(e -> load());

        load();
    }

    void load() {

        model.setRowCount(0);

        try (Connection con = database.DatabaseCon.connect()) {

            String sql =
                    "SELECT s.Stu_id, u.Name, s.Reg_number, s.Batch, s.Contact " +
                            "FROM student s " +
                            "JOIN user u ON s.User_id = u.User_id";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("Stu_id"),
                        rs.getString("Name"), // 🔥 use alias  // ✅ correct column
                        rs.getString("Reg_number"),
                        rs.getString("Batch"),
                        rs.getString("Contact")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading students: " + e.getMessage());
        }
    }
}