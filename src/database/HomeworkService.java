package database;

import gradience.GradienceAppConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import beans.Answer;
import beans.Attempt;
import beans.Course;
import beans.LoggedInUser;
import beans.Question;
import beans.Score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import structures.Homework;

public class HomeworkService implements GradienceAppConstants {

    private String FETCH_HOMEWORK_QUESTIONS = "select q.* from questions q,homework_questions hq where "
            + "q.id = hq.question_id and hq.homework_id = ? order by q.id";

    private String FETCH_PAST_HOMEWORK_QUESTIONS = "select q.*,temp.* from (select * from exam_questions where attempt_id = ? )"
            + "as temp,questions q "
            + "where temp.question_id=q.id order by temp.question_index";

    private String FETCH_UNFINISHED_HOMEWORK = "select id from attempts where submission_time is null and homework_id = ? and student_id = ?";

    private String FETCH_RANDOM_ANSWERS = ""
            + "(select * from answers where question_id = ? and correctness_flag = 't' order by random() limit 1)"
            + "union"
            + "(select * from answers where question_id = ? and correctness_flag = 'f' order by random() limit 3)";

    private String FETCH_ANSWER_BY_ID = "select * from answers where id = ?";

    private String INSERT_ATTEMPT = "insert into attempts(student_id, homework_id) values (?,?) returning id";

    public String UPDATE_HOME_WORK_SUBMISSION = "update attempts set submission_time = now(),score_obtained = ?,maximum_score = ? where id = ?";

    public String UPDATE_SELECTED_ANSWER = "update exam_questions set selected_answer = ? where question_index = ? and attempt_id = ?";

    private String INSERT_EXAM_QUESTIONS = "insert into exam_questions(question_id,answer1_id,answer2_id,answer3_id,answer4_id,attempt_id,question_index)"
            + "values (?,?,?,?,?,?,?)";

    private String FETCH_PARAMETERS = "select value from parameters where questionid = ? and setid = ? order by index";

    private String FETCH_HOMEWORK_BY_ATTEMPT_ID = "select * from homeworks where id in (select homework_id from attempts where id= ?)";

    private String FETCH_PAST_SCORE = "select score, maximum_score from scores where homework_id = ? and student_id = ?";

    private String FETCH_AVG_SCORE = "select avg(score_obtained) as avg from attempts where student_Id = ? and homework_id = ?";

    private String UPDATE_SCORE = "update scores set score = ?,maximum_score = ? where student_id = ? and homework_id = ?";

    private String INSERT_SCORE = "insert into scores(score, maximum_score, student_id, homework_id) values (?,?,?,?)";

    private String FETCH_EXPIRING_HOMEWORKS = "select h.name from homeworks h,enrollments e where e.student_id = ? "
            + "and e.course_id = h.course_id and h.end_date < now() + interval '1 day' and h.end_date > now() "
            + "and h.id not in (select distinct(homework_id) from attempts where student_id = e.student_id)";

    private String FETCH_ALERTS = "select * from alerts where professor_id = ?";

    private Connection connection = DataBaseConnection.connection;

    private String FETCH_NO_OF_TRIES = "select count(*) as count from attempts where student_id = ? and homework_id = ?";

    private String FETCH_COURSES = "select c.course_name, c.id from courses c, students s, enrollments e where s.id = ? and s.id = e.student_id "
            + "and e.course_id = c.id";

    private String FETCH_HOMEWORK_BY_COURSE_ID = "select * from homeworks where course_id = ?";

    private String FETCH_ATTEMPTS_BY_HOMEWORK_ID = "select * from attempts where homework_id = ? and student_id = ? order by id";

    private String FETCH_COURSE_ID_BY_TOKEN = "select * from courses where token = ?";

    private String ENROLL_COURSE = "insert into enrollments(course_id,student_id) values(?,?)";

    private String FETCH_SCORES = "select s.*,h.name from scores s,homeworks h where scores.student_id = ? and s.homework_id = h.id";

    private String FETCH_CONFLICTING_COURSES = "select distinct(professor_id),teaching_assistant_id,id from courses where id in "
            + "(select c2.course_id from course_topics c1,course_topics c2 where c1.course_id = ? and "
            + "c1.topic_id = c2.topic_id and c1.course_id<>c2.course_id ) and teaching_assistant_id = ?";

    private String INSERT_ALERT = "INSERT INTO alerts( professor_id, teaching_assistant_id, course_id, conflicting_course_id) VALUES ( ?, ?, ?, ?)";

