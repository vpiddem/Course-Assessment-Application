package view;

import database.DataBaseConnection;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
public class CoursesTab {

    DataBaseConnection db;

    public CoursesTab(DataBaseConnection db) {
        this.db = db;
    }

    public JPanel initComponents() {
        panel_Courses = new JPanel();
        splitPane_CoursesTab = new JSplitPane();
        panel_LeftTables = new JPanel();
        scrollPane_CoursesTable = new JScrollPane();
        scrollPane_TextBooks = new JScrollPane();
        //======== panel_Courses ========
        {

            panel_Courses.setLayout(new BorderLayout(5, 5));

            //======== splitPane_CoursesTab ========
            {
                splitPane_CoursesTab.setDividerLocation(400);

                //======== panel_LeftTables ========
                {
                    panel_LeftTables.setLayout(new GridLayout(2, 1, 5, 5));

                    scrollPane_CoursesTable.setViewportView(table_Courses);

                    //<editor-fold defaultstate="collapsed" desc="table_Courses Properties">
                    table_Courses = new JXTable();
                    vector_CourseHeaders = new Vector<>();

                    //Adding Column Labels - Table Header Values
                    vector_CourseHeaders.add("Select");
                    vector_CourseHeaders.add("Course ID");
                    vector_CourseHeaders.add("Course Name");
                    vector_CourseHeaders.add("Start Date");
                    vector_CourseHeaders.add("End Date");
                    vector_CourseHeaders.add("Enrollment Count");
                    vector_CourseHeaders.add("Level");
                    vector_CourseHeaders.add("Max Enrollment");
                    model_Courses = new DefaultTableModel(vector_CourseHeaders, 0) {

                        boolean[] cellEditable = {false, false, false, false};

                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return cellEditable[column];
                        }
                    };
                    table_Courses = new JXTable(model_Courses);
                    table_Courses.setShowGrid(true);
                    table_Courses.getTableHeader().setReorderingAllowed(false);
                    table_Courses.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
                    table_Courses.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));
                    table_Courses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    table_Courses.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                        }
                    });
                    scrollPane_CoursesTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    new GradienceBorder().setBorder(scrollPane_CoursesTable, "Courses", 12, 0);

                    db.setCoursesTable(table_Courses, vector_CourseHeaders);
                    scrollPane_CoursesTable.setViewportView(table_Courses);
                    setListSelectionListener_CourseTable(table_Courses);
                    //</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="table_TextBooks Properties">
                    table_TextBooks = new JXTable();
                    vector_TextBookHeaders = new Vector<>();

                    //Adding Column Labels - Table Header Values
                    vector_TextBookHeaders.add("Title");
                    vector_TextBookHeaders.add("Author");
                    vector_TextBookHeaders.add("ISBN Number");

                    model_Courses = new DefaultTableModel(vector_TextBookHeaders, 0) {

                        boolean[] cellEditable = {false, false, false, false};

                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return cellEditable[column];
                        }
                    };
                    table_TextBooks = new JXTable(model_TextBooks);
                    table_TextBooks.setShowGrid(true);
                    table_TextBooks.getTableHeader().setReorderingAllowed(false);
                    table_TextBooks.addHighlighter(HighlighterFactory.createAlternateStriping(Color.WHITE, HighlighterFactory.GENERIC_GRAY));
                    table_TextBooks.addHighlighter(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.DARK_GRAY, Color.WHITE));
                    table_TextBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    scrollPane_TextBooks.setViewportView(table_TextBooks);
                    scrollPane_TextBooks.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    new GradienceBorder().setBorder(scrollPane_TextBooks, "Text Books", 12, 0);

                    setListSelectionListener_CourseTable(table_Courses);
                    //</editor-fold>

                    panel_LeftTables.add(scrollPane_CoursesTable);
                    panel_LeftTables.add(scrollPane_TextBooks);
                }
                splitPane_CoursesTab.setLeftComponent(panel_LeftTables);

                //panel_RightContent
                {

                }

            }
            panel_Courses.add(panel_LeftTables, BorderLayout.CENTER);
        }
        // End of component initialization  
        return panel_Courses;
    }

    public void setListSelectionListener_CourseTable(JXTable table_CourseDetails) {
        table_Courses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boolean ALLOW_ROW_SELECTION = table_Courses.getRowSelectionAllowed();
        if (ALLOW_ROW_SELECTION) { // true by default
            ListSelectionModel rowSM = table_Courses.getSelectionModel();
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
                        TableModel model_Courses = table_Courses.getModel();
                        String courseID = model_Courses.getValueAt(selectedRow, 1).toString();
                        selectedCourseId = Integer.parseInt(courseID);
                        System.out.println(" " + courseID);
                        try {
                            panel_Courses.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            db.setTextBooksTable(table_TextBooks, vector_TextBookHeaders, selectedCourseId);
                            scrollPane_TextBooks.setViewportView(table_TextBooks);
                        } catch (Exception ex) {
                            System.out.println("@In Course View List Selection Listener");
                            ex.printStackTrace();
                        } finally {
                            panel_LeftTables.setCursor(Cursor.getDefaultCursor());
                        }
                    }
                }
            });
        } else {
            table_Courses.setRowSelectionAllowed(false);
        }
    }

    private JPanel panel_Courses;
    private JSplitPane splitPane_CoursesTab;
    private JXTable table_Courses;
    private Vector<String> vector_CourseHeaders, vector_TextBookHeaders;
    private DefaultTableModel model_Courses, model_TextBooks;
    private JXTable table_TextBooks;
    private JPanel panel_LeftTables;
    private JScrollPane scrollPane_CoursesTable;
    private JScrollPane scrollPane_TextBooks;
    private int selectedCourseId;
}
