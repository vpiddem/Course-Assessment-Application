/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structures;

/**
 *
 * @author Vikas
 */
public class Homework {
    private String homeworkName;
    private int difficultyLevel;
    private int randomizationSeed;
    private int homeworkId;
    private String startTime;
    private String endTime;
    private int marksPerQuestion;
    private int penaltyPerQuestion;
    private int topic;
    private int noOfRetries;
    private int courseID;
    private int scoreSelectionMethod;

    /**
     * @return the homeworkName
     */
    public String getHomeworkName() {
        return homeworkName;
    }

    /**
     * @param homeworkName the homeworkName to set
     */
    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    /**
     * @return the difficultyLevel
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * @param difficultyLevel the difficultyLevel to set
     */
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * @return the randomizationSeed
     */
    public int getRandomizationSeed() {
        return randomizationSeed;
    }

    /**
     * @param randomizationSeed the randomizationSeed to set
     */
    public void setRandomizationSeed(int randomizationSeed) {
        this.randomizationSeed = randomizationSeed;
    }

    /**
     * @return the homeworkId
     */
    public int getHomeworkId() {
        return homeworkId;
    }

    /**
     * @param homeworkId the homeworkId to set
     */
    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the marksPerQuestion
     */
    public int getMarksPerQuestion() {
        return marksPerQuestion;
    }

    /**
     * @param marksPerQuestion the marksPerQuestion to set
     */
    public void setMarksPerQuestion(int marksPerQuestion) {
        this.marksPerQuestion = marksPerQuestion;
    }

    /**
     * @return the penaltyPerQuestion
     */
    public int getPenaltyPerQuestion() {
        return penaltyPerQuestion;
    }

    /**
     * @param penaltyPerQuestion the penaltyPerQuestion to set
     */
    public void setPenaltyPerQuestion(int penaltyPerQuestion) {
        this.penaltyPerQuestion = penaltyPerQuestion;
    }

    /**
     * @return the topic
     */
    public int getTopic() {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(int topic) {
        this.topic = topic;
    }

    /**
     * @return the noOfRetries
     */
    public int getNoOfRetries() {
        return noOfRetries;
    }

    /**
     * @param noOfRetries the noOfRetries to set
     */
    public void setNoOfRetries(int noOfRetries) {
        this.noOfRetries = noOfRetries;
    }

    /**
     * @return the scoreSelectionMethod
     */
    public int getScoreSelectionMethod() {
        return scoreSelectionMethod;
    }

    /**
     * @param scoreSelectionMethod the scoreSelectionMethod to set
     */
    public void setScoreSelectionMethod(int scoreSelectionMethod) {
        this.scoreSelectionMethod = scoreSelectionMethod;
    }

    public void setCourseID(int courseID){
        this.courseID = courseID;
    }
    
    public int getCourseID() {
        return courseID;
    }
    
    
}
