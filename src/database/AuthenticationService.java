package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.DuplicateEmailException;
import gradience.GradienceAppConstants;
import beans.LoggedInUser;
import beans.Student;

public class AuthenticationService implements GradienceAppConstants {

    private Connection connection;

    DataBaseConnection db;

    public AuthenticationService(DataBaseConnection db) {
        this.db = db;
    }


    /**
     *
     * @param email
     * @param password
     * @return role of the User or -1 if such user doesn't exist
     */
    public int authenticate(String email, String password) {
        return db.getRoleOfLoggedInUser(email,password);
    }

    public void register(Student student) throws DuplicateEmailException {
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(CHECK_EMAIL);
            ps.setString(1, student.getEmail());

            if (ps.executeQuery().next()) {
                throw new DuplicateEmailException();
            }

            ps = connection.prepareStatement(INSERT_USER);
            ps.setString(1, student.getEmail());
            ps.setString(2, student.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LoggedInUser.userId = rs.getInt("id");
                ps = connection.prepareStatement(INSERT_STUDENT);
                ps.setString(1, student.getName());
                ps.setInt(2, LoggedInUser.userId);
                ps.setInt(3, LEVEL_GRAD);
                ps.execute();
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

        }

    }

    /*	*/    /**
     * This class is for testing purpose only.
     *
     * @param args
     *//*
     public static void main(String []args ){
     Student s = new Student();
     s.setEmail("mohansammeta2@gmail.com");
     s.setPassword("abcabc123");
     s.setName("mohan");
     DataBaseConnection dc = new DataBaseConnection(DB_DRIVER, DB_URL, DB_USERNAME, DB_PASSWORD);
     dc.makeDatabaseConnection();
     AuthenticationService as = new AuthenticationService();
     try {
     as.register(s);
     System.out.println("Logged in user : "+LoggedInUser.userId);
     System.out.println("User authenticated : "+as.authenticate("mohansammeta2@gmail.com","abcabc123"));
     } catch (DuplicateEmailException e) {
     // TODO Auto-generated catch block
     System.out.println("expected");
     e.printStackTrace();
     }
		
     }*/

}
