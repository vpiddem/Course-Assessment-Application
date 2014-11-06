package database;

import beans.LoggedInUser;
import beans.Score;
import static gradience.GradienceAppConstants.ROLE_INSTRUCTOR;
import static gradience.GradienceAppConstants.ROLE_STUDENT;
import static gradience.GradienceAppConstants.ROLE_TA;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import structures.AccountDetails;
import structures.Homework;

public class DataBaseConnection implements gradience.GradienceAppConstants {

    public void CreateDatabaseTables() {
        String questionsQuery = "CREATE TABLE IF NOT EXISTS QUESTIONS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "TOPICID BIGINT NOT NULL,"
                + "CONTENT TEXT,"
                + "DIFFICULTY_LEVEL BIGINT DEFAULT 1,"
                + "QUESTION_HINT VARCHAR(100) DEFAULT '-',"
                + "TYPE BIGINT DEFAULT 0)";

        String answerQuery = "CREATE TABLE IF NOT EXISTS ANSWERS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "QUESTIONID BIGINT NOT NULL,"
                + "CONTENT TEXT,"
                + "EXPLANATION TEXT,"
                + "CORRECTNESS_FLAG BIGINT DEFAULT 0,"
                + "SETID BIGINT NOT NULL)";

        String parametersQuery = "CREATE TABLE IF NOT EXISTS PARAMETERS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "QUESTIONID BIGINT NOT NULL,"
                + "SETID BIGINT NOT NULL,"
                + "VALUE VARCHAR(20) DEFAULT '-')";

        String homeworkQuery = "CREATE TABLE IF NOT EXISTS HOMEWORKS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "SCORING_METHOD BIGINT DEFAULT 1,"
                + "START_DATE TIMESTAMP,"
                + "END_DATE TIMESTAMP,"
                + "RANDOMIZED_SEED BIGINT NOT NULL,"
                + "NO_OF_RETRIES BIGINT DEFAULT 0,"
                + "POINTS_PER_QUESTION BIGINT DEFAULT 4,"
                + "PENALTY_PER_QUESTION BIGINT DEFAULT 4,"
                + "DIFFICULTY_LEVEL BIGINT DEFAULT 0,"
                + "COURSE_ID BIGINT)";

        String examQuestions = "CREATE TABLE IF NOT EXISTS EXAMQUESTIONS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "ANSWER1_ID BIGINT,"
                + "ANSWER2_ID BIGINT,"
                + "ANSWER3_ID BIGINT,"
                + "ANSWER4_ID BIGINT,"
                + "ATEEMPTID BIGINT DEFAULT 1)";

        String attemptQuery = "CREATE TABLE IF NOT EXISTS ATTEMPTS "
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "HOMEWORK_ID BIGINT,"
                + "SUBMISSION_TIME TIMESTAMP,"
                + "ANSWER_PATTERN VARCHAR(30))";

        String homeworkQuestions = "CREATE TABLE IF NOT EXISTS HOMEWORKQUESTIONS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "HOMEWORK_ID BIGINT,"
                + "QUESTION_ID BIGINT)";

        String usersQuery = "CREATE TABLE IF NOT EXISTS USERS "
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "USERNAME VARCHAR(50) DEFAULT 'user',"
                + "PASSWORD VARCHAR(50) DEFAULT 'password',"
                + "LOGIN_STATUS VARCHAR(1) DEFAULT 'N'"
                + "ROLE BIGINT DEFAULT 2)";

        String professorsQuery = " CREATE TABLE PROFESSORS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "NAME VARCHAR NOT NULL,"
                + "USER_ID BIGINT NOT NULL,"
                + "CONSTRAINT user_exists FOREIGN KEY(USER_ID) REFERENCES USERS"
                + " )";

        String studentsQuery = "CREATE TABLE STUDENTS("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "NAME VARCHAR NOT NULL,"
                + "LEVEL BIGINT NOT NULL,"
                + "TYPE BIGINT NOT NULL DEFAULT 1,"
                + "USER_ID BIGINT NOT NULL,"
                + "CONSTRAINT users_exists FOREIGN KEY(USER_ID) REFERENCES USERS(ID)"
                + " )";

        String coursesQuery = " CREATE TABLE IF NOT EXISTS COURSES"
                + "( "
                + "ID BIGSERIAL PRIMARY KEY, "
                + "COURSE_NAME VARCHAR NOT NULL, "
                + "START_DATE TIMESTAMP NOT NULL, "
                + "END_DATE TIMESTAMP NOT NULL, "
                + "TOTAL_STUDENTS BIGINT NOT NULL DEFAULT 0, "
                + "LEVEL BIGINT NOT NULL , "
                + "MAX_ENROLLMENTS BIGINT NOT NULL, "
                + "PROFESSOR_ID BIGINT NOT NULL, "
                + "TEACHING_ASSISTANT_ID BIGINT NOT NULL,"
                + "CONSTRAINT profesors_exists FOREIGN KEY(PROFESSOR_ID) REFERENCES PROFESSORS(ID),"
                + "CONSTRAINT student_exists FOREIGN KEY(TEACHING_ASSISTANT_ID) REFERENCES STUDENTS(ID)"
                + " )";

        String enrollmentsQuery = "CREATE TABLE ENROLLMENTS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "STUDENT_ID BIGINT NOT NULL,"
                + "COURSE_ID BIGINT NOT NULL,"
                + "CONSTRAINT student_exists FOREIGN KEY(STUDENT_ID) REFERENCES STUDENTS(ID)"
                + "CONSTRAINT course_exists FOREIGN KEY(COURSE_ID) REFERENCES COURSES(ID)"
                + ")";

//        CREATE TABLE editions
//postgres-#               (isbn text,
//postgres(#               book_id integer,
//postgres(#               edition integer,
//postgres(#               publisher_id integer,
//postgres(#               publication date,
//postgres(#               type char,
//postgres(#               CONSTRAINT pkey PRIMARY KEY (isbn),
//postgres(#               CONSTRAINT integrity CHECK (book_id IS NOT NULL
//postgres(#                                           AND edition IS NOT NULL),
//postgres(#               CONSTRAINT book_exists FOREIGN KEY (book_id)
//postgres(#                          REFERENCES books (id)
//postgres(#                          ON DELETE CASCADE
//postgres(#                          ON UPDATE CASCADE);
        String topicsQuery = "CREATE TABLE TOPICS"
                + "("
                + "ID BIGSERIAL PRIMARY KEY,"
                + "NAME VARCHAR NOT NULL,"
                + "COURSE_ID BIGINT NOT NULL,"
                + "CONSTRAINT course_exists FOREIGN KEY(COURSE_ID) REFERENCES COURSES(ID)"
                + " )";

        try {
            //makeDatabaseConnection();

            try {

                if (!IsTableExists("QUESTIONS")) {
                    statement.executeUpdate(questionsQuery);
                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : QUESTIONS TABLE CREATION");
            }
            try {

                if (!IsTableExists("ANSWERS")) {
                    statement.executeUpdate(answerQuery);

                }

            } catch (SQLException sQLException) {
                System.err.println(" ERROR : ANSWERS TABLE CREATION");
            }
            try {
                if (!IsTableExists("PARAMETERS")) {
                    statement.executeUpdate(parametersQuery);

                }

            } catch (SQLException sQLException) {
                System.err.println(" ERROR : PARAMETERS TABLE CREATION");
            }
            try {
                if (!IsTableExists("HOMEWORKS")) {
                    statement.executeUpdate(homeworkQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : HOMEWORKS TABLE CREATION");
            }

            try {
                if (!IsTableExists("EXAMQUESTIONS")) {
                    statement.executeUpdate(examQuestions);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : EXAMS TABLE CREATION");
            }
            try {
                if (!IsTableExists("ATTEMPTS")) {
                    statement.executeUpdate(attemptQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : ATTEMPTS TABLE CREATION");
            }
            try {
                if (!IsTableExists("HOMEWORKQUESTIONS")) {
                    statement.executeUpdate(homeworkQuestions);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : HOMEWORKQUESTIONS Joining TABLE CREATION");
            }
            try {
                if (!IsTableExists("USERS")) {
                    statement.executeUpdate(usersQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : USERS TABLE CREATION");
            }
            try {
                if (!IsTableExists("COURSES")) {
                    statement.executeUpdate(coursesQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : COURSES TABLE CREATION");
            }
            try {
                if (!IsTableExists("STUDENTS")) {
                    statement.executeUpdate(studentsQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : STUDENTS TABLE CREATION");
            }
            try {
                if (!IsTableExists("PROFESSORS")) {
                    statement.executeUpdate(professorsQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : PROFESSORS TABLE CREATION");
            }
            try {
                if (!IsTableExists("ENROLLMENTS")) {
                    statement.executeUpdate(enrollmentsQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : ENROLLMENTS TABLE CREATION");
            }
            try {
                if (!IsTableExists("COURSES")) {
                    statement.executeUpdate(coursesQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : COURSES TABLE CREATION");
            }
            try {
                if (!IsTableExists("TOPICS")) {
                    statement.executeUpdate(topicsQuery);

                }
            } catch (SQLException sQLException) {
                System.err.println(" ERROR : TOPICS TABLE CREATION");
            }
        } catch (Exception ex) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeDatabaseConnection();
        }
    }

    public DataBaseConnection(String driver, String url, String username, String password) {
        Driver = driver;
        URL = url;
        Username = username;
        Password = password;
    }

    /**
     * Attempts to establish Connection to Database
     */
    public synchronized void makeDatabaseConnection() {
        try {
            Class.forName(Driver);
            connection = DriverManager.getConnection(URL, Username, Password);
            statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            connectedToDatabase = true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Closes the Database Connection
     */
    public synchronized void closeDatabaseConnection() {
        if (connectedToDatabase) {
            try {
                statement.close();
                connection.close();
            } catch (SQLException ex) {
            } finally {
                connectedToDatabase = false;
            }
        }
    }

    public void insertLoginDetails() {
        try {
            //  makeDatabaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // closeDatabaseConnection();
        }
    }

    public boolean isPasswordEqualsOldPassword(String enteredPassword) {
        try {
            //makeDatabaseConnection();
            String sqlQuery = "SELECT PASSWORD FROM USERS WHERE ID=" + LoggedInUser.userId;
            ResultSet rs = statement.executeQuery(sqlQuery);
            if (rs.next()) {
                return rs.getString("PASSWORD").equals(enteredPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return false;
    }

    public String getPasswordUpdateTime(String password) {
        try {
            String sqlQuery = "SELECT PASSWORD_UPDATE_DATE FROM USERS WHERE OLD_PASSWORD='" + password + "'";
            ResultSet rs = statement.executeQuery(sqlQuery);
            if (rs.next()) {
                return rs.getString("PASSWORD_UPDATE_DATE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void updateLoginUserDetails(AccountDetails accDetails) {
        try {
            //makeDatabaseConnection();
            String sqlQuery = "UPDATE USERS SET USERNAME='" + accDetails.getName() + "',"
                    + "EMAIL='" + accDetails.getEmail() + "',"
                    + "OLD_PASSWORD=PASSWORD,"
                    + "PASSWORD='" + accDetails.getPassword() + "',"
                    + "PASSWORD_UPDATE_DATE = now(),"
                    + "PHONENUMBER='" + accDetails.getPhoneNumberString() + "' WHERE ID=" + LoggedInUser.userId;
            statement.executeUpdate(sqlQuery);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    private synchronized boolean IsTableExists(String strTableName) {
        boolean tableExists = false;
        try {
            String SQLQuery = "SELECT * FROM ALL_OBJECTS WHERE OBJECT_TYPE IN('TABLE','VIEW') AND OBJECT_NAME = '" + strTableName
                    + "' AND OWNER='" + Username.toUpperCase() + "'";
            SQLQuery = "SELECT relname FROM pg_class "
                    + " WHERE relname = '" + strTableName + "'";

            SQLQuery = "select * from information_schema.tables where table_schema='"
                    + strTableName
                    + "' and table_type='BASE TABLE'";
            ResultSet rs = statement.executeQuery(SQLQuery);
            if (rs.next()) {
                tableExists = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tableExists;
    }

    //USERS Query
    //<editor-fold defaultstate="collapsed" desc="USERS TABLE OPERATIONS">
    public boolean isValidLoginCredentials(String userName, String password) {
        String query = "SELECT PASSWORD FROM USERS  WHERE LOWER(USERNAME) = '" + userName.trim().toLowerCase() + "'";
        try {
            //makeDatabaseConnection();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return (password).equals(rs.getString("PASSWORD"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return false;
    }

    public synchronized boolean isUserExists(String userName) {
        String query = "SELECT * FROM USERS WHERE lower(USERNAME)='" + userName.toLowerCase() + "'";
        try {
            //makeDatabaseConnection();
            ResultSet rs = statement.executeQuery(query);
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return false;
    }

    public synchronized void updatePassword(String userName, String newPassword) {
        String query;
        try {
            //makeDatabaseConnection();
            query = "UPDATE USERS SET PASSWORD = '" + newPassword + "' WHERE USERNAME='" + userName.toLowerCase() + "'";
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public synchronized void updateLoginStatus(String userName, String loginStatus) {
        String SQLstr;
        try {
            //makeDatabaseConnection();
            SQLstr = "UPDATE USERS SET  LOGIN_STATUS= '" + loginStatus + "' WHERE lower(USERNAME) ='" + userName.toLowerCase() + "'";
            statement.executeUpdate(SQLstr);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }
//</editor-fold>

    public static Connection connection;
    private static Statement statement;
    public static boolean connectedToDatabase = false;
    private static String Driver;
    public static String URL;
    private String Username;
    private String Password;

    public synchronized void setCoursesTable(JXTable table_Courses, Vector<String> vector_CourseHeaders) {
        Vector<Vector<Object>> courseInfo = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT DISTINCT * FROM COURSES ORDER BY START_DATE DESC";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(false);
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("COURSE_NAME"));
                vector_cellData.add(rs.getString("START_DATE"));
                vector_cellData.add(rs.getString("START_DATE"));
                vector_cellData.add(rs.getString("END_DATE"));
                vector_cellData.add(rs.getString("MAX_ENROLLMENTS"));
                vector_cellData.add(rs.getString("LEVEL"));
                courseInfo.add(vector_cellData);
            }
            table_Courses.setModel(new DefaultTableModel(
                    courseInfo,
                    vector_CourseHeaders));
            table_Courses.getColumnModel().getColumn(0).setCellEditor(table_Courses.getDefaultEditor(Boolean.class));
            table_Courses.getColumnModel().getColumn(0).setCellRenderer(table_Courses.getDefaultRenderer(Boolean.class));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public synchronized void setTextBooksTable(JXTable table_TextBooks, Vector<String> vector_TextBookHeaders, int selectedCourseId) {
        Vector<Vector<Object>> textBookInfo = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT DISTINCT * FROM TEXTBOOKS "
                    + " WHERE COURSE_ID = " + selectedCourseId + " ORDER BY TITLE DESC";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(rs.getString("TITLE"));

                vector_cellData.add(rs.getString("AUTHOR"));
                vector_cellData.add(rs.getString("ISBNNO"));
                textBookInfo.add(vector_cellData);
            }
            table_TextBooks.setModel(new DefaultTableModel(
                    textBookInfo,
                    vector_TextBookHeaders));
//            table_TextBooks.getColumnModel().getColumn(0).setCellEditor(table_TextBooks.getDefaultEditor(Boolean.class));
//            table_TextBooks.getColumnModel().getColumn(0).setCellRenderer(table_TextBooks.getDefaultRenderer(Boolean.class));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public void setQuestionsTableByDifficultyLevel(JXTable table_Questions, Vector<String> vector_QuestionHeaders, int difficultyLevel) {
        Vector<Vector<Object>> questionData = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT DISTINCT * FROM QUESTIONS WHERE DIFFICULTY_LEVEL=" + difficultyLevel + " ORDER BY ID DESC";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(false);
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("CONTENT"));
                questionData.add(vector_cellData);
            }
            table_Questions.setModel(new DefaultTableModel(
                    questionData,
                    vector_QuestionHeaders));
            table_Questions.getColumnModel().getColumn(0).setCellEditor(table_Questions.getDefaultEditor(Boolean.class));
            table_Questions.getColumnModel().getColumn(0).setCellRenderer(table_Questions.getDefaultRenderer(Boolean.class));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public void setQuestionsTable(JXTable table_Questions, Vector<String> vector_QuestionHeaders) {
        Vector<Vector<Object>> questionData = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT DISTINCT * FROM QUESTIONS ORDER BY ID DESC";

            if (LoggedInUser.role == ROLE_TA) {
                SQLQuery = "SELECT q.* FROM QUESTIONS q,topics t,courses c WHERE q.topicid = t.id and t.course_id = c.id and c.teaching_assistant_id = " + LoggedInUser.studentId;
            }

            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(false);
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("CONTENT"));
                questionData.add(vector_cellData);
            }
            table_Questions.setModel(new DefaultTableModel(
                    questionData,
                    vector_QuestionHeaders));
            table_Questions.getColumnModel().getColumn(0).setCellEditor(table_Questions.getDefaultEditor(Boolean.class));
            table_Questions.getColumnModel().getColumn(0).setCellRenderer(table_Questions.getDefaultRenderer(Boolean.class));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public Vector<String> getTopicDataByCourseId(int courseId) {
        Vector<String> vector_TopicsByCourse = new Vector<String>();
        try {
            //makeDatabaseConnection();
            String query = "SELECT * FROM TOPICS WHERE COURSE_ID=" + courseId;

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                vector_TopicsByCourse.add(rs.getString("name"));
            }

        } catch (Exception e) {
        } finally {
            //closeDatabaseConnection();
        }
        return vector_TopicsByCourse;
    }

    public int getCourseIdByCourseName(String courseName) {
        int courseId = 0;
        try {
            //makeDatabaseConnection();
            String query = "SELECT ID FROM COURSES WHERE COURSE_NAME='" + courseName + "'";

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                courseId = rs.getInt("ID");
            }
        } catch (Exception e) {
        } finally {
            //closeDatabaseConnection();
        }
        return courseId;
    }

    public void insertIntoQuestionsTableByHomeWork() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getIncrementMaxHomeWorkID() {
        int incrementedHomeworkId = 0;
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT MAX(ID)+1 AS ID FROM HOMEWORKS";
            ResultSet rs = statement.executeQuery(SQLQuery);
            if (rs.next()) {
                incrementedHomeworkId = rs.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return incrementedHomeworkId + "";
    }

    public void insertIntoQuestionsTableByHomeWork(ArrayList<Integer> list_QuestionID, int homeWorkID) {
        try {
            //makeDatabaseConnection();
            String SQLQuery = "INSERT INTO HOMEWORK_QUESTIONS(HOMEWORK_ID,QUESTION_ID) VALUES";
            for (int i = 0; i < list_QuestionID.size(); i++) {
                SQLQuery += "(" + homeWorkID + "," + list_QuestionID.get(i) + "),";
            }
            SQLQuery = SQLQuery.substring(0, SQLQuery.length() - 1);
            statement.executeUpdate(SQLQuery);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public void setCourseRollTable(JXTable table_CourseRoll, Vector<String> vector_CourseRollHeaders, int courseId) {
        Vector<Vector<Object>> questionData = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT e.STUDENT_ID AS ID,s.NAME AS NAME,s.LEVEL AS LEVEL FROM ENROLLMENTS e,STUDENTS s WHERE e.COURSE_ID=" + courseId + " AND e.STUDENT_ID  = s.ID";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("NAME"));
                vector_cellData.add(rs.getInt("LEVEL") == 2 ? "STUDENT" : "TA");
                questionData.add(vector_cellData);
            }
            table_CourseRoll.setModel(new DefaultTableModel(
                    questionData,
                    vector_CourseRollHeaders));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public Vector<String> getCourseNameList() {
        Vector<String> vector_TopicsByCourse = new Vector<String>();
        try {
            //makeDatabaseConnection();
            String query = "SELECT COURSE_NAME FROM COURSES";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                vector_TopicsByCourse.add(rs.getString("COURSE_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return vector_TopicsByCourse;
    }

    public Vector<String> getCourseNameListForTA(int studentId) {
        Vector<String> vector_TopicsByCourse = new Vector<String>();
        try {
            //makeDatabaseConnection();
            String query = "SELECT COURSE_NAME FROM COURSES WHERE TEACHING_ASSISTANT_ID =" + studentId;
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                vector_TopicsByCourse.add(rs.getString("COURSE_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return vector_TopicsByCourse;
    }

    public Vector<String> getCourseNameListByProfessorID(int profID) {
        Vector<String> vector_TopicsByCourse = new Vector<String>();
        try {
            //makeDatabaseConnection();
            String query = "SELECT COURSE_NAME FROM COURSES WHERE PROFESSOR_ID=" + profID;
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                vector_TopicsByCourse.add(rs.getString("COURSE_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return vector_TopicsByCourse;
    }

    public ArrayList<String> getNotificationsById(int i, int i0) {
        ArrayList<String> list_HomeworkNames = new ArrayList<>();
        try {
            //makeDatabaseConnection();
        } catch (Exception e) {
        } finally {
            //closeDatabaseConnection();
        }
        return list_HomeworkNames;
    }

    int getRoleOfLoggedInUser(String email, String password) {

        try {
            //makeDatabaseConnection();
            PreparedStatement ps = connection.prepareStatement(FETCH_USER);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                LoggedInUser.userId = rs.getInt("id");
                LoggedInUser.role = rs.getInt("role");

                if (LoggedInUser.role == ROLE_STUDENT || LoggedInUser.role == ROLE_TA) {
                    ps = connection.prepareStatement(FETCH_STUDENT_ID);
                    ps.setInt(1, LoggedInUser.userId);

                    ResultSet studentResult = ps.executeQuery();

                    while (studentResult.next()) {
                        LoggedInUser.studentId = studentResult.getInt("id");
                        LoggedInUser.name = studentResult.getString("name");
                    }
                } else if (LoggedInUser.role == ROLE_INSTRUCTOR) {
                    ps = connection.prepareStatement(FETCH_PROFESSOR_ID);
                    ps.setInt(1, LoggedInUser.userId);

                    ResultSet profResult = ps.executeQuery();

                    while (profResult.next()) {
                        LoggedInUser.professorId = profResult.getInt("id");
                        LoggedInUser.name = profResult.getString("name");
                    }
                }
                return LoggedInUser.role;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return -1;
    }

    public void setLastLoggedInTime() {
        //makeDatabaseConnection();
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("update users set last_login_date = now() where id = ?");
            ps.setInt(1, LoggedInUser.userId);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLastLoggedInDate() {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("select last_login_date from users where id = ?");

            ps.setInt(1, LoggedInUser.userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return rs.getString("last_login_date");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Vector<String> getHomeWorksByCourseID(int selectedCourseID) {
        Vector<String> vector_HomeWorksByCourse = new Vector<String>();
        try {
            //makeDatabaseConnection();
            String query = "SELECT ID FROM HOMEWORKS WHERE COURSE_ID=" + selectedCourseID;
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                vector_HomeWorksByCourse.add(rs.getString("ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return vector_HomeWorksByCourse;
    }

    public void insertIntoHomeWorksTable(Homework homeWorkStructure) {
        try {
            String SQLQuery = "INSERT INTO HOMEWORKS(NAME,SCORING_METHOD,START_DATE,END_DATE,RANDOMIZED_SEED,NO_OF_RETRIES,POINTS_PER_QUESTION,PENALTY_PER_QUESTION,"
                    + "DIFFICULTY_LEVEL,COURSE_ID) VALUES("
                    + "'" + homeWorkStructure.getHomeworkName() + "',"
                    + homeWorkStructure.getScoreSelectionMethod() + ","
                    + "TO_TIMESTAMP('" + homeWorkStructure.getStartTime() + "','MM-DD-YYYY HH24:MI:SS')" + ","
                    + "TO_TIMESTAMP('" + homeWorkStructure.getEndTime() + "','MM-DD-YYYY HH24:MI:SS')" + ","
                    + homeWorkStructure.getRandomizationSeed() + ","
                    + homeWorkStructure.getNoOfRetries() + ","
                    + homeWorkStructure.getMarksPerQuestion() + ","
                    + homeWorkStructure.getPenaltyPerQuestion() + ","
                    + homeWorkStructure.getDifficultyLevel() + ","
                    + homeWorkStructure.getCourseID() + ""
                    + ")";
            statement.executeUpdate(SQLQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAnswersTable(JXTable table_Answers, Vector<String> vector_AnswerHeaders, int questionId) {

        Vector<Vector<Object>> answersTableInfo = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT DISTINCT * FROM ANSWERS WHERE QUESTION_ID= " + questionId + "";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("CONTENT"));
                vector_cellData.add(rs.getString("EXPLANATION"));
                String strTemp = rs.getString("CORRECTNESS_FLAG");
                String retValue = "wrong";
                if (strTemp != null) {
                    retValue = (strTemp.equalsIgnoreCase("t")) ? "Right" : "Wrong";
                }

                System.out.println(" " + retValue);
                vector_cellData.add(retValue);
                answersTableInfo.add(vector_cellData);
            }
            table_Answers.setModel(new DefaultTableModel(
                    answersTableInfo,
                    vector_AnswerHeaders));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }

    }

    public void executeUpdateCommand(String txt_Query) {
        try {
            statement.executeUpdate(txt_Query);
        } catch (Exception e) {
            System.err.println("Invalid Update Query Found");
            JOptionPane.showMessageDialog(null, "Invalid Query \n Please Check the Query", "Info", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void generateCsvFile(String query) {
        try {
            PrintWriter writer = new PrintWriter("data.txt");
            ResultSet rs = statement.executeQuery(query);
            int columnCount = rs.getMetaData().getColumnCount();
            ArrayList<String> list_ColumnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                list_ColumnNames.add(rs.getMetaData().getColumnName(i));
                writer.print(list_ColumnNames.get(i - 1) + " | ");
            }
            writer.println();
            while (rs.next()) {
                for (int i = 0; i < columnCount; i++) {
                    writer.print(rs.getString(list_ColumnNames.get(i)) + " | ");
                }
                writer.println();
            }
            //generate whatever data you want
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println(" *** Opening File *** ");
                Desktop.getDesktop().open(new File("data.txt"));
            } catch (IOException ex) {
                Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void updateHomeWorksTable(Homework homeWorkStructure) {
        try {
            String SQLQuery = "UPDATE HOMEWORKS SET "
                    + "NAME = '" + homeWorkStructure.getHomeworkName() + "',"
                    + "SCORING_METHOD=" + homeWorkStructure.getScoreSelectionMethod() + ","
                    + "START_DATE = TO_TIMESTAMP('" + homeWorkStructure.getStartTime() + "','MM-DD-YYYY HH24:MI:SS')" + ","
                    + "END_DATE = TO_TIMESTAMP('" + homeWorkStructure.getEndTime() + "','MM-DD-YYYY HH24:MI:SS')" + ","
                    + "RANDOMIZED_SEED=" + homeWorkStructure.getRandomizationSeed() + ","
                    + "NO_OF_RETRIES=" + homeWorkStructure.getNoOfRetries() + ","
                    + "POINTS_PER_QUESTION=" + homeWorkStructure.getMarksPerQuestion() + ","
                    + "PENALTY_PER_QUESTION=" + homeWorkStructure.getPenaltyPerQuestion() + ","
                    + "DIFFICULTY_LEVEL=" + homeWorkStructure.getDifficultyLevel() + ","
                    + "COURSE_ID=" + homeWorkStructure.getCourseID() + " WHERE ID=" + homeWorkStructure.getHomeworkId();
            statement.executeUpdate(SQLQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<String> getHomeWorksIDList() {
        Vector<String> vector_homeworkIds = new Vector<>();
        try {
            String SQLQuery = "SELECT ID FROM HOMEWORKS";
            ResultSet rs = statement.executeQuery(SQLQuery);

            while (rs.next()) {
                vector_homeworkIds.add(rs.getString("ID"));
            }

        } catch (Exception e) {
        }
        return vector_homeworkIds;

    }

    public void setHomeWorkStructureFromDB(Homework homeWorkStructure, int selectedHomeWorkID) {
        try {
            String sqlQuery = "SELECT * FROM HOMEWORKS WHERE ID=" + selectedHomeWorkID;
            ResultSet rs = statement.executeQuery(sqlQuery);
            if (rs.next()) {
                homeWorkStructure.setCourseID(rs.getInt("COURSE_ID"));
                homeWorkStructure.setHomeworkName(rs.getString("NAME"));
                homeWorkStructure.setRandomizationSeed(rs.getInt("RANDOMIZED_SEED"));
                homeWorkStructure.setNoOfRetries(rs.getInt("NO_OF_RETRIES"));
                homeWorkStructure.setDifficultyLevel(rs.getInt("DIFFICULTY_LEVEL"));
                homeWorkStructure.setMarksPerQuestion(rs.getInt("POINTS_PER_QUESTION"));
                homeWorkStructure.setPenaltyPerQuestion(rs.getInt("PENALTY_PER_QUESTION"));
                homeWorkStructure.setStartTime(rs.getString("START_DATE"));
                homeWorkStructure.setEndTime(rs.getString("END_DATE"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getCourseNameByCourseID(int courseID) {
        String courseName = "";
        try {
            ResultSet rs = statement.executeQuery("SELECT NAME FROM COURSES WHERE COURSE_ID=" + courseID);
            courseName = rs.getString("NAME");
        } catch (Exception e) {
        }
        return courseName;
    }

    public int getHomeWorkIdByHomeWorkName(String homeWorkName) {
        try {
            String sqlQuery = "SELECT ID FROM HOMEWORKS WHERE NAME='" + homeWorkName + "'";
            ResultSet rs = statement.executeQuery(sqlQuery);
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Vector<String> getAttemptIDListByHomeWorkID(int selectedHomeWorkID) {
        Vector<String> vector_AttemptsList = new Vector<>();
        try {
            String sqlQuery = "SELECT ID FROM ATTEMPTS WHERE HOMEWORK_ID=" + selectedHomeWorkID;
            ResultSet rs = statement.executeQuery(sqlQuery);
            while (rs.next()) {
                vector_AttemptsList.add(rs.getString("ID"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector_AttemptsList;
    }

    public Vector<String> getHomeWorkNamesList() {
        Vector<String> vector_HomeWorkNames = new Vector<>();
        try {
            String sqlQuery = "SELECT DISTINCT NAME FROM HOMEWORKS where course_id in (select distinct(id) from courses where professor_id = " + LoggedInUser.professorId + ")";
            if (LoggedInUser.role == ROLE_STUDENT) {
                sqlQuery = "SELECT DISTINCT NAME FROM HOMEWORKS WHERE END_DATE < now() and COURSE_ID in (select distinct(e.course_id) from enrollments e where e.id = " + LoggedInUser.studentId + ")";
            } else if (LoggedInUser.role == ROLE_TA) {
                sqlQuery = "SELECT DISTINCT NAME FROM HOMEWORKS where course_id in (select distinct(id) from courses where teaching_assistant_id = " + LoggedInUser.studentId + ") "
                        + "union "
                        + "SELECT DISTINCT NAME FROM HOMEWORKS WHERE END_DATE < now() and COURSE_ID in (select distinct(e.course_id) from enrollments e where e.id = " + LoggedInUser.studentId + ")";
            }
            ResultSet rs = statement.executeQuery(sqlQuery);
            while (rs.next()) {
                vector_HomeWorkNames.add(rs.getString("NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector_HomeWorkNames;
    }

    public Vector<String> getStudentIdNameList() {
        Vector<String> vector_StudentIds = new Vector<>();
        try {
            String sqlQuery = "SELECT ID,NAME FROM STUDENTS";
            ResultSet rs = statement.executeQuery(sqlQuery);
            while (rs.next()) {
                vector_StudentIds.add(rs.getString("ID") + "|" + rs.getString("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vector_StudentIds;

    }

    public void setFinalScoresTable(JXTable table_Scores, Vector<String> vector_FinalScoreHeaders, int selectedStudentID) {
        Vector<Vector<Object>> finalScoresInfo = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT s.*,h.* FROM SCORES s,homeworks h WHERE s.STUDENT_ID=" + selectedStudentID + "and s.homework_id = h.id";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(rs.getString("NAME"));
                vector_cellData.add(rs.getString("SCORE"));
                vector_cellData.add(rs.getString("MAXIMUM_SCORE"));
                finalScoresInfo.add(vector_cellData);
            }
            table_Scores.setModel(new DefaultTableModel(
                    finalScoresInfo,
                    vector_FinalScoreHeaders));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
        }
    }

    public void setCoursesTableByStudentID(JXTable table_CoursesEnrolled, Vector<String> vector_CourseHeaders, int studentId) {
        Vector<Vector<Object>> courseInfo = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "select c.course_name, c.id from courses c, students s, enrollments e where s.id = " + studentId + " and s.id = e.student_id "
                    + "and e.course_id = c.id";;
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("COURSE_NAME"));
//                vector_cellData.add(rs.getString("START_DATE"));
//                vector_cellData.add(rs.getString("START_DATE"));
//                vector_cellData.add(rs.getString("END_DATE"));
//                vector_cellData.add(rs.getString("MAX_ENROLLMENTS"));
//                vector_cellData.add(rs.getString("LEVEL"));
                courseInfo.add(vector_cellData);
            }
            table_CoursesEnrolled.setModel(new DefaultTableModel(
                    courseInfo,
                    vector_CourseHeaders));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

    public boolean isOldPassword(String userName, String password) {
        try {
            String sqlQuery = "SELECT OLD_PASSWORD FROM USERS WHERE lower(USERNAME)='" + userName.toLowerCase() + "'";
            ResultSet rs = statement.executeQuery(sqlQuery);
            if (rs.next()) {
                return rs.getString("OLD_PASSWORD").equals(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Vector<String> getCourseNameListByStudentID(int studentId) {

        Vector<String> vector_StudentCourses = new Vector<String>();
        try {
            //makeDatabaseConnection();
            String query = "SELECT c.COURSE_NAME FROM COURSES c, enrollments e WHERE e.STUDENT_ID=" + studentId + " and e.course_id = c.id";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                vector_StudentCourses.add(rs.getString("COURSE_NAME"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
        return vector_StudentCourses;

    }

    public int getTopicIDByTopicName(String topicName) {
        try {
             String sqlQuery = "";
             ResultSet rs = statement.executeQuery("SELECT ID FROM TOPICS WHERE NAME='"+topicName+"'");
             if(rs.next()){
                 return rs.getInt("ID");
             }
        } catch (Exception e) {
            
        }
        return 0;
    }

    public void setQuestionsTableByTopicID(JXTable table_Questions, Vector<String> vector_QuestionHeaders, int topicID) {
        Vector<Vector<Object>> questionData = new Vector<>();
        try {
            //makeDatabaseConnection();
            String SQLQuery = "SELECT DISTINCT * FROM QUESTIONS WHERE topicid=" + topicID + " ORDER BY ID DESC";
            ResultSet rs;
            rs = statement.executeQuery(SQLQuery);
            while (rs.next()) {
                Vector<Object> vector_cellData = new Vector<>();
                vector_cellData.add(false);
                vector_cellData.add(rs.getString("ID"));
                vector_cellData.add(rs.getString("CONTENT"));
                questionData.add(vector_cellData);
            }
            table_Questions.setModel(new DefaultTableModel(
                    questionData,
                    vector_QuestionHeaders));
            table_Questions.getColumnModel().getColumn(0).setCellEditor(table_Questions.getDefaultEditor(Boolean.class));
            table_Questions.getColumnModel().getColumn(0).setCellRenderer(table_Questions.getDefaultRenderer(Boolean.class));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //closeDatabaseConnection();
        }
    }

}
