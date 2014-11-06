package view;

import beans.LoggedInUser;
import database.DataBaseConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.LineBorder;
import structures.Homework;

/**
 *
 * @author Vikas
 */
public class EditHomework {

    DataBaseConnection db;

    public EditHomework(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getEditHomeWorkPanel() {
        panel_Filters = new JPanel();
        panel_NorthFilters = new JPanel();
        lbl_DifficultyLevel = new JLabel();
        cbox_DifficultyLevel = new JComboBox<>();
        lbl_SelectTopic = new JLabel();
        cbox_Topic = new JComboBox<>();
        panel_SouthApply = new JPanel();
        btn_Update = new JButton();
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
//                int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseList.getSelectedItem().toString());
                cbox_Topic = new JComboBox(db.getTopicDataByCourseId(1));

                panel_NorthFilters.add(lbl_SelectCourse);
                panel_NorthFilters.add(cbox_CourseList);
                cbox_CourseList.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseList.getSelectedItem().toString());
                        cbox_Topic.setModel(new DefaultComboBoxModel(db.getTopicDataByCourseId(selectedCourseID)));
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

                cbox_HomeWorkIds = new JComboBox(db.getHomeWorksIDList());

                cbox_HomeWorkIds.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        updatePanelWithHomeWorkSpecificData();
                    }

                });

                txt_HomeWorkID = new JTextField();
                txt_HomeWorkID.setText(homeWorkIdString);
                txt_HomeWorkID.setEditable(false);
                panel_NorthFilters.add(lbl_HomeWorkID);
                panel_NorthFilters.add(cbox_HomeWorkIds);

            }
            panel_Filters.add(panel_NorthFilters, BorderLayout.NORTH);

            //======== panel_SouthApply ========
            {
                panel_SouthApply.setBorder(new LineBorder(Color.darkGray));
                panel_SouthApply.setLayout(new FlowLayout());

                //---- btn_ApplyConfiguration ----
                btn_Update.setText("Update");
                btn_Update.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        action_btnUpdatePerformed();
                    }
                });
                panel_SouthApply.add(btn_Update);
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
                spinner_StartDate.setEditor(new JSpinner.DateEditor(spinner_StartDate, "MM-dd-yyyy HH:mm:ss"));

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
                spinner_EndDate.setEditor(new JSpinner.DateEditor(spinner_EndDate, "MM-dd-yyyy HH:mm:ss"));
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
        return panel_Filters;
    }

    private void action_btnUpdatePerformed() {
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
            homeWorkStructure.setRandomizationSeed(Integer.parseInt(txt_RandomizationSeed.getText()));
            homeWorkStructure.setNoOfRetries(Integer.parseInt(txt_MaxRetries.getText().trim()));
            homeWorkStructure.setMarksPerQuestion(Integer.parseInt(txt_CorrectAnswerPoints.getText().trim()));
            homeWorkStructure.setPenaltyPerQuestion(Integer.parseInt(txt_WrongAnswerPenalty.getText().trim()));
            homeWorkStructure.setScoreSelectionMethod(cbox_DifficultyLevel.getSelectedIndex());
            homeWorkStructure.setTopic(1);
            homeWorkStructure.setCourseID(db.getCourseIdByCourseName(cbox_CourseList.getSelectedItem().toString()));
            homeWorkStructure.setHomeworkId(Integer.parseInt(cbox_HomeWorkIds.getSelectedItem().toString()));
            db.updateHomeWorksTable(homeWorkStructure);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePanelWithHomeWorkSpecificData() {
        Homework homeWorkStructure = new Homework();
        int selectedHomeWorkID = Integer.parseInt(cbox_HomeWorkIds.getSelectedItem().toString());
        db.setHomeWorkStructureFromDB(homeWorkStructure,selectedHomeWorkID);
        txt_ExerciseName.setText(homeWorkStructure.getHomeworkName());
        txt_CorrectAnswerPoints.setText(homeWorkStructure.getMarksPerQuestion()+"");
        txt_HomeWorkID.setText(homeWorkStructure.getHomeworkId()+"");
        txt_MaxRetries.setText(homeWorkStructure.getNoOfRetries()+"");
        txt_RandomizationSeed.setText(homeWorkStructure.getRandomizationSeed()+"");
        txt_WrongAnswerPenalty.setText(homeWorkStructure.getPenaltyPerQuestion()+"");
        cbox_CourseList.setSelectedItem(db.getCourseNameByCourseID(homeWorkStructure.getCourseID()));
        cbox_DifficultyLevel.setSelectedItem(homeWorkStructure.getDifficultyLevel()+"");
    }

    private JPanel panel_Filters;
    private JPanel panel_NorthFilters;
    private JLabel lbl_DifficultyLevel, lbl_SelectTopic, lbl_SelectCourse, lbl_HomeWorkID;
    private JComboBox cbox_DifficultyLevel, cbox_Topic, cbox_CourseList;
    private JTextField txt_HomeWorkID;
    private JPanel panel_SouthApply;
    private JButton btn_Update;
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
    private JComboBox<String> cbox_HomeWorkIds;
    private JComboBox<String> cbox_ScoreSelectionMethod;
}
