package com.demodevops.app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class App {
  public static void main(String[] args) {
    connectToServer();
  }

  public static void connectToServer() {
    try(ServerSocket serverSocket = new ServerSocket(8080)) {
      while (true) { 
        String MYSQL_HOST = System.getenv("MYSQL_HOST"); 
        String MYSQL_USER = System.getenv("MYSQL_USER"); 
        String MYSQL_PASS = System.getenv("MYSQL_PASS"); 
        String MYSQL_DATABASE = System.getenv("MYSQL_DATABASE"); 
        String GIT_COMMIT = System.getenv("GIT_COMMIT"); 
        String TITLE = "Hola, Juan";


        String MYSQL_CONN_STATE = "danger";
        boolean dbdone = false;
        try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          dbdone = true;
        } catch (ClassNotFoundException e) {
          System.out.println("MySQL JDBC Driver not found !!");
          dbdone = false;
        }
        Connection connection = null;
        try {
          connection = DriverManager.getConnection("jdbc:mysql://"+ MYSQL_HOST +":3306/"+ MYSQL_DATABASE, MYSQL_USER, MYSQL_PASS);
          System.out.println("SQL Connection to database established!");
        } catch (SQLException e) {
          System.out.println("Connection Failed to "+ MYSQL_HOST);
          dbdone = false;
        } finally {
          try {
            if(connection != null)
              connection.close();
            System.out.println("Connection closed !!");
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      
        Socket connectionSocket = serverSocket.accept();

        //Create Input&Outputstreams for the connection
        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.write("HTTP/1.0 200 OK\r\n");
        serverPrintOut.write("Content-Type: text/html\r\n");
        serverPrintOut.write("Last-modified: " + new Date());
        serverPrintOut.write("\r\n\r\n");
        
        if(dbdone == false){
          MYSQL_CONN_STATE = "danger";
        } else {
          MYSQL_CONN_STATE = "success";
        }
        serverPrintOut.println("<html><head><link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css' integrity='sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T' crossorigin='anonymous'></head><body><div class='container'><div class='row justify-content-md-center'><h1>"+TITLE+"</h1></div><div class='row offset-md-3 col-md-6'><table class='table table-bordered'><thead><tr><th>Service</th><th>State</th></tr></thead><tbody><tr><td>Mysql</td><td class='table-"+MYSQL_CONN_STATE+" text-center'>"+MYSQL_CONN_STATE+"</td></tbody></table></div><div class='row justify-content-md-center'>Commit: "+GIT_COMMIT+"</div></div></body></html>");
        serverPrintOut.close();
        connectionSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
