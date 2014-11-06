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
import database.DataBaseConnection;
import database.HomeworkService;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class AssignmentPane extends JPanel implements GradienceAppConstants {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JLabel label;

    public AssignmentPane(int courseID) {
        final HomeworkService homeworkService = new HomeworkService();
        final List<Homework> homeworks = homeworkService.getHomeworksByCourseId(courseID);
        setBorder(new TitledBorder("Home Works"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ButtonGroup bg = new ButtonGroup();

        cbox_HomeWorkID = new JComboBox();
        for (Homework homework : homeworks) {
            System.out.println(homework.getHomeworkName());
            JRadioButton rb = new JRadioButton(Integer.toString(homework.getHomeworkId()));
            cbox_HomeWorkID.addItem(homework.getHomeworkId() + "");
        }
        add(cbox_HomeWorkID);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 8));
        add(panel, gbc);
        gbc.weighty = 1;
        JButton submit = new JButton("Submit Homework");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Homework h = null;
                for (Homework hm : homeworks) {

                    if (hm.getHomeworkId() == Integer.parseInt(cbox_HomeWorkID.getSelectedItem().toString())) {
                        h = hm;
                        break;

                    } else {
                        continue;
                    }

                }
                int noOfRetries = h.getNoOfRetries();
                int noOfattempts = homeworkService.getNumberOfAttemptsByHomeworkId(h.getHomeworkId());
                if (noOfattempts >= noOfRetries) {
                    JOptionPane.showMessageDialog(cbox_HomeWorkID, "Attempts Exceeded", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                new QuestionAnswer(Integer.parseInt(cbox_HomeWorkID.getSelectedItem().toString()));
            }
        });
        add(submit, gbc);
    }
    JComboBox cbox_HomeWorkID;
}
