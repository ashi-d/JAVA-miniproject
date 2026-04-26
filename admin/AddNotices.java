package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

import database.DatabaseCon;
import database.DatabaseCon;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddNotices extends JFrame {
    public JPanel MainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton addNoticeButton;
    private JButton clearButton;
    private JButton exitButton;

    public AddNotices() {
        setContentPane(MainPanel);
        setTitle("Add Notices");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setVisible(true);

        addNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String noticeId = textField1.getText().trim();
                String title    = textField3.getText().trim();
                String date     = textField4.getText().trim();
                String adId     = "Admin01";

                if (noticeId.isEmpty() || title.isEmpty() || date.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                try {
                    String trimmedDate = date.trim().replaceAll("[\\n\\r\\t]", "");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate parsedDate = LocalDate.parse(trimmedDate, formatter);
                    date = parsedDate.toString();
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date! Use YYYY-MM-DD format.");
                    return;
                }

                try {
                    Connection conn = DatabaseCon.connect();
                    String sql = "INSERT INTO notice (Notice_id, Ad_id, Title, Date) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, noticeId);
                    stmt.setString(2, adId);
                    stmt.setString(3, title);
                    stmt.setString(4, date);
                    int rows = stmt.executeUpdate();

                    if (rows > 0) {
                        File dir = new File("notices");
                        if (!dir.exists()) dir.mkdirs();
                        File file = new File(dir, "notice_" + noticeId + ".txt");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            writer.write("Title: " + title);
                            writer.newLine();
                            writer.write("Date: " + date);
                            writer.newLine();
                            writer.write("Content:");
                            writer.newLine();
                        }
                        JOptionPane.showMessageDialog(null, "Notice added successfully!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add notice.");
                    }
                    stmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        clearButton.addActionListener(e -> clearFields());

        exitButton.addActionListener(e -> {
            new AdHome();
            dispose();
        });
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }
}