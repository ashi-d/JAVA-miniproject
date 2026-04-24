package Technical_officer;

import database.DatabaseCon;
import database.Session;
import javax.swing.*;
import java.io.File;
import java.sql.*;

public class updateTOprofile extends JFrame {
    private JPanel mainPanel;
    private JLabel updateProfileMainLbl;
    private JPanel detailPanel;
    private JLabel firstNameLbl;
    private JTextField firstNameTxt;
    private JLabel lastNameLbl;
    private JTextField lastNameTxt;
    private JLabel addressLbl;
    private JLabel emailLbl;
    private JLabel pNoLbl;
    private JTextField addressTxt;
    private JTextField emailTxt;
    private JTextField pNoTxt;
    private JTextField proPicTxt;
    private JButton uploadAnImageButton;
    private JButton updateProfileButton;
    private JButton cancelButton;

    public updateTOprofile() {
        setContentPane(mainPanel);
        setTitle("Update Technical Officer Profile");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(2000, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        uploadAnImageButton.addActionListener(e -> uploadProfilePicture());
        updateProfileButton.addActionListener(e -> updateTODetails());
        cancelButton.addActionListener(e -> { dispose(); new toHome(); });
    }

    public void updateTODetails() {
        File file = new File(proPicTxt.getText());
        try(Connection c=DatabaseCon.connect();PreparedStatement ps=c.prepareStatement("UPDATE User SET FName=?,LName=?,Address=?,Email=?,Phone_No=?,Profile_pic=? WHERE UserName=?")){
            ps.setString(1,firstNameTxt.getText());ps.setString(2,lastNameTxt.getText());ps.setString(3,addressTxt.getText());
            ps.setString(4,emailTxt.getText());ps.setString(5,pNoTxt.getText());ps.setString(6,file.getName());ps.setString(7,Session.loggedInUsername);
            int i=ps.executeUpdate();
            JOptionPane.showMessageDialog(null,i>0?"Profile Updated":"Profile Not Updated");
        } catch(Exception e){JOptionPane.showMessageDialog(null,"Update Failed");e.printStackTrace();}
    }

    public void uploadProfilePicture() {
        JFileChooser fc=new JFileChooser();fc.setDialogTitle("Select Profile Picture");fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files","jpg","jpeg","png","gif"));
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) proPicTxt.setText(fc.getSelectedFile().getAbsolutePath());
    }
}