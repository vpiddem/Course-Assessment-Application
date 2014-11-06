/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import beans.LoggedInUser;
import database.DataBaseConnection;
import gradience.GradienceAppConstants;
import homework.FetchPastDeadlineHomeWorks;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Vikas Piddempally
 */
public class SubmissionsPanel implements GradienceAppConstants{

    DataBaseConnection db;

    public SubmissionsPanel(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getSubmissionsPanel() {
        panel_Container = new JPanel();
        panel_North = new JPanel();
        lbl_SelectHomeWork = new JLabel();
        cbox_HomeWorkNamesList = new JComboBox();
        lbl_Arrow = new JLabel();
        lbl_AttemptId = new JLabel();
        cbox_AttemptIDList = new JComboBox();
        btn_FetchSubmission = new JButton();
        panel_Center = new JPanel();
        panel_SubmissionData = new JPanel();
        panel_South = new JPanel();

        //======== panel_Container ========
        {
            panel_Container.setLayout(new BorderLayout(5, 5));

            //======== panel_North ========
            {
                panel_North.setBorder(new TitledBorder(null, "Filter Settings", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                        new Font("Tahoma", Font.BOLD, 14), new Color(0, 104, 136)));
                panel_North.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- lbl_SelectHomeWork ----
                lbl_SelectHomeWork.setText("Select HomeWork :");
                cbox_HomeWorkNamesList.setModel(new DefaultComboBoxModel(db.getHomeWorkNamesList()));
                cbox_HomeWorkNamesList.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int selectedHomeWorkID = db.getHomeWorkIdByHomeWorkName(cbox_HomeWorkNamesList.getSelectedItem().toString());
//                        cbox_AttemptIDList.setModel(new DefaultComboBoxModel(db.getAttemptIDListByHomeWorkID(selectedHomeWorkID)));
                    }
                });

                panel_North.add(lbl_SelectHomeWork);
                panel_North.add(cbox_HomeWorkNamesList);

                //---- lbl_Arrow ----
                lbl_Arrow.setText(" => ");
                lbl_Arrow.setForeground(Color.ORANGE);
                
                panel_North.add(lbl_Arrow);

                
                if(LoggedInUser.role == ROLE_STUDENT){
                //---- lbl_AttemptId ----
//                lbl_AttemptId.setText("Select Attempt ID:");
//                panel_North.add(lbl_AttemptId);
//                panel_North.add(cbox_AttemptIDList);
                }

                //---- btn_FetchSubmission ----
                btn_FetchSubmission.setText("Fetch Submission");
                btn_FetchSubmission.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        updateSubmissionPanelData();
                    }

                });
                panel_North.add(btn_FetchSubmission);
            }
            panel_Container.add(panel_North, BorderLayout.NORTH);

            //======== panel_Center ========
            {
                panel_Center.setLayout(new BorderLayout(5, 5));

                //======== panel_SubmissionData ========
                {
                    panel_SubmissionData.setBorder(new TitledBorder(null, "Submission Data", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                            new Font("Tahoma", Font.BOLD, 14), new Color(0, 104, 136)));
                    panel_SubmissionData.setLayout(new BorderLayout(5, 5));

                    scrollPane_Submissions = new JScrollPane();
                    scrollPane_Submissions.setViewportView(new JLabel("Click on Fetch Submission Button to display Data"));

                    panel_SubmissionData.add(scrollPane_Submissions);
                }
                panel_Center.add(panel_SubmissionData, BorderLayout.CENTER);
            }
            panel_Container.add(panel_Center, BorderLayout.CENTER);

            //======== panel_South ========
            {
                panel_South.setLayout(new FlowLayout());
            }
            panel_Container.add(panel_South, BorderLayout.SOUTH);
        }
        return panel_Container;
    }

    private void updateSubmissionPanelData() {
        try {
//            int attemptId = Integer.parseInt(cbox_AttemptIDList.getSelectedItem().toString());
            int homeWorkId = db.getHomeWorkIdByHomeWorkName(cbox_HomeWorkNamesList.getSelectedItem().toString());
            scrollPane_Submissions.setViewportView(new FetchPastDeadlineHomeWorks(homeWorkId).getPastHomeWorksPanel());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel_Center, "Unable to Load Data.. \n Please try again.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel panel_Container;
    private JPanel panel_North;
    private JLabel lbl_SelectHomeWork;
    private JComboBox cbox_HomeWorkNamesList;
    private JLabel lbl_Arrow;
    private JLabel lbl_AttemptId;
    private JComboBox cbox_AttemptIDList;
    private JButton btn_FetchSubmission;
    private JPanel panel_Center;
    private JPanel panel_SubmissionData;
    private JScrollPane scrollPane_Submissions;
    private JPanel panel_South;
}
