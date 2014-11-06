package view;

import beans.LoggedInUser;
import database.DataBaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JSpinner.DateEditor;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import structures.Homework;

/**
 * @author Vikas Piddempally
 */
public class QuestionConfiguration {

    private DataBaseConnection db;

    public QuestionConfiguration(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getQuestionConfigurationPanel() {
        createQuestionConfigurationPanel();
        return panel_QuestionConfigurationMain;
    }

    private void createQuestionConfigurationPanel() {
        panel_North = new JPanel();
        panel_QuestionConfigurationMain = new JPanel();
        panel_QuestionConfigurationMain.setLayout(new BorderLayout(5, 5));
        panel_Center = new JPanel();
        scrollPane_QuestionsTable = new JScrollPane();
        panel_South = new JPanel();
        btn_SubmitAndCreateExercise = new JButton();
        btn_Reset = new JButton();

        //======== panel_North ========
        {
            createNorthFilterPanel();
            panel_North.add(panel_Filters, BorderLayout.CENTER);
        }

        //======== panel_Center ========
        {
            panel_Center.setLayout(new BorderLayout(5, 5));

            table_Questions = new JXTable();
            scrollPane_QuestionsTable.setViewportView(table_Questions);

            vector_QuestionHeaders = new Vector<>();

            vector_QuestionHeaders.add("Select");
            vector_QuestionHeaders.add("Question ID");
            vector_QuestionHeaders.add("Question");

            table_Questions.setVisibleRowCount(30);
            table_Questions.setModel(new DefaultTableModel(null, vector_QuestionHeaders));

            table_Questions.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent ev) {
                    if (ev.isPopupTrigger()) {
                        if (table_Questions.getSelectedRowCount() > 0) {
                            menu_QuestionsTable.show(ev.getComponent(), ev.getX(), ev.getY());
                        } else {
                            JOptionPane.showMessageDialog(panel_Center, "Select atleast One Question Record");
                        }
                    }
                }
            });
            db.setQuestionsTable(table_Questions, vector_QuestionHeaders);
            createRightClickQuestionsTableMenu();
            table_Questions.setShowGrid(true);
            table_Questions.getTableHeader().setReorderingAllowed(false);
            table_Questions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table_Questions.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
            table_Questions.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));

            panel_Center.add(scrollPane_QuestionsTable, BorderLayout.CENTER);
        }

        //======== panel_South ========
        {
            panel_South.setLayout(new FlowLayout());

            //---- btn_Submit ----
            btn_SubmitAndCreateExercise.setText("Submit Questions & Create Homework");
            panel_South.add(btn_SubmitAndCreateExercise);
            btn_SubmitAndCreateExercise.addActionListener(new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    action_btnCreateHomeworkRelatedQuestions();
                }

            });

            //---- btn_Reset ----
            btn_Reset.setText("Reset");
            btn_Reset.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    enableAllFields();
                }
            });
            panel_South.add(btn_Reset);
        }
        panel_QuestionConfigurationMain.add(panel_North, BorderLayout.NORTH);
        panel_QuestionConfigurationMain.add(panel_Center, BorderLayout.CENTER);
        panel_QuestionConfigurationMain.add(panel_South, BorderLayout.SOUTH);
        //GEN-END:initComponents
    }

    private void createRightClickQuestionsTableMenu() {
        menu_QuestionsTable = new JPopupMenu();
        menuItem_SelectAll = new JMenuItem("Select All");
        menuItem_SelectAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                menuItem_SelectAllActionPerformed();
            }

        });
        menu_QuestionsTable.add(menuItem_SelectAll);
        menuItem_DeSelectAll = new JMenuItem("Deselect All");
        menuItem_DeSelectAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                menuItem_DeSelectAllActionPerformed();
            }

        });
        menu_QuestionsTable.add(menuItem_DeSelectAll);
    }

    private void menuItem_DeSelectAllActionPerformed() {
        for (int i = 0; i < table_Questions.getRowCount(); i++) {
            table_Questions.setValueAt(false, i, 0);
        }
    }

    private void menuItem_SelectAllActionPerformed() {
        for (int i = 0; i < table_Questions.getRowCount(); i++) {
            table_Questions.setValueAt(true, i, 0);
        }
    }

    private void createNorthFilterPanel() {
        panel_Filters = new JPanel();
        panel_NorthFilters = new JPanel();
        lbl_DifficultyLevel = new JLabel();
        cbox_DifficultyLevel = new JComboBox<>();
        lbl_SelectTopic = new JLabel();
        cbox_Topic = new JComboBox<>();
        panel_SouthApply = new JPanel();
        btn_ApplyConfiguration = new JButton();
        btn_ShowAllQuestions = new JButton();
        panel_ConfigParameters = new JPanel();
        lbl_ExerciseName = new JLabel();
        txt_ExerciseName = new JTextField();
        lbl_StartDate = new JLabel();
        spinner_StartDate = new JSpinner();
        lbl_CorrectPoints = new JLabel();
        txt_CorrectAnswerPoints = new JTextField();
        lbl_RandomizationSeed = new JLabel();
        txt_RandomizationSeed = new JTextField();
        lbl_MaxRetries = new JLabel();
        txt_MaxRetries = new JTextField();
        lbl_EndDate = new JLabel();
        spinner_EndDate = new JSpinner();
        lbl_PenaltyPoints = new JLabel();
        txt_WrongAnswerPenalty = new JTextField();
        lbl_ScoreSelectionMethod = new JLabel();
        cbox_ScoreSelectionMethod = new JComboBox<>();

        //======== panel_Filters ========
        {

            panel_Filters.setLayout(new BorderLayout(6, 6));

            //======== panel_NorthFilters ========
            {
                panel_NorthFilters.setBorder(new LineBorder(Color.gray, 1, true));
                panel_NorthFilters.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- lbl_DifficultyLevel ----
                lbl_DifficultyLevel.setText("Difficulty Level");
                panel_NorthFilters.add(lbl_DifficultyLevel);

                //---- cbox_DifficultyLevel ----
                cbox_DifficultyLevel.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Update the Task Name Lists
                        int difficultyLevel = Integer.parseInt(cbox_DifficultyLevel.getSelectedItem().toString());
                        //Update Signal_Info Scroll View Data
                        DefaultTableModel tm = new DefaultTableModel();
                        if ((cbox_DifficultyLevel.getSelectedIndex() != 0)) {
                            db.setQuestionsTableByDifficultyLevel(table_Questions, vector_QuestionHeaders, difficultyLevel);
                        } else {
                            return;
                        }

                        scrollPane_QuestionsTable.setViewportView(table_Questions);
                    }
                });

                cbox_DifficultyLevel.setModel(new DefaultComboBoxModel<>(new String[]{
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6"
                }));
                panel_NorthFilters.add(cbox_DifficultyLevel);

                lbl_SelectCourse = new JLabel("Select a Course:");
                cbox_CourseList = new JComboBox(db.getCourseNameListByProfessorID(LoggedInUser.professorId));

                lbl_SelectTopic = new JLabel("Select Topic :");
                int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseList.getSelectedItem().toString());
                cbox_Topic = new JComboBox(db.getTopicDataByCourseId(selectedCourseID));

                panel_NorthFilters.add(lbl_SelectCourse);
                panel_NorthFilters.add(cbox_CourseList);
                cbox_CourseList.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseList.getSelectedItem().toString());
                        cbox_Topic.setModel(new DefaultComboBoxModel(db.getTopicDataByCourseId(selectedCourseID)));
                    }
                });

                
                cbox_Topic.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        
                        
                        //Update the Questions Name Lists
                        int topicID = db.getTopicIDByTopicName(cbox_Topic.getSelectedItem().toString());
                        //Update Scroll View Data
                        DefaultTableModel tm = new DefaultTableModel();
                        if ((cbox_Topic.getSelectedIndex() != 0)) {
                            db.setQuestionsTableByTopicID(table_Questions, vector_QuestionHeaders, topicID);
                        } else {
                            return;
                        }
                        
                        
                    }
                });
                
                
                
                panel_NorthFilters.add(lbl_SelectTopic);
                panel_NorthFilters.add(cbox_Topic);

                lbl_HomeWorkID = new JLabel("HomeWork ID :");
                String homeWorkIdString = "";
                try {
                    homeWorkIdString = db.getIncrementMaxHomeWorkID();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                txt_HomeWorkID = new JTextField();
                txt_HomeWorkID.setText(homeWorkIdString);
                txt_HomeWorkID.setEditable(false);
                panel_NorthFilters.add(lbl_HomeWorkID);
                panel_NorthFilters.add(txt_HomeWorkID);

            }
            panel_Filters.add(panel_NorthFilters, BorderLayout.NORTH);

            //======== panel_SouthApply ========
            {
                panel_SouthApply.setBorder(new LineBorder(Color.darkGray));
                panel_SouthApply.setLayout(new FlowLayout());

                //---- btn_ApplyConfiguration ----
                btn_ApplyConfiguration.setText("Apply Configuration");
                btn_ApplyConfiguration.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        createHomeworkWithSetOptions();
                        disableAllFieldsUntilReset();
                    }

                });
                panel_SouthApply.add(btn_ApplyConfiguration);

                //---- btn_ShowAll ----
                btn_ShowAllQuestions.setText("Show All");
                panel_SouthApply.add(btn_ShowAllQuestions);
            }
            panel_Filters.add(panel_SouthApply, BorderLayout.SOUTH);

            //======== panel2 ========
            {
                panel_ConfigParameters.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_ConfigParameters.getLayout()).columnWidths = new int[]{0, 61, 0, 0, 0, 46, 0, 52, 0};
                ((GridBagLayout) panel_ConfigParameters.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
                ((GridBagLayout) panel_ConfigParameters.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel_ConfigParameters.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0E-4};

                //---- lbl_ExerciseName ----
                lbl_ExerciseName.setText("Exercise Name");
                panel_ConfigParameters.add(lbl_ExerciseName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_ConfigParameters.add(txt_ExerciseName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_StartDate ----
                lbl_StartDate.setText("Start Date");
                panel_ConfigParameters.add(lbl_StartDate, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                spinner_StartDate = new JSpinner();
                spinner_StartDate.setModel(new SpinnerDateModel());
                spinner_StartDate.setEditor(new DateEditor(spinner_StartDate, "MM-dd-yyyy HH:mm:ss"));

                panel_ConfigParameters.add(spinner_StartDate, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_CorrectPoints ----
                lbl_CorrectPoints.setText("Correct Answer Points");
                panel_ConfigParameters.add(lbl_CorrectPoints, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_ConfigParameters.add(txt_CorrectAnswerPoints, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_RandomizationSeed ----
                lbl_RandomizationSeed.setText("Randomization Seed");
                panel_ConfigParameters.add(lbl_RandomizationSeed, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_ConfigParameters.add(txt_RandomizationSeed, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- lbl_MaxRetries ----
                lbl_MaxRetries.setText("Max Retries");
                panel_ConfigParameters.add(lbl_MaxRetries, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_ConfigParameters.add(txt_MaxRetries, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_EndDate ----
                lbl_EndDate.setText("End Date");
                panel_ConfigParameters.add(lbl_EndDate, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                spinner_EndDate = new JSpinner();
                spinner_EndDate.setModel(new SpinnerDateModel());
                spinner_EndDate.setEditor(new DateEditor(spinner_EndDate, "MM-dd-yyyy HH:mm:ss"));
                panel_ConfigParameters.add(spinner_EndDate, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_PenaltyPoints ----
                lbl_PenaltyPoints.setText("Wrong Answer Penalty");
                panel_ConfigParameters.add(lbl_PenaltyPoints, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                panel_ConfigParameters.add(txt_WrongAnswerPenalty, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- label1 ----
                lbl_ScoreSelectionMethod.setText("Score Selection Method");
                panel_ConfigParameters.add(lbl_ScoreSelectionMethod, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- cbox_ScoreSelectionMethod ----
                cbox_ScoreSelectionMethod.setModel(new DefaultComboBoxModel<>(new String[]{
                    "Latest Attempt",
                    "Maximum Score",
                    "Average Score"
                }));
                panel_ConfigParameters.add(cbox_ScoreSelectionMethod, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
            }
            panel_Filters.add(panel_ConfigParameters, BorderLayout.CENTER);
        }
    }

    private void disableAllFieldsUntilReset() {
        txt_CorrectAnswerPoints.setEditable(false);
        txt_ExerciseName.setEditable(false);
        txt_MaxRetries.setEditable(false);
        txt_WrongAnswerPenalty.setEditable(false);
        cbox_ScoreSelectionMethod.setEnabled(false);
        spinner_EndDate.setEnabled(false);
        spinner_StartDate.setEnabled(false);
    }

    private void enableAllFields() {
        txt_CorrectAnswerPoints.setEditable(true);
        txt_ExerciseName.setEditable(true);
        txt_MaxRetries.setEditable(true);
        txt_WrongAnswerPenalty.setEditable(true);
        cbox_ScoreSelectionMethod.setEnabled(true);
        spinner_EndDate.setEnabled(true);
        spinner_StartDate.setEnabled(true);
    }

    private void createHomeworkWithSetOptions() {
        String startTime = "", endTime = "";
        try {
            startTime = spinner_StartDate.getValue().toString();
            System.out.println(" " + startTime);
            endTime = spinner_EndDate.getValue().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            Date temp_Start = sdf.parse(startTime);
            Date temp_Stop = sdf.parse(endTime);

            //Convert the UTF Format Date to yyyy-mm-yy HH:mm:ss
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            startTime = df.format(temp_Start);
            endTime = df.format(temp_Stop);
            Date Date_Start = df.parse(startTime);
            System.out.println(" " + startTime);

        } catch (ParseException ex) {
            Logger.getLogger(QuestionConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Homework homeWorkStructure = new Homework();
            homeWorkStructure.setDifficultyLevel(cbox_DifficultyLevel.getSelectedIndex());
            homeWorkStructure.setHomeworkName(txt_ExerciseName.getText().trim());
            homeWorkStructure.setStartTime(startTime);
            homeWorkStructure.setEndTime(endTime);
            homeWorkStructure.setNoOfRetries(Integer.parseInt(txt_MaxRetries.getText().trim()));
            homeWorkStructure.setMarksPerQuestion(Integer.parseInt(txt_CorrectAnswerPoints.getText().trim()));
            homeWorkStructure.setPenaltyPerQuestion(Integer.parseInt(txt_WrongAnswerPenalty.getText().trim()));
            homeWorkStructure.setScoreSelectionMethod(cbox_DifficultyLevel.getSelectedIndex());
            homeWorkStructure.setTopic(1);
            homeWorkStructure.setRandomizationSeed(Integer.parseInt(txt_RandomizationSeed.getText()));
            homeWorkStructure.setCourseID(db.getCourseIdByCourseName(cbox_CourseList.getSelectedItem().toString()));
            db.insertIntoHomeWorksTable(homeWorkStructure);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void action_btnCreateHomeworkRelatedQuestions() {
        try {
            ArrayList<Integer> list_QuestionIDs = new ArrayList<Integer>();
            for (int i = 0; i < table_Questions.getRowCount(); i++) {
                if (table_Questions.getValueAt(i, 0).equals(true)) {
                    list_QuestionIDs.add(Integer.parseInt(table_Questions.getValueAt(i, 1).toString()));
                }
            }
            db.insertIntoQuestionsTableByHomeWork(list_QuestionIDs, Integer.parseInt(txt_HomeWorkID.getText().toString().trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel panel_QuestionConfigurationMain;
    private JPanel panel_North;
    private JPanel panel_Center;
    private JScrollPane scrollPane_QuestionsTable;
    private JPanel panel_South;
    private JButton btn_SubmitAndCreateExercise;
    private JButton btn_Reset;
    private JPopupMenu menu_QuestionsTable;
    private JMenuItem menuItem_SelectAll, menuItem_DeSelectAll;
    Vector<String> vector_QuestionHeaders;
    private JXTable table_Questions;
    // End of variables declaration  

    private JPanel panel_Filters;
    private JPanel panel_NorthFilters;
    private JLabel lbl_DifficultyLevel, lbl_SelectTopic, lbl_SelectCourse, lbl_HomeWorkID;
    private JComboBox cbox_DifficultyLevel, cbox_Topic, cbox_CourseList;
    private JTextField txt_HomeWorkID;
    private JPanel panel_SouthApply;
    private JButton btn_ApplyConfiguration;
    private JButton btn_ShowAllQuestions;
    private JPanel panel_ConfigParameters;
    private JLabel lbl_ExerciseName;
    private JTextField txt_ExerciseName;
    private JLabel lbl_StartDate;
    private JSpinner spinner_StartDate;
    private JLabel lbl_CorrectPoints;
    private JTextField txt_CorrectAnswerPoints;
    private JLabel lbl_RandomizationSeed;
    private JTextField txt_RandomizationSeed;
    private JLabel lbl_MaxRetries;
    private JTextField txt_MaxRetries;
    private JLabel lbl_EndDate;
    private JSpinner spinner_EndDate;
    private JLabel lbl_PenaltyPoints;
    private JTextField txt_WrongAnswerPenalty;
    private JLabel lbl_ScoreSelectionMethod;
    private JComboBox<String> cbox_ScoreSelectionMethod;
}
