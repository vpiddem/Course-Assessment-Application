package homework;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import beans.Answer;
import beans.Question;

public class QuestionAnswerPane extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel jLabel ;
	public QuestionAnswerPane(Question questions) {
        List<Answer> answers = questions.getAnswers();
//        answers.add(questions.getAnswers().get(1));
        setBorder(new TitledBorder(questions.getContent()));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ButtonGroup bg = new ButtonGroup();
        for (beans.Answer answer : answers) {
            JRadioButton rb = new JRadioButton(new AnswerAction(questions, answer));
            bg.add(rb);
            add(rb, gbc);
        }
        jLabel = new JLabel("Response");
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(1, 8));
        add(panel, gbc);
        add(jLabel, gbc);
        gbc.weighty = 1;
    }

}