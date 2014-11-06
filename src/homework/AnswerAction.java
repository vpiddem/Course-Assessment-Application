package homework;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import beans.Answer;


public class AnswerAction extends AbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final beans.Question question;
    private final Answer answer;

    public AnswerAction(beans.Question question2, beans.Answer answer2) {
        this.question = question2;
        this.answer = answer2;
        putValue(NAME, answer2.getContent());
    }

    public Answer getAnswer() {
        return answer;
    }

    public beans.Question getQuestion() {
        return question;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        question.setSelectedAnswer(answer.getId());;

    }

}