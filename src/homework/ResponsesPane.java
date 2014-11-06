package homework;

import gradience.GradienceAppConstants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import structures.Homework;
import beans.Answer;
import beans.LoggedInUser;
import beans.Question;
import database.DataBaseConnection;
import database.HomeworkService;

public class ResponsesPane extends JPanel implements GradienceAppConstants {

    JLabel jLabel;
    private static final long serialVersionUID = 1L;

    public ResponsesPane(List<Question> questions) {
        int count = 0;
        List<Answer> answers;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        DataBaseConnection dc = new DataBaseConnection(DB_DRIVER, DB_URL, DB_USERNAME, DB_PASSWORD);
        dc.makeDatabaseConnection();
        int total_score = 0;
        int i = 0;
        int answersArray[] = new int[questions.size()];
        for (Question q : questions) {
            add(new ResponseAnswerPane(q, count), gbc);
            total_score = total_score + q.getScore();
            answersArray[i] = q.getSelectedAnswer();
            i++;
        }
        HomeworkService homeworkService = new HomeworkService();
        Homework homework = homeworkService.getHomeworkByAttemptId(LoggedInUser.attemptId);//change attempt id
        //	List<Answer> answers = questions.get
        homeworkService.submitHomework(answersArray, total_score * homework.getMarksPerQuestion() - ((questions.size() - total_score) * homework.getPenaltyPerQuestion()), homework.getMarksPerQuestion() * questions.size(), LoggedInUser.attemptId);
        int total_score_points = (total_score * homework.getMarksPerQuestion() - ((questions.size() - total_score) * homework.getPenaltyPerQuestion()));
        jLabel = new JLabel("Total Score: " + Integer.toString(total_score_points > 0 ? total_score_points : 0));
        gbc.weighty = 1;
        add(new JPanel(), gbc);
        add(jLabel, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton submit = new JButton("Try Again");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
//        add(submit, gbc);
    }

}
