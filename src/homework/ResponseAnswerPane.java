package homework;

import java.awt.Color;
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

public class ResponseAnswerPane extends JPanel {

	private static final long serialVersionUID = 1L;
	JLabel jLabel ;
	public ResponseAnswerPane(Question questions, int count) {
        List<Answer> answers = questions.getAnswers();
        setBorder(new TitledBorder(questions.getContent()));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ButtonGroup bg = new ButtonGroup();
        boolean isAnswered = false;
        for (beans.Answer answer : answers) {
            if (questions.getSelectedAnswer()==answer.getId()){
        	JRadioButton rb = new JRadioButton(new AnswerAction(questions, answer));           
        	bg.add(rb);
            rb.setSelected(true);
            rb.setEnabled(false);
            add(rb, gbc);

        }
            else{
            	JRadioButton rb = new JRadioButton(new AnswerAction(questions, answer));
                bg.add(rb);
                rb.setEnabled(false);
                add(rb, gbc);

            }
            if(questions.getSelectedAnswer()==answer.getId()&&answer.isCorrect()){
            	questions.setScore(1);
            	isAnswered = true;
            	
            }
           
        }
        
        JPanel panel=new JPanel();
		if(isAnswered){
            jLabel = new JLabel("RightAnswer"); 
            jLabel.setForeground(Color.GREEN);

		}else{
			jLabel = new JLabel("Wrong answer Hint : "+ questions.getHint());
            jLabel.setForeground(Color.RED);

		}
		panel.setLayout(new GridLayout(1, 8));
        add(panel, gbc);
        add(jLabel, gbc);
        gbc.weighty = 1;
    }
	
}