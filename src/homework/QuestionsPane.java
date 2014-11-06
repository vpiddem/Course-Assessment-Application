package homework;

import gradience.GradienceAppConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import beans.LoggedInUser;
import beans.Question;
import database.DataBaseConnection;
import database.HomeworkService;

public class QuestionsPane extends JPanel implements GradienceAppConstants {

    private static final long serialVersionUID = 1L;

    public QuestionsPane(int homeWorkID) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        final List<beans.Question> questions;// = new ArrayList<>();
        final HomeworkService homeworkService = new HomeworkService();
        questions = homeworkService.getQuestionsByHomeworkId(homeWorkID, LoggedInUser.studentId);
        for (Question q : questions) {
            add(new QuestionAnswerPane(q), gbc);
        }
        gbc.weighty = 1;
        add(new JPanel(), gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submit = new JButton("Submit");
        submit.addActionListener(new ActionListener() {
            int i = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int[] answersArray = new int[questions.size()];
                for (Question q : questions) {
                    answersArray[i] = q.getSelectedAnswer();
                    i++;
                }
                homeworkService.submitHomework(answersArray, 0, 25, LoggedInUser.attemptId);
                new HomeWorkResponses(questions);
            }
        });
        add(submit, gbc);
    }
}
