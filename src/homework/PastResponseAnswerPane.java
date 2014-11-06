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

public class PastResponseAnswerPane extends JPanel {

	private static final long serialVersionUID = 1L;
	public PastResponseAnswerPane(Question questions) {
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
            if (answer.isCorrect()){
        	JRadioButton rb = new JRadioButton(answer.getContent());           
        	bg.add(rb);
            rb.setSelected(true);
            rb.setEnabled(false);
            add(rb, gbc);

        }
            else{
            	JRadioButton rb = new JRadioButton(answer.getContent());
                bg.add(rb);
                rb.setEnabled(false);
                add(rb, gbc);

            }
       
           
        }
        
        add(new JLabel("Explanation :\n "+questions.getExplanation()));
        
        
        JPanel panel=new JPanel();

		panel.setLayout(new GridLayout(1, 8));
        add(panel, gbc);
        gbc.weighty = 1;
    }
	
}