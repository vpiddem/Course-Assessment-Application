/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import beans.Attempt;
import beans.LoggedInUser;
import database.DataBaseConnection;
import database.HomeworkService;
import homework.FetchHomeWork;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;

/**
 * @author Vikas Piddempally
 */
public class Reports implements gradience.GradienceAppConstants {

    DataBaseConnection db = null;

    public Reports(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getReportsPanel() {
        panel_Container = new JPanel();
        panel_north = new JPanel();
        lbl_SelectHomeWorkID = new JLabel();
        cbox_HomeWorkID = new JComboBox();
        lbl_StudentID = new JLabel("Select Student :");
        cbox_StudentIDNames = new JComboBox();
        lbl_CourseNames = new JLabel();
        cbox_CourseNames = new JComboBox();
        panelCenter = new JPanel();
        splitPane_SubmissionDetails = new JSplitPane();
        panel_AttemptsListTable = new JPanel();
        panel_ScoresTop = new JPanel();

        //======== panel_Container ========
        {
            panel_Container.setLayout(new BorderLayout(5, 5));

            //======== panel_north ========
            {
                panel_north.setLayout(new FlowLayout());

                //---- lbl_SelectHomeWorkID ----
                lbl_SelectHomeWorkID.setText("Select Homework ID:");

                //---- lbl_CourseNames ----
                lbl_CourseNames.setText("Course Names :");

                if (LoggedInUser.role == ROLE_STUDENT || LoggedInUser.role == ROLE_TA) {
                    cbox_CourseNames = new JComboBox(db.getCourseNameListByStudentID(LoggedInUser.studentId));
                } else {
                    cbox_CourseNames = new JComboBox(db.getCourseNameListByProfessorID(LoggedInUser.professorId));
                }

                cbox_CourseNames.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseNames.getSelectedItem().toString());
                        cbox_HomeWorkID.setModel(new DefaultComboBoxModel(db.getHomeWorksByCourseID(selectedCourseID)));
                    }
                });

