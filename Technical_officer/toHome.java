package Technical_officer;

import database.DatabaseCon;
import database.Session;
import noticesViewing.NoticeViewing;
import student.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class toHome extends  JFrame implements NoticeViewing {
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JLabel stuManaSysLbl;
    private JLabel FoTLbl;
    private JButton logOutButton;
    private JPanel btnPanel;
    private JButton profileButton;
    private JButton attendanceButton;
    private JButton medicalButton;
    private JButton timeTableButton;
    private JButton noticeButton;
    private JPanel cardMainPanel;
    private JPanel profileCard;
    private JLabel profileHeadingLbl;
    private JPanel detailPanel;
    private JLabel fNameLbl;
    private JTextField fNameTxt;
    private JLabel lNameLbl;
    private JTextField lNameTxt;
    private JLabel addressLbl;
    private JTextField addressTxt;
    private JLabel emailLbl;
    private JTextField emailTxt;
    private JLabel pNoLbl;
    private JLabel roleLbl;
    private JTextField pNoTxt;
    private JTextField roleTxt;
    private JPanel imgMainPanel;
    private JPanel imgPanel;
    private JLabel imageLbl;
    private JButton updateProfileButton;
    private JPanel noticeCard;
    private JLabel noticeHeadingLbl;
    private JLabel selectTitleLbl;
    private JComboBox selectTitleCombo;
    private JPanel noticeTxtAreaPanel;
    private JScrollPane noticeScrollPane;
    private JTextArea noticeTxtArea;
    private JPanel timeTableCard;
    private JLabel timeTableHeadingLbl;
    private JPanel timeTablePanel;
    private JScrollPane timeTableScrollPane;
    private JPanel attendanceCard;
    private JLabel headingLabel;
    private JPanel medicalCard;
    private JLabel heading;
    private JPanel viewPanel;
    private JButton add;
    private JButton update;
    private JPanel buttons;
    private JPanel view;
    private JPanel buttonPanel;
    private JButton addbtn;
    private JButton updatebtn;
    private JTable timeTableTable;
    private JButton delete;
    private JButton deletebtn;
    private JButton deleteProfilePictureButton;

    public toHome() {

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Technical Officer home");
        setSize(2000,890);
        setVisible(true);
        setLocationRelativeTo(null);


        displayProfileDetils();
        showProfilePicture(imageLbl);


        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

        cardMainPanel.add(profileCard, "profileCard");
        cardMainPanel.add(attendanceCard, "attendanceCard");
        cardMainPanel.add(medicalCard, "medicalCard");
        cardMainPanel.add(timeTableCard, "timeTableCard");
        cardMainPanel.add(noticeCard, "noticeCard");
        cardLayout.show(cardMainPanel, "profileCard");

        profileButton.setFocusPainted(false);
        attendanceButton.setFocusPainted(false);
        medicalButton.setFocusPainted(false);
        timeTableButton.setFocusPainted(false);
        noticeButton.setFocusPainted(false);

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = ""                        ;
                new Login();
                dispose();
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "profileCard");
            }
        });
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new updateTOprofile();
                dispose();
            }
        });
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "attendanceCard");
                showAttendancetable();
            }
        });
        medicalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "medicalCard");
                showMedicaltable();
            }
        });
        timeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "timeTableCard");
                showTimeTable();
            }
        });
        noticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "noticeCard");
                addNoticeTitlesToComboBox();
            }
        });
        selectTitleCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected title
                String selectedTitle = (String) selectTitleCombo.getSelectedItem();
                System.out.println("Selected Title: " + selectedTitle);
                // Display the content for the selected title
                if (selectedTitle != null) {
                    displayNoticeContent(selectedTitle);
                }
            }
        });

        //Add attendance
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddAttendance addForm = new AddAttendance();
                addForm.setVisible(true);// make sure class name matches
            }
        });

        //Update attendance
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAttendance updateForm = new updateAttendance();
                updateForm.setVisible(true);
            }
        });

        //Add medical
        addbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addmedical addmedi = new addmedical();
                addmedi.setVisible(true);
            }
        });

        //Update medical
        updatebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMedical upmedi = new  updateMedical();
                upmedi.setVisible(true);
            }
        });

        //delete attendance
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedAttendanceRow();
            }
        });

        //delete medical
        deletebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedMedicalRow();
            }
        });

        deleteProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProfilePicture(imageLbl);
                deleteProfilePictureButton.setEnabled(false);
            }
        });
    }
    Connection con;

    //Attendance table

    private void showAttendancetable() {
        try {
            con = DatabaseCon.connect();
            Statement st = con.createStatement();
            String query = "SELECT * FROM attendance";
            ResultSet rs = st.executeQuery(query);

            // Get column names from ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
            }

            // Create table model
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Add rows to model
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(580, 300));

            viewPanel.removeAll();
            viewPanel.setLayout(new BorderLayout());
            viewPanel.add(scrollPane, BorderLayout.CENTER);
            viewPanel.revalidate();
            viewPanel.repaint();

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error");
        }
    }


    //Delete Attendance
    private void deleteSelectedAttendanceRow() {
        // Find the JTable in the viewPanel
        for (Component comp : viewPanel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();

                if (view instanceof JTable) {
                    JTable table = (JTable) view;

                    // Optional: Check if this table is the attendance table by checking column headers
                    if (table.getColumnName(0).equalsIgnoreCase("Attendance_id")) {
                        int selectedRow = table.getSelectedRow();

                        if (selectedRow == -1) {
                            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                            return;
                        }

                        Object idValue = table.getValueAt(selectedRow, 0); // Attendance_id
                        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this attendance record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                con = DatabaseCon.connect();
                                String sql = "DELETE FROM attendance WHERE Attendance_id = ?";
                                PreparedStatement pstmt = con.prepareStatement(sql);
                                pstmt.setObject(1, idValue);

                                int rowsAffected = pstmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                                    showAttendancetable(); // Refresh the table
                                } else {
                                    JOptionPane.showMessageDialog(this, "Failed to delete record.");
                                }

                                pstmt.close();
                                con.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(this, "Error deleting attendance: " + e.getMessage());
                            }
                        }

                        return; // Exit after handling the table
                    }
                }
            }
        }

        // If no suitable table is found
        JOptionPane.showMessageDialog(this, "Attendance table not found.");
    }

    //Medical
    private void showMedicaltable() {
        try {
            con = DatabaseCon.connect();
            Statement st = con.createStatement();
            String query = "SELECT * FROM medical";
            ResultSet rs = st.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // Get column names
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = rsmd.getColumnName(i + 1);
            }

            // Table model to store data
            DefaultTableModel model = new DefaultTableModel(columnNames, 0){
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            };

            // Read each row and add to model
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(580, 300));

            view.removeAll();
            view.setLayout(new BorderLayout());
            view.add(scrollPane, BorderLayout.CENTER);
            view.revalidate();
            view.repaint();

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error while loading medical table");
        }
    }

    //Delete Medical
    private void deleteSelectedMedicalRow() {
        int selectedRow = -1;

        for (Component comp : view.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                JViewport viewport = scrollPane.getViewport();
                Component viewComponent = viewport.getView();
                if (viewComponent instanceof JTable) {
                    JTable table = (JTable) viewComponent;
                    selectedRow = table.getSelectedRow();

                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(this, "Please select a medical row to delete.");
                        return;
                    }

                    Object idValue = table.getValueAt(selectedRow, 0); // Assume first column is ID
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this medical record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            //connectToDatabase();
                            con = DatabaseCon.connect();
                            String sql = "DELETE FROM medical WHERE Medical_id = ?";
                            PreparedStatement pstmt = con.prepareStatement(sql);
                            pstmt.setObject(1, idValue);

                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(this, "Medical record deleted successfully.");
                                showMedicaltable(); // Refresh
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to delete medical record.");
                            }

                            pstmt.close();
                            con.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error deleting medical record: " + e.getMessage());
                        }
                    }
                    break;
                }
            }
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Medical table not found.");
        }
    }


    //Timetable
    public void showTimeTable() {
        try {
            con = DatabaseCon.connect();
            String sql = "SELECT * FROM TimeTable";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            String[] columnNames = {"Department", "Course_Code", "Course_Name", "Time", "Day"};
            DefaultTableModel model = new DefaultTableModel(null, columnNames){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            while (rs.next()) {
                // Get the day number from the database
                int dayNumber = rs.getInt("Day");

                // Map the day number to the corresponding weekday name
                String dayName = mapDayNumberToName(dayNumber);

                model.addRow(new Object[]{
                        rs.getString("Department"),
                        rs.getString("Course_Code"),
                        rs.getString("Course_Name"),
                        rs.getString("Time"),
                        dayName
                });
            }
            timeTableTable.setModel(model);
        } catch (Exception e) {
            System.out.println("Error in display Time Table: " + e.getMessage());
        }
    }

    private String mapDayNumberToName(int dayNumber) {
        switch (dayNumber) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            default: return "Unknown";
        }
    }

    //Notices
    public void addNoticeTitlesToComboBox(){
        con = DatabaseCon.connect();

        try{
            Connection conn = DatabaseCon.connect();
            String sql = "SELECT * FROM Notice";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            selectTitleCombo.removeAllItems();

            while (rs.next()) {
                String title = rs.getString("Title");
                selectTitleCombo.addItem(title); // Add each title to the combo box
                System.out.println("Title: " + title);
            }
        }catch(Exception e){
            System.out.println("Error in add Notice Titles To ComboBox: " + e.getMessage());
        }
    }

    public void displayNoticeContent(String title) {
        try {
            noticeTxtArea.setText("");
            // Establish connection to the database to get the NoticeId based on the title
            Connection con = DatabaseCon.connect();
            String sql = "SELECT Notice_id FROM Notice WHERE Title = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String noticeId = rs.getString("Notice_id");

                // Read content from the corresponding text file (e.g., notice_1.txt)
                File noticeFile = new File("notices/notice_" + noticeId + ".txt");
                BufferedReader reader = new BufferedReader(new FileReader(noticeFile));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                // Display the content in the JTextArea
                noticeTxtArea.setText(content.toString());
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error in display Notice Content: " + e.getMessage());
        }
    }

    //Profile
    public void displayProfileDetils(){
        con = DatabaseCon.connect();

        try {
            String sql = "SELECT FName, LName, Address, Email, Phone_No, Role FROM User WHERE UserName = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, Session.loggedInUsername);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String fName1 = rs.getString("FName");
                fNameTxt.setText(fName1);
                System.out.println("fName1: " + fName1);
                String lName1 = rs.getString("LName");
                lNameTxt.setText(lName1);
                System.out.println("lName1: " + lName1);
                String address1 = rs.getString("Address");
                addressTxt.setText(address1);
                System.out.println("address1: " + address1);
                String email1 = rs.getString("Email");
                emailTxt.setText(email1);
                System.out.println("email1: " + email1);
                String phoneNo1 = rs.getString("Phone_No");
                pNoTxt.setText(phoneNo1);
                System.out.println("phoneNo1: " + phoneNo1);
                String role1 = rs.getString("Role");
                roleTxt.setText(role1);
                System.out.println("role1: " + role1);

            }else {
                JOptionPane.showMessageDialog(null, "No Profile Found");
            }
        }catch (Exception e){
            System.out.println("Error in displayProfileDetils: " + e.getMessage());
        }
    }

    // *******  Profile Picture *****************

    public void showProfilePicture(JLabel imageLbl) {
        Connection con = DatabaseCon.connect();
        try {
            String sql = "SELECT Profile_pic FROM User WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, Session.loggedInUsername);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String fileName = rs.getString("Profile_pic");

                // If no profile picture set in DB, use default
                if (fileName == null || fileName.trim().isEmpty()) {
                    fileName = "default.png";
                }

                String path = "user_Pro_Pic/" + fileName;
                File imageFile = new File(path);

                // If image file does not exist, fallback to default image
                if (!imageFile.exists()) {
                    path = "user_Pro_Pic/default.png";
                }

                // Load and Resize Image to fit JLabel
                ImageIcon imageIcon = new ImageIcon(path);

                // Get JLabel size (designed from GUI builder)
                int width = imageLbl.getWidth();
                int height = imageLbl.getHeight();

                // Default size safety check (in case label not ready)
                if (width == 0 || height == 0) {
                    width = 150;
                    height = 150;
                }

                Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLbl.setIcon(new ImageIcon(image));
                imageLbl.repaint(); // Refresh label to show updated image
            }
        } catch (Exception e) {
            System.out.println("Error in showProfilePicture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteProfilePicture(JLabel imageLbl) {
        Connection con = DatabaseCon.connect();
        try{
            String sql = "UPDATE User SET Profile_pic = NULL WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, Session.loggedInUsername);

            int result = pst.executeUpdate();

            if (result > 0) {
                // Set default image after deletion
                String path = "user_Pro_Pic/default.png";

                // Get label size
                int width = imageLbl.getWidth();
                int height = imageLbl.getHeight();

                if (width == 0 || height == 0) {
                    width = 150;
                    height = 150;
                }

                ImageIcon imageIcon = new ImageIcon(path);
                Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLbl.setIcon(new ImageIcon(image));
                imageLbl.repaint();  // Refresh label

                System.out.println("Profile picture deleted successfully.");
            } else {
                System.out.println("No profile picture was found or username invalid.");
            }

        } catch (Exception e) {
            System.out.println("Error in deleteProfilePicture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        new toHome();
    }*/
}
