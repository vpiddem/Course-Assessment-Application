package view;

import database.DataBaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * @author Vikas Piddempally
 */
public class CourseRoll {

    DataBaseConnection db;

    public CourseRoll(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel getClassRollPanel() {
        panel_ClassRollContainer = new JPanel();
        panel_ClassRollContainer.setLayout(new BorderLayout(5, 5));
        panel_North = new JPanel();
        lbl_SelectCourse = new JLabel();
        cbox_CourseNames = new JComboBox(db.getCourseNameList());
        panel_ClassRollTable = new JPanel();
        //======== panel_North ========
        {
            panel_North.setBackground(new Color(221, 240, 230));
            panel_North.setLayout(new FlowLayout(5));
            //---- lbl_SelectCourse ----
            lbl_SelectCourse.setText("Select Course :");
            panel_North.add(lbl_SelectCourse);
            panel_North.add(cbox_CourseNames);
        }

        //======== panel_ClassRollTable ========
        {
            panel_ClassRollTable.setLayout(new BorderLayout(5, 5));

            scrollPane_CourseEnrollment = new JScrollPane();
            table_CourseRoll = new JXTable();

            //<editor-fold defaultstate="collapsed" desc="table_CourseRoll Properties">
            table_CourseRoll = new JXTable();
            vector_CourseRollHeaders = new Vector<>();

            //Adding Column Labels - Table Header Values
            vector_CourseRollHeaders.add("Student ID");
            vector_CourseRollHeaders.add("Name");
            vector_CourseRollHeaders.add("Level");
            model_CourseRoll = new DefaultTableModel(vector_CourseRollHeaders, 0) {

                boolean[] cellEditable = {false, false, false, false};

                @Override
                public boolean isCellEditable(int row, int column) {
                    return cellEditable[column];
                }
            };
            table_CourseRoll = new JXTable(model_CourseRoll);
            table_CourseRoll.setShowGrid(true);
            table_CourseRoll.getTableHeader().setReorderingAllowed(false);
            table_CourseRoll.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
            table_CourseRoll.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));
            table_CourseRoll.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table_CourseRoll.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {

                }
            });
            
            scrollPane_CourseEnrollment.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            new GradienceBorder().setBorder(scrollPane_CourseEnrollment, "Course Enrollment", 12, 0);

            
         
             cbox_CourseNames.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        int selectedCourseID = db.getCourseIdByCourseName(cbox_CourseNames.getSelectedItem().toString());
                        db.setCourseRollTable(table_CourseRoll, vector_CourseRollHeaders, selectedCourseID);
                        scrollPane_CourseEnrollment.setViewportView(table_CourseRoll);
                    }
                });
            db.setCourseRollTable(table_CourseRoll, vector_CourseRollHeaders,0);
            scrollPane_CourseEnrollment.setViewportView(table_CourseRoll);
            //</editor-fold>
        }

        scrollPane_CourseEnrollment.setViewportView(table_CourseRoll);
        panel_ClassRollTable.add(scrollPane_CourseEnrollment);
        panel_ClassRollContainer.add(panel_North, BorderLayout.NORTH);
        panel_ClassRollContainer.add(panel_ClassRollTable, BorderLayout.CENTER);

        return panel_ClassRollContainer;
    }

    private JPanel panel_ClassRollContainer;
    private JScrollPane scrollPane_CourseEnrollment;
    private JXTable table_CourseRoll;
    private Vector<String> vector_CourseRollHeaders;
    private DefaultTableModel model_CourseRoll;
    private JPanel panel_North;
    private JLabel lbl_SelectCourse;
    private JComboBox cbox_CourseNames;
    private JPanel panel_ClassRollTable;
}