    /**
     * @param homeworkId
     * @return randomized list of questions with answers to each of them.
     */
    @SuppressWarnings("resource")
    public List<Question> getQuestionsByHomeworkId(int homeworkId, int studentId) {

        List<Question> questions = new ArrayList<Question>();
        PreparedStatement ps = null;
//		LoggedInUser.studentId = studentId;
        try {
            connection.setAutoCommit(false);
            //Checking if an unfinished homework is already present for given student
            ps = connection.prepareStatement(FETCH_UNFINISHED_HOMEWORK);
            ps.setInt(1, homeworkId);
            ps.setInt(2, studentId);

            ResultSet rs = ps.executeQuery();
            int attemptId = 0;
            if (rs.next()) {

                attemptId = rs.getInt("id");

            }
            ps.close();

            if (attemptId > 0) {
                LoggedInUser.attemptId = attemptId;
                System.out.println("got unfinished homework");
                // There is an unfinished homework, populating the questions previously stored in database
                questions = getQuestionsByAttemptId(attemptId);
                return questions;
            } else {
                // Fetch fresh set of random questions from database
                ps = connection.prepareStatement(FETCH_HOMEWORK_QUESTIONS);
                ps.setInt(1, homeworkId);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Question question = new Question();
                    question.setContent(rs.getString("content"));
                    question.setHint(rs.getString("question_hint"));
                    question.setExplanation(rs.getString("explanation"));
                    question.setId(rs.getInt("id"));
                    if (rs.getInt("type") == 2) {
                        //Add missing parameters to the question based on set
                        Random rn = new Random();

                        int setId = rn.nextInt(2) + 1; // Should be random generated

                        System.out.println("Selected set id : " + setId);

                        setParameterizedContent(question, setId);
                    }

                    questions.add(question);

                }

                ps.close();

                for (Question q : questions) {
                    ps = connection.prepareStatement(FETCH_RANDOM_ANSWERS);
                    ps.setInt(1, q.getId());
                    ps.setInt(2, q.getId());

                    rs = ps.executeQuery();
                    List<Answer> answers = new ArrayList<Answer>();
                    while (rs.next()) {
                        Answer a = new Answer();
                        a.setId(rs.getInt("id"));
                        a.setContent(rs.getString("content"));
                        a.setCorrect(rs.getBoolean("correctness_flag"));
                        a.setExplanation(rs.getString("explanation"));
                        a.setQuestionId(rs.getInt("question_id"));
                        answers.add(a);
                    }
                    long seed = System.nanoTime();
                    Collections.shuffle(answers, new Random(seed));
                    q.setAnswers(answers);
                }
                long seed = System.nanoTime();
                Collections.shuffle(questions, new Random(seed));
                if (studentId != 0) {
                    persistHomework(questions, studentId, homeworkId);

                }
                connection.setAutoCommit(true);
                return questions;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public int getNumberOfAttemptsByHomeworkId(int homeworkId) {

        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_NO_OF_TRIES);

            ps.setInt(1, LoggedInUser.studentId);
            ps.setInt(2, homeworkId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    private void setParameterizedContent(Question question, int setId) throws SQLException {

        String questionText = question.getContent();

        PreparedStatement ps = connection.prepareStatement(FETCH_PARAMETERS);

        ps.setInt(1, question.getId());
        ps.setInt(2, setId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            questionText = questionText.replaceFirst("\\?", rs.getString("value"));
        }

        question.setContent(questionText);

    }

    // Method to save the attempt when a student requests for homework.
    private void persistHomework(List<Question> questions, int studentId, int homeworkId) {

        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_ATTEMPT);
            ps.setInt(1, studentId);
            ps.setInt(2, homeworkId);

            ResultSet rs = ps.executeQuery();
            int attemptId = 0;
            while (rs.next()) {
                attemptId = rs.getInt("id");
                LoggedInUser.attemptId = attemptId;
            }
            int index = 0;

            for (Question question : questions) {

                ps = connection.prepareStatement(INSERT_EXAM_QUESTIONS);
                ps.setInt(1, question.getId());
                List<Answer> answers = question.getAnswers();
                ps.setInt(2, answers.get(0).getId());
                ps.setInt(3, answers.get(1).getId());
                ps.setInt(4, answers.get(2).getId());
                ps.setInt(5, answers.get(3).getId());

                ps.setInt(6, attemptId);
                ps.setInt(7, ++index);

                ps.execute();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public List<Question> getQuestionsByAttemptId(int attemptId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(FETCH_PAST_HOMEWORK_QUESTIONS);

        List<Question> questions = new ArrayList<Question>();
        ps.setInt(1, attemptId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Question question = new Question();
            question.setContent(rs.getString("content"));
            question.setHint(rs.getString("question_hint"));
            question.setId(rs.getInt("id"));
            question.addAnswer(getAnswerById(rs.getInt("answer1_id")));
            question.addAnswer(getAnswerById(rs.getInt("answer2_id")));
            question.addAnswer(getAnswerById(rs.getInt("answer3_id")));
            question.addAnswer(getAnswerById(rs.getInt("answer4_id")));
            question.setSelectedAnswer(rs.getInt("selected_answer"));
            questions.add(question);

        }
        return questions;
    }

    private Answer getAnswerById(int answerId) {

        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_ANSWER_BY_ID);
            ps.setInt(1, answerId);

            ResultSet rs = ps.executeQuery();
            Answer answer = null;
            while (rs.next()) {
                answer = new Answer();
                answer.setId(rs.getInt("id"));
                answer.setContent(rs.getString("content"));
                answer.setCorrect(rs.getBoolean("correctness_flag"));
                answer.setExplanation(rs.getString("explanation"));
                answer.setQuestionId(rs.getInt("question_id"));
            }
            ps.close();
            return answer;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public void submitHomework(int[] answers, int score, int maxScore, int attemptId) {
        try {
            connection.setAutoCommit(false);
            boolean update_avg_score = false;
            Homework homework = getHomeworkByAttemptId(attemptId);
            int prevScore = -1;
            prevScore = getPreviousScore(homework.getHomeworkId(), LoggedInUser.studentId);
            boolean prevScoreExists = false;
            if (prevScore != -1) {
                prevScoreExists = true;
            }
            int scoringType = homework.getScoreSelectionMethod();
            int final_score = 0;
            if (scoringType == LATEST_SCORE) {
                final_score = score;
            } else if (scoringType == MAXIMUM_SCORE) {
                final_score = prevScore > score ? prevScore : score;
            } else if (scoringType == AVERAGE_SCORE) {
                update_avg_score = true;
            }

            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(UPDATE_HOME_WORK_SUBMISSION);
            ps.setInt(3, attemptId);
            ps.setInt(1, score);
            ps.setInt(2, maxScore);
            ps.execute();

            if (update_avg_score) {
                ps = connection.prepareStatement(FETCH_AVG_SCORE);
                ps.setInt(1, LoggedInUser.studentId);
                ps.setInt(2, homework.getHomeworkId());

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    final_score = rs.getInt("avg");
                }
            }

            ps = connection.prepareStatement(UPDATE_SELECTED_ANSWER);

            for (int i = 0; i < answers.length; i++) {
                ps.setInt(1, answers[i]);
                ps.setInt(2, i + 1);
                ps.setInt(3, attemptId);

                int rowsUpdated = ps.executeUpdate();

                System.out.println("Updated answers for : " + rowsUpdated + " questions");

            }

            if (prevScoreExists) {
                ps = connection.prepareStatement(UPDATE_SCORE);
            } else {
                ps = connection.prepareStatement(INSERT_SCORE);
            }
            ps.setInt(1, final_score);
            ps.setInt(2, maxScore);
            ps.setInt(3, LoggedInUser.studentId);
            ps.setInt(4, homework.getHomeworkId());

            ps.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Homework getHomeworkByAttemptId(int attemptID) {
        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_HOMEWORK_BY_ATTEMPT_ID);

            ps.setInt(1, attemptID);

            ResultSet rs = ps.executeQuery();
            Homework homework = null;

            while (rs.next()) {
                Homework homework2 = new Homework();

                homework2.setHomeworkId(rs.getInt("id"));
                homework2.setDifficultyLevel(rs.getInt("difficulty_level"));
                homework2.setStartTime(rs.getString("start_date"));
                homework2.setEndTime(rs.getString("end_date"));
                homework2.setMarksPerQuestion(rs.getInt("points_per_question"));
                homework2.setRandomizationSeed(rs.getInt("randomized_seed"));
                homework2.setNoOfRetries(rs.getInt("no_of_retries"));
                homework2.setPenaltyPerQuestion(rs.getInt("penalty_per_question"));

                homework = homework2;

            }
            return homework;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public int getPreviousScore(int homeworkId, int studentId) {
        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_PAST_SCORE);

            ps.setInt(1, homeworkId);
            ps.setInt(2, studentId);

            ResultSet rs = ps.executeQuery();
            int score = -1;
            while (rs.next()) {
                score = rs.getInt("score");
            }
            return score;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }

    public List<String> getNotificationsById(int id, int role) {
        List<String> notifications = new ArrayList<String>();
        try {
            PreparedStatement ps;
            if (role == ROLE_STUDENT) {
                ps = connection.prepareStatement(FETCH_EXPIRING_HOMEWORKS);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    notifications.add("The homework " + rs.getString("name") + "is expiring in 24hrs, please attempt it to get score.");
                }

            } else if (role == ROLE_INSTRUCTOR) {
                ps = connection.prepareStatement(FETCH_ALERTS);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    StringBuilder s = new StringBuilder();
                    s.append("The student with id : ");
                    s.append(rs.getInt("teaching_assistant_id"));
                    s.append("has registered to course id : ");
                    s.append(rs.getInt("conflicting_course_id"));
                    s.append("which is having common topics with course id : ");
                    s.append(rs.getInt("course_id"));
                    notifications.add(s.toString());
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return notifications;
    }

    public List<Course> getCoursesByStudentId(int studentId) {

        List<Course> courses = new ArrayList<Course>();
        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_COURSES);

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course course = new Course();

                course.setId(rs.getInt("id"));
                course.setName(rs.getString("course_name"));

                courses.add(course);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return courses;
    }

    public List<Homework> getHomeworksByCourseId(int courseId) {
        List<Homework> homeworks = new ArrayList<Homework>();
        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_HOMEWORK_BY_COURSE_ID);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Homework homework2 = new Homework();
                homework2.setHomeworkId(rs.getInt("id"));
                homework2.setDifficultyLevel(rs.getInt("difficulty_level"));
                homework2.setStartTime(rs.getString("start_date"));
                homework2.setEndTime(rs.getString("end_date"));
                homework2.setMarksPerQuestion(rs.getInt("points_per_question"));
                homework2.setRandomizationSeed(rs.getInt("randomized_seed"));
                homework2.setNoOfRetries(rs.getInt("no_of_retries"));
                homework2.setPenaltyPerQuestion(rs.getInt("penalty_per_question"));

                homeworks.add(homework2);

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return homeworks;
    }

    public List<Attempt> getAttemptsByHomeworkId(int homeworkId, int studentId) {
        List<Attempt> attempts = new ArrayList<Attempt>();

        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_ATTEMPTS_BY_HOMEWORK_ID);

            ps.setInt(1, homeworkId);
            ps.setInt(2, studentId);

            ResultSet rs = ps.executeQuery();
            int i = 1;

            while (rs.next()) {
                Attempt at = new Attempt();

                at.setName("Attempt :" + i);
                i++;
                at.setId(rs.getInt("id"));
                at.setMaxScore((rs.getInt("maximum_score")));
                at.setScoreObtained(rs.getInt("SCORE_OBTAINED"));

                attempts.add(at);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return attempts;
    }

    public boolean enrollCourse(String token, int studentId) {

        try {
            PreparedStatement ps = connection.prepareStatement(FETCH_COURSE_ID_BY_TOKEN);

            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();

            int courseId = 0;

            if (rs.next()) {
                if (studentId == rs.getInt("teaching_assistant_id")) {
                    System.out.println("teaching assistant cant register for same course");
                } else {
                    courseId = rs.getInt("id");

                    int profId = rs.getInt("professor_id");

                    ps = connection.prepareStatement(ENROLL_COURSE);

                    ps.setInt(1, courseId);
                    ps.setInt(2, studentId);

                    ps.execute();
                    if (LoggedInUser.role == ROLE_TA) {
                        ps = connection.prepareStatement(FETCH_CONFLICTING_COURSES);

                        ps.setInt(1, courseId);
                        ps.setInt(2, studentId);

                        rs = ps.executeQuery();

                        while (rs.next()) {
                            ps = connection.prepareStatement(INSERT_ALERT);

                            ps.setInt(1, rs.getInt("professor_id"));
                            ps.setInt(2, rs.getInt("teaching_assistant_id"));
                            ps.setInt(3, rs.getInt("id"));
                            ps.setInt(4, courseId);

                            ps.execute();

                            ps.setInt(1, profId);
                            ps.setInt(2, rs.getInt("teaching_assistant_id"));
                            ps.setInt(3, rs.getInt("id"));
                            ps.setInt(4, courseId);
                            ps.execute();

                        }

                        return true;
                    }

                }

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public List<Score> getPastScores(int studentId) {
        List<Score> scores = new ArrayList<>();

        PreparedStatement ps;
        try {
            ps = connection.prepareStatement(FETCH_SCORES);

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Score s = new Score();
                s.setHomeworkName(rs.getString("name"));
                s.setScore_obtained(rs.getInt("score_obtained"));
                s.setMaximum_score(rs.getInt("maximum_score"));
                scores.add(s);
            }
        } catch (SQLException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return scores;
    }

    /*
     */
    /**
     * Testing class
     *
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DataBaseConnection dc = new DataBaseConnection(DB_DRIVER, DB_URL, DB_USERNAME, DB_PASSWORD);
        dc.makeDatabaseConnection();
        HomeworkService hs = new HomeworkService();

        //System.out.println("home work attempt 1 :\n\n"+hs.getQuestionsByHomeworkId(1,1));
        //System.out.println("home work attempt 2 :\n\n"+hs.getQuestionsByHomeworkId(1,1));
        //System.out.println("home work attempt 3 :\n\n"+hs.getQuestionsByHomeworkId(1,1));
        System.out.println("home work attempt details" + hs.getHomeworkByAttemptId(127));
    }

}
