package homework;

import gradience.GradienceAppConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import beans.Course;
import database.DataBaseConnection;
import database.HomeworkService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class CourseListPane implements GradienceAppConstants {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JLabel label;
    int studentId;
    DataBaseConnection db;

    public CourseListPane(DataBaseConnection db, int studentId) {

        this.db = db;
        this.studentId = studentId;
    }

    public JPanel getCourseListPane() {
        JPanel panel_Container = new JPanel();
        HomeworkService homeworkService = new HomeworkService();
        List<Course> courses = homeworkService.getCoursesByStudentId(studentId);
        panel_Container.setBorder(new TitledBorder("Courses"));
        panel_Container.setLayout(new FlowLayout());
        ButtonGroup bg = new ButtonGroup();

        Vector<String> courseNameList = new Vector<String>();

        cbox_Courses = new JComboBox();
        for (beans.Course course : courses) {
            courseNameList.add(course.getName());
        }

        cbox_Courses = new JComboBox(courseNameList);
        JPanel panel = new JPanel();
        JButton fetchHomeworks = new JButton("Fetch HomeWorks");
        panel.setLayout(new FlowLayout(5));
        panel.add(cbox_Courses);
        panel.add(fetchHomeworks);
        panel_Container.add(panel);

        fetchHomeworks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int courseId = db.getCourseIdByCourseName(cbox_Courses.getSelectedItem().toString());
                System.out.println("*** " + courseId);
                new AssignmentsFrame(courseId);
            }
        });
        panel_Container.add(fetchHomeworks, BorderLayout.SOUTH);
        return panel_Container;
    }

    private JComboBox cbox_Courses;
}
