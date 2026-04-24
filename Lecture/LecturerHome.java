package Lecture;

import javax.swing.*;
import java.awt.*;

public class LecturerHome extends JFrame {

    CardLayout cardLayout;
    JPanel rightPanel;
    String username;

    public LecturerHome(String username) {

        this.username = username; // 🔥 IMPORTANT

        setTitle("TECMIS - Lecturer Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // =========================
        // LEFT PANEL
        // =========================
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(30, 30, 30));
        leftPanel.setPreferredSize(new Dimension(220, 600));

        JLabel title = new JLabel("TECMIS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel userLbl = new JLabel("👤 " + username, SwingConstants.CENTER);
        userLbl.setForeground(Color.LIGHT_GRAY);

        JPanel top = new JPanel(new GridLayout(2,1));
        top.setBackground(new Color(30, 30, 30));
        top.add(title);
        top.add(userLbl);

        leftPanel.add(top, BorderLayout.NORTH);

        // =========================
        // MENU
        // =========================
        JPanel menu = new JPanel(new GridLayout(8, 1, 8, 8));
        menu.setBackground(new Color(30, 30, 30));

        JButton profile = createBtn("Profile");
        JButton course = createBtn("Course Materials");
        JButton marks = createBtn("Upload Marks");
        JButton students = createBtn("Students");
        JButton eligibility = createBtn("Eligibility");
        JButton results = createBtn("Results");
        JButton attendance = createBtn("Attendance");
        JButton notices = createBtn("Notices");

        menu.add(profile);
        menu.add(course);
        menu.add(marks);
        menu.add(students);
        menu.add(eligibility);
        menu.add(results);
        menu.add(attendance);
        menu.add(notices);

        leftPanel.add(menu, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // =========================
        // RIGHT PANEL
        // =========================
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);

        // 🔥 HOME PANEL (better UI)
        JPanel home = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("WELCOME, " + username, SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        home.add(welcome, BorderLayout.CENTER);

        rightPanel.add(home, "HOME");

        // 🔥 PASS USERNAME TO PANELS (IMPORTANT)
       rightPanel.add(new ProfilePanel(database.Session.loggedUserId), "PROFILE");
       //rightPanel.add(new CourseMaterialPanel(), "COURSE");
       rightPanel.add(new MarksPanel(), "MARKS");
        rightPanel.add(new StudentDetailsPanel(), "STUDENTS");
        rightPanel.add(new EligibilityPanel(), "ELIGIBILITY");
        rightPanel.add(new ResultsPanel(), "RESULTS");
       rightPanel.add(new AttendancePanel(), "ATTENDANCE");
        rightPanel.add(new NoticePanel(), "NOTICES");

        add(rightPanel, BorderLayout.CENTER);

        // =========================
        // BUTTON ACTIONS
        // =========================
        profile.addActionListener(e -> show("PROFILE"));
        course.addActionListener(e -> show("COURSE"));
        marks.addActionListener(e -> show("MARKS"));
        students.addActionListener(e -> show("STUDENTS"));
        eligibility.addActionListener(e -> show("ELIGIBILITY"));
        results.addActionListener(e -> show("RESULTS"));
        attendance.addActionListener(e -> show("ATTENDANCE"));
        notices.addActionListener(e -> show("NOTICES"));

        cardLayout.show(rightPanel, "HOME");

        setVisible(true);
    }

    // 🔥 reusable navigation
    private void show(String name) {
        cardLayout.show(rightPanel, name);
    }

    // =========================
    // BUTTON STYLE
    // =========================
    private JButton createBtn(String text) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);
        btn.setBackground(new Color(50, 50, 50));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }
}