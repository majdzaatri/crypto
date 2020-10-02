package sample.server;

import java.sql.*;

public class DatabaseController {
    private static DatabaseController DBControllerInstance = new DatabaseController();
    static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String user) {
        username = user;
    }

    private static Connection connection;

    private DatabaseController() {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/crypto?serverTimezone=AST", "root", "password");
                this.connection = con;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

    }

    public static DatabaseController getInstance()
    {
        if(DBControllerInstance == null)
            DBControllerInstance = new DatabaseController();
        return DBControllerInstance;
    }



    public boolean verifyImage(String hash, String username) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM crypto.imageshash WHERE Username = '"+username+"' and HashCode = '"+hash+"'");
        System.out.println(hash);

        while(rs.next()) {
            if (rs.getInt(1) != 0)
                return true;

        }
        return false;
    }

    public boolean verifyUser(String username, String password) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM crypto.users WHERE Username = '"+username+"' and Password = '"+password+"'");
        setUsername(username);
        System.out.println(getUsername());
        while(rs.next()) {
            if (rs.getInt(1) != 0)
                return true;

        }
        return false;
    }
}


