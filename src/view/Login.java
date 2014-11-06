package view;

import database.AuthenticationService;
import database.DataBaseConnection;
import gradience.GradienceAppConstants;
import gradience.GradienceAppWindow;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.tools.DocumentationTool;
import org.jdesktop.swingx.JXTitledPanel;

/**
 * @author Vikas Piddempally
 */
public class Login implements GradienceAppConstants {

    public Login(DataBaseConnection db) {
        this.db = db;
    }

    public void initComponents() {
        // Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        frame_Login = new JFrame();
        borderObj = new GradienceBorder();
        panel_Container = new JXTitledPanel();
        panel_Login = new JPanel();
        lbl_UserName = new JLabel();
        txt_UserName = new JTextField();
        txt_Password = new JPasswordField();
        lbl_Password = new JLabel();
        panel_SouthButtons = new JPanel();
        btn_Login = new JButton();
        btn_Cancel = new JButton();
        btn_SignUp = new JButton();

        lbl_Prompt = new JLabel();
        lbl_Prompt.setText("Enter your Credentials");

        //======== panel_Container ========
        {
            panel_Container.setLayout(new BorderLayout(8, 8));
            //======== panel_Login ========
            {
                panel_Login.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_Login.getLayout()).columnWidths = new int[]{0, 90, 0};
                ((GridBagLayout) panel_Login.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel_Login.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel_Login.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

                //---- lbl_UserName ----
                lbl_UserName.setText("User Name");
                panel_Login.add(lbl_UserName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_Login.add(txt_UserName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
                panel_Login.add(txt_Password, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- lbl_Password ----
                lbl_Password.setText("Password");
                panel_Login.add(lbl_Password, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                panel_Login.add(lbl_Prompt, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

            }
            panel_Container.add(panel_Login, BorderLayout.CENTER);
        }

        //======== panel_SouthButtons ========
        {
            panel_SouthButtons.setLayout(new FlowLayout());

            //---- btn_Login ----
            btn_Login.setText("Login");
            btn_Login.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    action_btnLogin(); //To change body of generated methods, choose Tools | Templates.
                }
            });
            panel_SouthButtons.add(btn_Login);

            //---- btn_Cancel ----
            btn_Cancel.setText("Cancel");
            panel_SouthButtons.add(btn_Cancel);

            //---- btn_SignUp ----
            btn_SignUp.setText("Sign Up");
            panel_SouthButtons.add(btn_SignUp);
            panel_Container.add(panel_SouthButtons, BorderLayout.SOUTH);
        }
        borderObj.setBorder(panel_Container, "Login", TitledBorder.CENTER, "Serief", Font.BOLD, 12, 0);
        // End of component initialization  //GEN-END:initComponents
        frame_Login.add(panel_Container);
        frame_Login.setVisible(true);
        frame_Login.setLocation(new Point(500, 500));
        frame_Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_Login.setSize(400, 300);
    }

    private void action_btnLogin() {
//        if (db.isValidLoginCredentials(txt_UserName.getText().trim(), txt_Password.getText().trim())) {
        AuthenticationService aService = new AuthenticationService(db);
        int role = aService.authenticate(txt_UserName.getText().trim(), txt_Password.getText().trim());

        if (db.isUserExists(txt_UserName.getText().toLowerCase())) {
            if (db.isOldPassword(txt_UserName.getText(), txt_Password.getText())) {
                lbl_Prompt.setText("Password Changed on " + db.getPasswordUpdateTime(txt_Password.getText()));
                return;
            }
        }

        if (role == -1) {
            lbl_Prompt.setText("*Invalid Credentials*");
            lbl_Prompt.setForeground(Color.RED);
            return;
        } else {
            lbl_Prompt.setForeground(Color.ORANGE);
        }

        frame_Login.setVisible(false);
        appWin = new GradienceAppWindow(db);
        appWin.setTitle("Gradience Application");
        try {
            appWin.createTrayIcon();
        } catch (AWTException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        appWin.setVisible(true);
        appWin.addWindowListener(new winListener());
        db.updateLoginStatus(txt_UserName.getText().trim(), "Y");

//        }
//        else{
//            JOptionPane.showMessageDialog(panel_Login, "Please Enter valid login details","Info",JOptionPane.INFORMATION_MESSAGE);
//        }
    }

    public static GradienceAppWindow getAppWindow() {
        return appWin;
    }

    private static class winListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            appWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            appWin.setVisible(false);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowIconified(WindowEvent e) {
            appWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            appWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        @Override
        public void windowActivated(WindowEvent e) {
            appWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            appWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    private JFrame frame_Login;
    private JXTitledPanel panel_Container;
    private JPanel panel_Login;
    private JLabel lbl_UserName;
    private JTextField txt_UserName;
    private JTextField txt_Password;
    private JLabel lbl_Password;
    private JPanel panel_SouthButtons;
    private JButton btn_Login;
    private JButton btn_Cancel;
    private JButton btn_SignUp;
    // End of variables declaration  //GEN-END:variables
    private GradienceBorder borderObj;
    private DataBaseConnection db;
    private static GradienceAppWindow appWin;
    private JLabel lbl_Prompt;
}
