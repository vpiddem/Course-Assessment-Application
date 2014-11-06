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

import structures.Homework;
import beans.Course;
import database.DataBaseConnection;
import database.HomeworkService;

public class PreviousSubmissionsList  extends JPanel  implements GradienceAppConstants{
	private static final long serialVersionUID = 1L;
	JLabel label;
	public PreviousSubmissionsList(int student_id) {
        DataBaseConnection dc = new DataBaseConnection(DB_DRIVER, DB_URL, DB_USERNAME, DB_PASSWORD);
		dc.makeDatabaseConnection();
		HomeworkService homeworkService= new HomeworkService();
		List<Homework> homeworks = homeworkService.getHomeworksByCourseId(3);
        setBorder(new TitledBorder("Courses"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ButtonGroup bg = new ButtonGroup();
        for (Homework homework : homeworks) {
        	JRadioButton rb = new JRadioButton(Integer.toString(homework.getHomeworkId()));
            bg.add(rb);
            add(rb, gbc);
        }
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(1, 8));
        add(panel, gbc);
        gbc.weighty = 1;
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
            	new FetchHomeWorkFrame(0);
            }
        });
        add(submit, gbc);
    }
}