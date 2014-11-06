package homework;

import gradience.GradienceAppConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import beans.Question;
import database.DataBaseConnection;
import database.HomeworkService;

public class FetchPastDeadlineHomeWorks  implements GradienceAppConstants {

    private int homeWorkId;

    public FetchPastDeadlineHomeWorks(int homeworkId) {
        this.homeWorkId = homeworkId;
    }

    public JPanel getPastHomeWorksPanel() {
        panel_Container= new JPanel();
        panel_Container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        List<Question> questions = null;
        HomeworkService homeworkService = new HomeworkService();
        try {
            questions = homeworkService.getQuestionsByHomeworkId(homeWorkId, 0);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        for (Question q : questions) {
             panel_Container.add(new PastResponseAnswerPane(q), gbc);
        }
        gbc.weighty = 1;
         panel_Container.add(new JPanel(), gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submit = new JButton("Try Again");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
//         panel_Container.add(submit, gbc);
         return panel_Container;
    }
    
    private JPanel panel_Container;
}
