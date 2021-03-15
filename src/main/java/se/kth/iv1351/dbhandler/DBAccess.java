package se.kth.iv1351.dbhandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class provides access to the database.
 */
public class DBAccess {
    private static final String URL = "jdbc:postgresql://localhost:5432/soundgood_music_school";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "ozzi7315";
    private Connection connection;
    
    /** 
     * Creates a new instance.
     */
    public DBAccess() {
        accessDB();
    }
    
    /**
     * Sets up access to the database.
     */
    private void accessDB(){
        try {
            this.connection = createConnection();
        } catch(SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Sets up a connection to the database.
     * @return the connection from the driver manager.
     * @throws SQLException if failed to connect to the database.
     * @throws ClassNotFoundException if the class is not found.
     */
    private Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL,
        USERNAME, PASSWORD);
    }
    
    /**
     * @return the connection to the database.
     */
    Connection getConnection() {
        return this.connection;
    }
    
}
