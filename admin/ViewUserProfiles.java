package admin;

import javax.swing.*;
import database.DatabaseCon;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.awt.CardLayout;

public class ViewUserProfiles extends JFrame {
    public JPanel Mainpanel;
    private JPanel userdetailpanel;
    private JTable usertable;
    private JButton deleteButton;
    private JTextField username;
    private JButton clearButton;
    private JPanel parentCardPanel;
    private String returnCardName;

    public ViewUserProfiles(JPanel cardpanel, String returnToCard) {
        this.parentCardPanel = cardpanel;
        this.returnCardName = returnToCard;

        loadUserTable();

        deleteButton.addActionListener(e -> deleteUser(username.getText().trim()));
        clearButton.addActionListener(e -> username.setText(""));
    }

    public void loadUserTable() {
        Connection conn = DatabaseCon.connect();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();

                DefaultTableModel model = new DefaultTableModel() {
                    public boolean isCellEditable(int r, int c) { return false; }
                };
                for (int i = 1; i <= cols; i++) model.addColumn(meta.getColumnName(i));
                while (rs.next()) {
                    Object[] row = new Object[cols];
                    for (int i = 1; i <= cols; i++) row[i-1] = rs.getObject(i);
                    model.addRow(row);
                }
                usertable.setModel(model);
                usertable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                rs.close(); stmt.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteUser(String usernameToDelete) {
        if (usernameToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a username.");
            return;
        }
        Connection conn = DatabaseCon.connect();
        if (conn != null) {
            try {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE username=?");
                stmt.setString(1, usernameToDelete);
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "User deleted successfully.");
                    loadUserTable();
                } else JOptionPane.showMessageDialog(null, "User not found.");
                stmt.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }
}