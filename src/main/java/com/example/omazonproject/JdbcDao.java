package com.example.omazonproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcDao {
    public Connection databaseLink;

    public Connection getConnention(){
        String databaseName = "omazon_users";
        String databaseUSer = "SurentharRajamohan";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost:3306/omazon_users?zeroDateTimeBehavior=CONVERT_TO_NULL";

    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        databaseLink = DriverManager.getConnection(url,databaseUSer,databasePassword);
    }catch(Exception e){
        e.printStackTrace();
        e.getCause();
    }
    return databaseLink;
    }
    /*    public static void main(String[] args) {

    }
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/omazon_users?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String DATABASE_USERNAME = "omazon_users";
    private static final String DATABASE_PASSWORD = "";
    private static final String INSERT_QUERY = "INSERT INTO registration (Username, Password,Email) VALUES (?, ?, ?)";


    public void insertRecord(String Username, String Email, String Password) throws SQLException {

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
            .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, Username);
            preparedStatement.setString(2, Email);
            preparedStatement.setString(3, Password);

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}


*/

}



