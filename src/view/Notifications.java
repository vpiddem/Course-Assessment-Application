package view;

import beans.LoggedInUser;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import database.DataBaseConnection;
import database.HomeworkService;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Vikas Piddempally
 */
public class Notifications implements gradience.GradienceAppConstants {

    DataBaseConnection db;

    public Notifications(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getNotificationsPanel() {
        panel_NotificationsContainer = new JPanel();
        panel_Content = new JPanel();
        lbl_Notification1 = new JLabel();
        lbl_Notification2 = new JLabel();

        //======== panel_NotificationsContainer ========
        {

//        ArrayList<String> list_Notifications = db.getNotificationsById(1,1);
            panel_NotificationsContainer.setLayout(new BorderLayout(6, 6));

            //======== panel_Content ========
            {
                panel_Content.setLayout(new GridLayout(20, 1, 10, 10));

                HomeworkService homeworkService = new HomeworkService();
                List list_Notifications = new ArrayList();

                switch (LoggedInUser.role) {
                    case ROLE_INSTRUCTOR:
                        list_Notifications = homeworkService.getNotificationsById(LoggedInUser.professorId, ROLE_INSTRUCTOR);
                        break;

                    case ROLE_TA:
                    case ROLE_STUDENT:
                        list_Notifications = homeworkService.getNotificationsById(LoggedInUser.studentId, ROLE_STUDENT);
                        break;
                }

                for (int i = 0; i < list_Notifications.size(); i++) {
                    JLabel lbl_Notification = new JLabel();
                    lbl_Notification.setText(list_Notifications.get(i).toString());
                    lbl_Notification.setForeground(Color.RED);
                    panel_Content.add(lbl_Notification);
                }
            }
            panel_NotificationsContainer.add(panel_Content, BorderLayout.CENTER);
        }
        return panel_NotificationsContainer;
    }

    private JPanel panel_NotificationsContainer;
    private JPanel panel_Content;
    private JLabel lbl_Notification1;
    private JLabel lbl_Notification2;
}
