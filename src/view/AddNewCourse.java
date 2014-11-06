package view;

import beans.LoggedInUser;
import database.DataBaseConnection;
import database.HomeworkService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class AddNewCourse {

    DataBaseConnection db;

    public AddNewCourse(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getAddNewCoursePanel() {
        panel_Container = new JPanel();
        panel_AddCourse = new JPanel();
        lbl_CourseToken = new JLabel();
        txt_CourseToken = new JTextField();
        btn_Enroll = new JButton();
        //======== panel_Container ========
        {
            panel_Container.setLayout(new BorderLayout(5, 5));

            //======== panel_AddCourse ========
            {
                panel_AddCourse.setBorder(new TitledBorder(null, "Add Course", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, new Color(0, 133, 243)));
                panel_AddCourse.setLayout(new GridBagLayout());
                ((GridBagLayout) panel_AddCourse.getLayout()).columnWidths = new int[]{0, 115, 0};
                ((GridBagLayout) panel_AddCourse.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
                ((GridBagLayout) panel_AddCourse.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                lbl_CourseToken.setText("Enter Course Token:");
                panel_AddCourse.add(lbl_CourseToken, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 6, 6), 0, 0));
                panel_AddCourse.add(txt_CourseToken, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 6, 6), 0, 0));

                //---- btn_Enroll ----
                btn_Enroll.setText("Enroll");
                btn_Enroll.addActionListener(new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        action_btnEnrollClicked();
                    }

                });
                panel_AddCourse.add(btn_Enroll, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 6, 6), 0, 0));
            }
            //panel_Center
            {
                panel_Center = new JPanel();
                panel_Center.setLayout(new GridLayout(1, 1));
                scroll_CoursesEnrolled = new JScrollPane();
                
                    //<editor-fold defaultstate="collapsed" desc="table_CoursesEnrolled Properties">
                    table_CoursesEnrolled = new JXTable();
                vector_CourseHeaders = new Vector<>();
                    //Adding Column Labels - Table Header Values
                    vector_CourseHeaders.add("Course ID");
                    vector_CourseHeaders.add("Course Name");
//                    vector_CourseHeaders.add("Start Date");
//                    vector_CourseHeaders.add("End Date");
//                    vector_CourseHeaders.add("Enrollment Count");
//                    vector_CourseHeaders.add("Level");
//                    vector_CourseHeaders.add("Max Enrollment");
                    model_Courses = new DefaultTableModel(vector_CourseHeaders, 0) {

                        boolean[] cellEditable = {false, false, false, false};

                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return cellEditable[column];
                        }
                    };
                    table_CoursesEnrolled = new JXTable(model_Courses);
                    table_CoursesEnrolled.setShowGrid(true);
                    table_CoursesEnrolled.getTableHeader().setReorderingAllowed(false);
                    table_CoursesEnrolled.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
                    table_CoursesEnrolled.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));
                    table_CoursesEnrolled.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scroll_CoursesEnrolled.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    new GradienceBorder().setBorder(scroll_CoursesEnrolled, "Courses", 12, 0);
                    db.setCoursesTableByStudentID(table_CoursesEnrolled, vector_CourseHeaders,LoggedInUser.studentId);
                    scroll_CoursesEnrolled.setViewportView(table_CoursesEnrolled);
                    //</editor-fold>
                    
                    panel_Center.add(scroll_CoursesEnrolled);

            }
            
            //panel_South
            {
            JButton btn_Refresh = new JButton();
            btn_Refresh.setText("Refresh");
            btn_Refresh.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                   db.setCoursesTableByStudentID(table_CoursesEnrolled, vector_CourseHeaders, LoggedInUser.studentId);
                   scroll_CoursesEnrolled.setViewportView(table_CoursesEnrolled);
                }
            });
             panel_South = new JPanel();
            panel_South.add(btn_Refresh);
        }
            
            panel_Container.add(panel_AddCourse, BorderLayout.NORTH);
            panel_Container.add(panel_Center, BorderLayout.CENTER);
            panel_Container.add(panel_South, BorderLayout.SOUTH);
        }
        return panel_Container;
    }

    private void action_btnEnrollClicked() {
        HomeworkService homeWorkService = new HomeworkService();
        boolean returnValue = homeWorkService.enrollCourse(txt_CourseToken.getText(), LoggedInUser.studentId);

        if (!returnValue) {
            JOptionPane.showMessageDialog(panel_AddCourse, "Please Enter Correct Course Token Value\n", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(panel_AddCourse, "Successfully Enrolled", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private JPanel panel_Container;
    private JPanel panel_Center;
    private JPanel panel_South;
    private JScrollPane scroll_CoursesEnrolled;
    private Vector<String> vector_CourseHeaders;
    private DefaultTableModel model_Courses;
    private JXTable table_CoursesEnrolled;
    private JPanel panel_AddCourse;
    private JLabel lbl_CourseToken;
    private JTextField txt_CourseToken;
    private JButton btn_Enroll;
}
