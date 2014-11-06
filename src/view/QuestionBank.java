package view;

import beans.LoggedInUser;
import database.DataBaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * @author Vikas Piddempally
 */
public class QuestionBank implements gradience.GradienceAppConstants {

    DataBaseConnection db;

    public QuestionBank(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getQuestionBankPanel() {
        panel_QuestionBank = new JPanel();
        panel_NorthOptions = new JPanel();
        lbl_DifficultyLevel = new JLabel();
        cbox_DifficultyLevel = new JComboBox();
        lbl_SelectCourse = new JLabel();
        cbox_CourseNameList = new JComboBox();
        lbl_TopicsByCourse = new JLabel();
        cbox_TopicsByCourse = new JComboBox();
        panel_QuestionsList = new JPanel();
        splitPane_Tables = new JSplitPane();
        scrollPane_QuestionsTable = new JScrollPane();
        scrollPane_AnswersTable = new JScrollPane();

        //======== panel_QuestionBank ========
        {
            panel_QuestionBank.setLayout(new BorderLayout(5, 5));

            //======== panel_NorthOptions ========
            {
                panel_NorthOptions.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- lbl_DifficultyLevel ----
                lbl_DifficultyLevel.setText("Difficulty Level:");
                panel_NorthOptions.add(lbl_DifficultyLevel);

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

                panel_NorthOptions.add(cbox_DifficultyLevel);

                //---- lbl_SelectCourse ----
                lbl_SelectCourse.setText("Select Course:");
                panel_NorthOptions.add(lbl_SelectCourse);
                if(LoggedInUser.role == ROLE_INSTRUCTOR){
                cbox_CourseNameList = new JComboBox(db.getCourseNameList());
                }
                else{
                    cbox_CourseNameList = new JComboBox(db.getCourseNameListForTA(LoggedInUser.studentId));
                }
                panel_NorthOptions.add(cbox_CourseNameList);

                cbox_CourseNameList.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseNameList.getSelectedItem().toString());
                        cbox_TopicsByCourse.setModel(new DefaultComboBoxModel(db.getTopicDataByCourseId(selectedCourseID)));
                    }
                });

                //---- lbl_TopicsByCourse ----
                lbl_TopicsByCourse.setText("Topics By Selected Course");
                int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseNameList.getSelectedItem().toString());
                cbox_TopicsByCourse = new JComboBox(db.getTopicDataByCourseId(selectedCourseID));
                cbox_TopicsByCourse.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        
                        
                        //Update the Questions Name Lists
                        int topicID = db.getTopicIDByTopicName(cbox_TopicsByCourse.getSelectedItem().toString());
                        //Update Scroll View Data
                        DefaultTableModel tm = new DefaultTableModel();
                        if ((cbox_TopicsByCourse.getSelectedIndex() != 0)) {
                            db.setQuestionsTableByTopicID(table_Questions, vector_QuestionHeaders, topicID);
                        } else {
                            return;
                        }
                        
                        
                    }
                });

                panel_NorthOptions.add(lbl_TopicsByCourse);
                panel_NorthOptions.add(cbox_TopicsByCourse);
            }
            panel_QuestionBank.add(panel_NorthOptions, BorderLayout.NORTH);

            //======== panel_QuestionsList ========
            {
                panel_QuestionsList.setLayout(new BorderLayout(5, 5));

                //======== splitPane_Tables ========
                {
                    splitPane_Tables.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    splitPane_Tables.setDividerLocation(300);

                    vector_QuestionHeaders = new Vector<>();

                    vector_QuestionHeaders.add("Select");
                    vector_QuestionHeaders.add("Question ID");
                    vector_QuestionHeaders.add("Question");
                    table_Questions = new JXTable();
                    table_Questions.setVisibleRowCount(30);
                    table_Questions.setModel(new DefaultTableModel(null, vector_QuestionHeaders));
                    if (LoggedInUser.role == ROLE_INSTRUCTOR) {
                        db.setQuestionsTable(table_Questions, vector_QuestionHeaders);
                    }
                    else if(LoggedInUser.role == ROLE_TA){
                         db.setQuestionsTable(table_Questions, vector_QuestionHeaders);
                    }
                    
                    table_Questions.setShowGrid(true);
                    table_Questions.getTableHeader().setReorderingAllowed(false);
                    table_Questions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table_Questions.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
                    table_Questions.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));

                    scrollPane_QuestionsTable.setViewportView(table_Questions);

                    splitPane_Tables.setTopComponent(scrollPane_QuestionsTable);

                    setListSelectionListener(table_Questions);
                    vector_AnswerHeaders = new Vector<String>();
                    vector_AnswerHeaders.add("ID");
                    vector_AnswerHeaders.add("Content");
                    vector_AnswerHeaders.add("Explanation");
                    vector_AnswerHeaders.add("Correctness Flag");

                    table_Answers = new JXTable();
                    table_Answers.setVisibleRowCount(30);
                    table_Answers.setModel(new DefaultTableModel(null, vector_AnswerHeaders));
                    table_Answers.setShowGrid(true);
                    table_Answers.getTableHeader().setReorderingAllowed(false);
                    table_Answers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table_Answers.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
                    table_Answers.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));
                    scrollPane_AnswersTable.setViewportView(table_Answers);

                    splitPane_Tables.setBottomComponent(scrollPane_AnswersTable);
                }
                panel_QuestionsList.add(splitPane_Tables, BorderLayout.CENTER);
            }
            panel_QuestionBank.add(panel_QuestionsList, BorderLayout.CENTER);
        }
        return panel_QuestionBank;
    }

    private JPanel panel_QuestionBank;
    private JPanel panel_NorthOptions;
    private JLabel lbl_DifficultyLevel;
    private JComboBox cbox_DifficultyLevel;
    private JLabel lbl_SelectCourse;
    private JComboBox cbox_CourseNameList;
    private JLabel lbl_TopicsByCourse;
    private JComboBox cbox_TopicsByCourse;
    private JPanel panel_QuestionsList;
    private JSplitPane splitPane_Tables;
    private JScrollPane scrollPane_QuestionsTable;
    private JXTable table_Questions;
    private Vector<String> vector_QuestionHeaders;
    private JXTable table_Answers;
    private Vector<String> vector_AnswerHeaders;

    private JScrollPane scrollPane_AnswersTable;

    public void setListSelectionListener(final JXTable table_Questions) {
        table_Questions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boolean ALLOW_ROW_SELECTION = table_Questions.getRowSelectionAllowed();
        if (ALLOW_ROW_SELECTION) { // true by default
            ListSelectionModel rowSM = table_Questions.getSelectionModel();
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
                        int questionId = Integer.parseInt(table_Questions.getValueAt(table_Questions.getSelectedRow(), 1).toString());
                        db.setAnswersTable(table_Answers, vector_AnswerHeaders, questionId);

                    }
                }
            });
        } else {
            table_Questions.setRowSelectionAllowed(false);
        }
    }

}
