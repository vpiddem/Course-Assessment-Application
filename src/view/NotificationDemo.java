/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class NotificationDemo  {

    public void createNotification(final int count, String notificationMsg) {
        final JFrame f = new JFrame();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss ");
        JLabel lbl = new JLabel("<html>" + sdf.format(new Date()) + notificationMsg + "</html>");
        lbl.setForeground(new Color(0, 0, 0));
        lbl.setIcon(new ImageIcon("icons/Information.png"));
        f.add(lbl);
        f.setSize(320, 80+(notificationMsg.split("<br/>").length - 1) * 10);
        f.setType(Window.Type.UTILITY);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int width = (int) (toolkit.getScreenSize().getWidth() - f.getWidth());
        int height = (int) (toolkit.getScreenSize().getHeight() - f.getHeight() - 40 - (count * 80));
        f.setLocation(width, height);
        f.setVisible(true);
        f.setAlwaysOnTop(true);
        f.setResizable(false);
        Timer t = new Timer(10000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
//                notificationCount--;
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        NotificationDemo obj = new NotificationDemo();
        for (int i = 0; i < 10; i++) {
            obj.createNotification(i, "Deadline Of Homework1 is <br/>approaching.<br/>Deadline of Homweork2 is <br/>also approaching. <br/>");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(NotificationDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

