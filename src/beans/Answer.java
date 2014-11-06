package beans;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Answer {
	
	private int id;
	
	private int questionId;
	
	private String content;
	
	private String explanation;
	
	private boolean isCorrect;
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	public String toString(){
		return ReflectionToStringBuilder.toString(this)+"\n\n";
	}

}