                if (LoggedInUser.role == ROLE_INSTRUCTOR) {
                    cbox_StudentIDNames.setModel(new DefaultComboBoxModel(db.getStudentIdNameList()));
                } else {
                    cbox_StudentIDNames.addItem(LoggedInUser.studentId + "|" + LoggedInUser.name);
                }
                JButton btn_ViewAttempts = new JButton("View Attempts");
                btn_ViewAttempts.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        retrieveAttemptsDataAndUpdateView();
                    }
                });

                panel_north.add(lbl_StudentID);
                panel_north.add(cbox_StudentIDNames);
                panel_north.add(lbl_SelectHomeWorkID);
                panel_north.add(cbox_HomeWorkID);
                panel_north.add(lbl_CourseNames);
                panel_north.add(cbox_CourseNames);
                panel_north.add(btn_ViewAttempts);
            }
            panel_Container.add(panel_north, BorderLayout.NORTH);

            //======== panelCenter ========
            {
                panelCenter.setLayout(new BorderLayout(5, 5));

                //======== splitPane_SubmissionDetails ========
                {
                    splitPane_SubmissionDetails.setDividerLocation(300);

                    JPanel panel_TablesContainerLeft = new JPanel();
                    panel_TablesContainerLeft.setLayout(new GridLayout(2, 1));

                    //======== panel_AttemptsListTable ========
                    {
                        panel_AttemptsListTable.setLayout(new BorderLayout(5, 5));
                        vector_attemptHeaders = new Vector<String>();
                        vector_attemptHeaders.add("Attempt ID");
                        vector_attemptHeaders.add("Attempt Name");
                        vector_attemptHeaders.add("Score");
                        vector_attemptHeaders.add("Max Score");

                        table_ListOfAttempts = new JXTable(new DefaultTableModel(null, vector_attemptHeaders));
                        scroll_AttemptsTable = new JScrollPane();
                        scroll_AttemptsTable.setViewportView(table_ListOfAttempts);
                    }
                    setListSelectionListener_AttemptsTable(table_ListOfAttempts);
                    panel_AttemptsListTable.add(scroll_AttemptsTable);

                    panel_ScoresFinal = new JPanel();
                    scroll_FinalScores = new JScrollPane();
//                    panel_ScoresFinal.add(scroll_FinalScores);
                    new GradienceBorder().setBorder(panel_ScoresFinal, "Final Grades", 14, 5);
                    panel_TablesContainerLeft.add(panel_AttemptsListTable);
                    panel_TablesContainerLeft.add(scroll_FinalScores);
                    splitPane_SubmissionDetails.setLeftComponent(panel_TablesContainerLeft);

                    //======== panel_Scores ========
                    {
                        panel_ScoresTop.setLayout(new BorderLayout(5, 5));
                        scroll_SubmissionsRight = new JScrollPane();
                        table_SubmissionByID = new JXTable();
                        scroll_SubmissionsRight.setViewportView(table_SubmissionByID);
                        panel_ScoresTop.add(scroll_SubmissionsRight);
                    }
                    splitPane_SubmissionDetails.setRightComponent(scroll_SubmissionsRight);
                    splitPane_SubmissionDetails.setDividerLocation(300);
                }
                panelCenter.add(splitPane_SubmissionDetails, BorderLayout.CENTER);
            }
            panel_Container.add(panelCenter, BorderLayout.CENTER);
        }
        return panel_Container;
    }

    public void setListSelectionListener_AttemptsTable(final JXTable table_AttemptsList) {
        table_AttemptsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boolean ALLOW_ROW_SELECTION = table_AttemptsList.getRowSelectionAllowed();
        if (ALLOW_ROW_SELECTION) { // true by default
            ListSelectionModel rowSM = table_AttemptsList.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        System.out.println("No rows are selected.");
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        System.out.println("Row " + selectedRow
                                + " is now selected.");
                        TableModel model_Attempts = table_AttemptsList.getModel();
                        int selectedAttemptID = Integer.parseInt(model_Attempts.getValueAt(table_AttemptsList.getSelectedRow(), 0).toString());
                        System.out.println(" " + selectedAttemptID);
                        try {
                            int attemptID = Integer.parseInt(table_AttemptsList.getValueAt(table_AttemptsList.getSelectedRow(), 0).toString());
                            updateScorestViewByAttemptID(attemptID);
                        } catch (Exception ex) {
                            System.out.println("@In Attempts View List Selection Listener");
                            ex.printStackTrace();
                        } finally {
                        }
                    }
                }
            });
        } else {
            table_AttemptsList.setRowSelectionAllowed(false);
        }
    }

    private void updateScorestViewByAttemptID(int attemptID) {
        panel_ScoresTop = new FetchHomeWork().getHomeWorkPanelByAttemptID(attemptID);
        scroll_SubmissionsRight.setViewportView(panel_ScoresTop);
    }

    private void retrieveAttemptsDataAndUpdateView() {
        HomeworkService homeWork = new HomeworkService();
        int homeWorkID = 0;
        try {
            homeWorkID = Integer.parseInt(cbox_HomeWorkID.getSelectedItem().toString());

        } catch (Exception e) {
        }
        List<Attempt> list_Attempts = homeWork.getAttemptsByHomeworkId(homeWorkID, LoggedInUser.studentId);
        int i = 0;
        Vector<Vector<Object>> vector_OuterData = new Vector<>();
        while (i < list_Attempts.size()) {
            Vector<Object> vector_cellData = new Vector<>();
            vector_cellData.add(list_Attempts.get(i).getId());
            vector_cellData.add(list_Attempts.get(i).getName());
            vector_cellData.add(list_Attempts.get(i).getScoreObtained());
            vector_cellData.add(list_Attempts.get(i).getMaxScore());
            vector_OuterData.add(vector_cellData);
            i++;
        }
        table_ListOfAttempts.setModel(new DefaultTableModel(vector_OuterData, vector_attemptHeaders));
        scroll_AttemptsTable.setViewportView(table_ListOfAttempts);

        //Also Update FinalScores scrollPane View
        scroll_FinalScores.setViewportView(getFinalScoresPanel(LoggedInUser.studentId));
        new GradienceBorder().setBorder(scroll_FinalScores, "Final Grades", 14, 3);
    }

    private JFrame frame;
    private JPanel panel_Container;
    private JPanel panel_north;
    private JLabel lbl_SelectHomeWorkID;
    private JComboBox cbox_HomeWorkID;
    private JLabel lbl_StudentID;
    private JComboBox cbox_StudentIDNames;
    private JLabel lbl_CourseNames;
    private JComboBox cbox_CourseNames;
    private JPanel panelCenter;
    private JSplitPane splitPane_SubmissionDetails;
    private JPanel panel_AttemptsListTable;
    private JScrollPane scroll_AttemptsTable;
    private JScrollPane scroll_FinalScores;
    private JScrollPane scroll_SubmissionsRight;
    private JXTable table_ListOfAttempts, table_SubmissionByID;
    private JPanel panel_ScoresTop;
    private JPanel panel_ScoresFinal;
    private Vector<String> vector_attemptHeaders;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private JXTable getFinalScoresPanel(int studentId) {
        panel_ScoresFinal = new JPanel();
        JXTable table_Scores = new JXTable();

        Vector<String> vector_FinalScoreHeaders = new Vector<>();
        vector_FinalScoreHeaders.add("HomeWork Name");
        vector_FinalScoreHeaders.add("Obtained Final Score");
        vector_FinalScoreHeaders.add("Max Score");

        int selectedStudentID = Integer.parseInt(cbox_StudentIDNames.getSelectedItem().toString().split("|")[0]);
        db.setFinalScoresTable(table_Scores, vector_FinalScoreHeaders, selectedStudentID);
//        scroll_FinalScores.setViewportView(table_Scores);
//        panel_ScoresFinal.add(table_Scores);
//        return panel_ScoresFinal;
        return table_Scores;
    }
}
