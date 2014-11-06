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
import database.HomeworkService;

public class FetchHomeWork implements GradienceAppConstants {

    private static final long serialVersionUID = 1L;
    private JPanel panel_Container;

    public JPanel getHomeWorkPanelByAttemptID(int attemptID)
    {
        panel_Container = new JPanel();
        panel_Container.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        List<Question> questions = null;
        int score = 0;
        HomeworkService homeworkService = new HomeworkService();
        try {
            questions = homeworkService.getQuestionsByAttemptId(attemptID);
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }//homeworkService.getQuestionsByAttemptId(5);//homeworkService.getQuestionsByHomeworkId(1).get(0);
        for (Question q : questions) {
            panel_Container.add(new ResponseAnswerPane(q, score), gbc);
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
//        panel_Container.add(submit, gbc);
        return panel_Container;
    }
    
}
