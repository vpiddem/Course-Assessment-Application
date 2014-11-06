/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradience;

/**
 *
 * @author Vikas
 */
public interface GradienceAppConstants {

    //==Role RELATED CONSTANTS == //
    static final int ROLE_INSTRUCTOR = 1;
    static final int ROLE_STUDENT = 2;
    static final int ROLE_TA = 3;

    //== Course LEVEL CONSTANTS == 
    static final int LEVEL_POSTGRAD = 1;
    static final int LEVEL_GRAD = 2;
    static final int LEVEL_UNDERGRAD = 3;

    static final int LATEST_SCORE = 0;
    static final int MAXIMUM_SCORE = 1;
    static final int AVERAGE_SCORE = 2;

    //== Question Type CONSTANTS == 
    static final int QUESTIONTYPE_MULTIPLECHOICE = 1;

    String GradienceAppIcon = "icons/gradience.png";
    String GradienceDBIcon = "icons/viewdb.png";

    //-----------------------------------------------------------------------
    //                     DB variables
    //-----------------------------------------------------------------------
    //
    //<editor-fold defaultstate="collapsed" desc="MySQL DB Constants">
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost/gradience";
    String user = "root";
    String passwd = "root";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="POSTGRES DB Constants">
    public String DB_DRIVER = "org.postgresql.Driver";
    public String DB_URL = "jdbc:postgresql://localhost:5432/gradiencedb";
    public String DB_USERNAME = "postgres";
    public String DB_PASSWORD = "postgres";
    public int DB_PORT = 5432;
    //</editor-fold>

    //LOGIN related Queries 
    String FETCH_USER = "select * from users where lower(username) = ? and password = ?";

    String INSERT_USER = "insert into users(username,password) values(?,?) returning id ";

    String INSERT_STUDENT = "insert into students(name,user_id,level) values(?,?,?)";
    String CHECK_EMAIL = "select * from users where username = ?";

    String FETCH_STUDENT_ID = "select * from students where user_id = ? ";

    String FETCH_PROFESSOR_ID = "select * from professors where user_id = ? ";

}
