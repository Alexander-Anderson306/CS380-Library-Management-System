package com.cwu.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;

public class SQLHandler {

    private static Connection conn = null;

    /**
    * Load the MySQL JDBC driver.
    *
    * This method loads the MySQL JDBC driver class, which is required for
    * connecting to the database.
    *
    * @throws ClassNotFoundException if the driver class is not found
    */
    public static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        //TA SET USERNAME AND PW HERE
        try {
            if(conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "");
                IO.println("Connected to database.");
                return conn;
            } else {
                IO.println("Already connected to database.");
            }
        } catch (SQLException e) {
            // handle any errors
            IO.println("Failed to connect to database.");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return null;
    }

    /**
    * Closes the connection to the database. If the connection is already closed or null, prints a message to the console.
    * 
    * @param conn the connection to the database
    * @throws SQLException if there was an error closing the connection
    */
    public static void disconnect(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            } else {
                IO.println("Already disconnected from database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
