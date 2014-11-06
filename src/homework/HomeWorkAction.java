package homework;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class HomeWorkAction extends AbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final beans.Question question;
    private final int answer;

    public HomeWorkAction(beans.Question question2, int selectedAnswer2) {
        this.question = question2;
        this.answer = selectedAnswer2;
    }

    public int getAnswer() {
        return answer;
    }

    public beans.Question getQuestion() {
        return question;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        question.setSelectedAnswer(answer);;
    }

}