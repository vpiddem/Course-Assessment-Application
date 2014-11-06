/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradience;

import beans.LoggedInUser;
import database.DataBaseConnection;
import homework.CourseListPane;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.RadialGradientPaint;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import javax.management.Notification;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
import view.AddNewCourse;
import view.Colors;
import view.CourseRoll;
import view.CoursesTab;
import view.EditHomework;
import view.Login;
import view.Notifications;
import view.QueryPanel;
import view.QuestionBank;
import view.QuestionConfiguration;
import view.Reports;
import view.SubmissionsPanel;
import view.UpdateAccount;

/**
 *
 * @author Vikas
 */
public class GradienceAppWindow extends JFrame implements GradienceAppConstants {

    public GradienceAppWindow(DataBaseConnection db) {
        this.db = db;
        createAndShowGUI();
    }

    void createAndShowGUI() {
        try {
            setTitle("Gradience Application");
            Dimension dispSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(new Dimension(dispSize.width - 100, dispSize.height - 50));
            setMinimumSize(new Dimension(dispSize.width - dispSize.width / 3, dispSize.height - dispSize.height / 4));
            setLocation(85, 5);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            createUI();     //create user interface window
            Image icon = Toolkit.getDefaultToolkit().getImage(GradienceAppIcon);
            this.setIconImage(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(false);
    }

    void createUI() {
        final JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP);
        Dimension dispSize = Toolkit.getDefaultToolkit().getScreenSize();
        tabPane.setPreferredSize(new Dimension(dispSize.width - 200, dispSize.height - 150));
        tabPane.setFont(new Font("Serif", Font.ITALIC, 21));
        tabPane.addTab("Home            ", createImageIcon(GradienceDBIcon), getNotificationsTab());
        tabPane.addTab("  Update Account   ", createImageIcon(GradienceDBIcon), getUpdateAccountTab());
        tabPane.addTab("Courses         ", createImageIcon(GradienceDBIcon), getCoursesTab());

        if (!(LoggedInUser.role == ROLE_INSTRUCTOR)) {
            tabPane.addTab("Add NewCourse   ", createImageIcon(GradienceDBIcon), getAddCourseTab());
            tabPane.addTab("Attempt Homeworks", createImageIcon(GradienceDBIcon), getHomeWorksTab());
        }
        if (LoggedInUser.role == ROLE_INSTRUCTOR) {
            tabPane.addTab("Add Homework     ", createImageIcon(GradienceDBIcon), getAddHomeworkTab());
            tabPane.addTab("Enrollment      ", createImageIcon(GradienceDBIcon), getClassRollTab());

        }
        if (LoggedInUser.role == ROLE_INSTRUCTOR || LoggedInUser.role == ROLE_TA) {
            tabPane.addTab("Question Bank   ", createImageIcon(GradienceDBIcon), getQuestionBankTab());
            tabPane.addTab("Edit Homeworks", createImageIcon(GradienceDBIcon), getEditHomeWorkTab());
            tabPane.addTab("Query Reporter   ", createImageIcon(GradienceDBIcon), getQueryReporterTab());

        }

        tabPane.addTab("Reports         ", createImageIcon(GradienceDBIcon), getReportsTab());
        tabPane.addTab("Submissions   ", createImageIcon(GradienceDBIcon), getSubmissionsTab());

        tabPane.setToolTipTextAt(0, "Home Page");

        tabPane.setTabPlacement(JTabbedPane.LEFT);
        add(linkBar(), BorderLayout.NORTH);
        add(tabPane, BorderLayout.CENTER);
        add(new JLabel("South Content"), BorderLayout.SOUTH);
        pack();
    }

    public static MattePainter getPainter() {
        int width = 300;
        int height = 100;
        Color color1 = Colors.White.color(0.6f);
        Color color2 = Colors.LightBlue.color(1.0f);
        LinearGradientPaint gradientPaint
                = new LinearGradientPaint(0.0f, 0.0f, width, height,
                        new float[]{0.0f, 1.0f},
                        new Color[]{color1, color2});
        RadialGradientPaint gradientPaint1
                = new RadialGradientPaint(new Point(200, 200), 400,
                        new float[]{0.0f, 1.0f},
                        new Color[]{color2, color1});
        MattePainter mattePainter = new MattePainter(gradientPaint1);
        return mattePainter;
    }

    private Box linkBar() {
        Box statusBox = Box.createHorizontalBox();
        JXPanel labelPanel = new JXPanel();
        labelPanel.setBackgroundPainter(getPainter());
        labelPanel.setPreferredSize(new Dimension(600, 32));
        statusBox.add(labelPanel);
        JXPanel linkPannel = new JXPanel();
        linkPannel.setBackgroundPainter(new MattePainter(new LinearGradientPaint(0.0f, 0.0f, 300, 400,
                new float[]{0.0f, 1.0f},
                new Color[]{Colors.White.color(0.6f), Colors.LightBlue.color(1.0f)})));
        statusBox.add(linkPannel);
        setGradienceTitleLabel(labelPanel);

        JLabel lbl_LastLoginTime = new JLabel();
        String loginDateString = "";
        try {
            loginDateString = db.getLastLoggedInDate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lbl_LastLoginTime.setText("   Last Login:" + loginDateString.substring(0, loginDateString.lastIndexOf(".")) + "  ");

        JLabel lbl_UserName = new JLabel();
        lbl_UserName.setText("Welcome " + LoggedInUser.name + "  ");

        JButton btn_Logout = new JButton("Logout");

        btn_Logout.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                db.setLastLoggedInTime();
                db.updateLoginStatus(LoggedInUser.name, "N");
                System.exit(0);
            }
        });
        linkPannel.add(lbl_UserName);
        linkPannel.add(btn_Logout);
        linkPannel.add(lbl_LastLoginTime);
        statusBox.setMinimumSize(new Dimension(500, 400));
        statusBox.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        return statusBox;
    }

    private void setGradienceTitleLabel(JXPanel labelPanel) {
        JLabel lbl_GradienceTitle = new JLabel("Gradience Application");
        lbl_GradienceTitle.setFont(new Font("Serif", Font.ITALIC, 23));
        lbl_GradienceTitle.setForeground(new Color(0, 125, 155));
        labelPanel.add(lbl_GradienceTitle);
    }

    private ImageIcon createImageIcon(String path) {
        if (path != null) {
            return new ImageIcon(path);
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void createTrayIcon() throws AWTException {
        PopupMenu popup = new PopupMenu();
        Image img = Toolkit.getDefaultToolkit().getImage(GradienceAppIcon);
        MenuItem openApp = new MenuItem("Open GradienceApp");
        MenuItem aboutApp = new MenuItem("About");
        MenuItem exitApp = new MenuItem("Exit");
        popup.add(openApp);
        popup.add(aboutApp);
        popup.add(exitApp);

        openApp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
            }
        });
        aboutApp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Version 1.2", "About", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        exitApp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });

        trayIcon = new TrayIcon(img, "Gradience App", popup);
        tray = SystemTray.getSystemTray();
        tray.add(trayIcon);
        trayIcon.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private Component getCoursesTab() {
        CoursesTab coursesObj = new CoursesTab(db);
        JPanel panel_Courses = coursesObj.initComponents();
        return panel_Courses;
    }

    private Component getAddHomeworkTab() {
        QuestionConfiguration questionConfigObj = new QuestionConfiguration(db);
        return questionConfigObj.getQuestionConfigurationPanel();
    }

    private Component getUpdateAccountTab() {
        UpdateAccount updateAccountWin = new UpdateAccount(db);
        return updateAccountWin.createAccountSettingsUI();
    }

    private Component getClassRollTab() {
        return new CourseRoll(db).getClassRollPanel();
    }

    private Component getNotificationsTab() {
        return new Notifications(db).getNotificationsPanel();
    }

    private Component getHomeWorksTab() {

        System.out.println(" " + LoggedInUser.role);
        return new CourseListPane(db, LoggedInUser.studentId).getCourseListPane();

    }

    private Component getReportsTab() {
        return new Reports(db).getReportsPanel();

    }

    private Component getQuestionBankTab() {
        return new QuestionBank(db).getQuestionBankPanel();
    }

    private Component getQueryReporterTab() {
        return new QueryPanel(db).getQueryPanel();
    }

    private Component getEditHomeWorkTab() {
        return new EditHomework(db).getEditHomeWorkPanel();
    }

    private Component getAddCourseTab() {
        return new AddNewCourse(db).getAddNewCoursePanel();
    }

    private Component getSubmissionsTab() {
        return new SubmissionsPanel(db).getSubmissionsPanel();
    }

    private SystemTray tray;
    private TrayIcon trayIcon;
    private DataBaseConnection db;
}
