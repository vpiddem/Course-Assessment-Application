import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Wed Sep 17 07:39:06 EDT 2014
 */

/**
 * @author Vikas Piddempally
 */
public class FilterPanel {

    private void createNorthFilterPanel() {

        panel_Filters = new JPanel();
        panel_NorthFilters = new JPanel();
        lbl_DifficultyLevel = new JLabel();
        cbox_DifficultyLevel = new JComboBox<>();
        panel1 = new JPanel();
        btn_ApplyConfiguration = new JButton();
        btn_ShowAll = new JButton();
        panel_ConfigParameters = new JPanel();
        lbl_ExerciseName = new JLabel();
        textField1 = new JTextField();
        lbl_StartDate = new JLabel();
        spinner_StartDate = new JSpinner();
        lbl_CorrectPoints = new JLabel();
        txt_CorrectAnswerPoints = new JTextField();
        lbl_RandomizationSeed = new JLabel();
        textField2 = new JTextField();
        lbl_MaxRetries = new JLabel();
        txt_MaxRetries = new JTextField();
        lbl_EndDate = new JLabel();
        spinner_EndDate = new JSpinner();
        lbl_PenaltyPoints = new JLabel();
        txt_WrongAnswerPenalty = new JTextField();
        label1 = new JLabel();
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
                cbox_DifficultyLevel.setModel(new DefaultComboBoxModel<>(new String[]{
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6"
                }));
                panel_NorthFilters.add(cbox_DifficultyLevel);
            }
            panel_Filters.add(panel_NorthFilters, BorderLayout.NORTH);

            //======== panel1 ========
            {
                panel1.setBorder(new LineBorder(Color.darkGray));
                panel1.setLayout(new FlowLayout());

                //---- btn_ApplyConfiguration ----
                btn_ApplyConfiguration.setText("Apply Configuration");
                panel1.add(btn_ApplyConfiguration);

                //---- btn_ShowAll ----
                btn_ShowAll.setText("Show All");
                panel1.add(btn_ShowAll);
            }
            panel_Filters.add(panel1, BorderLayout.SOUTH);

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
                panel_ConfigParameters.add(textField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- lbl_StartDate ----
                lbl_StartDate.setText("Start Date");
                panel_ConfigParameters.add(lbl_StartDate, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
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
                panel_ConfigParameters.add(textField2, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
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
                label1.setText("Score Selection Method");
                panel_ConfigParameters.add(label1, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
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
  
    private JPanel panel_Filters;
    private JPanel panel_NorthFilters;
    private JLabel lbl_DifficultyLevel;
    private JComboBox<String> cbox_DifficultyLevel;
    private JPanel panel1;
    private JButton btn_ApplyConfiguration;
    private JButton btn_ShowAll;
    private JPanel panel_ConfigParameters;
    private JLabel lbl_ExerciseName;
    private JTextField textField1;
    private JLabel lbl_StartDate;
    private JSpinner spinner_StartDate;
    private JLabel lbl_CorrectPoints;
    private JTextField txt_CorrectAnswerPoints;
    private JLabel lbl_RandomizationSeed;
    private JTextField textField2;
    private JLabel lbl_MaxRetries;
    private JTextField txt_MaxRetries;
    private JLabel lbl_EndDate;
    private JSpinner spinner_EndDate;
    private JLabel lbl_PenaltyPoints;
    private JTextField txt_WrongAnswerPenalty;
    private JLabel label1;
    private JComboBox<String> cbox_ScoreSelectionMethod;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
