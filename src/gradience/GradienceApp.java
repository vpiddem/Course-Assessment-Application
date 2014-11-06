package gradience;

import database.DataBaseConnection;
import view.Login;

/**
 *
 * @author Vikas
 */
public class GradienceApp implements GradienceAppConstants {

    public static void main(String[] args) {
        db = new DataBaseConnection(DB_DRIVER, DB_URL, DB_USERNAME, DB_PASSWORD);
        db.makeDatabaseConnection();
//        db.CreateDatabaseTables();
        Login loginWin = new Login(db);
        loginWin.initComponents();
    }

    private static DataBaseConnection db;
   
}
