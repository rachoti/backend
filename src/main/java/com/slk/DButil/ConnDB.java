package com.slk.DButil;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnDB {
	
		
		public static Connection getConnection() throws Exception { 
	        String dname="com.mysql.cj.jdbc.Driver"; 
	        String url="jdbc:mysql://localhost:3306/abc_bank?useSSL=false"; 
	        String username="root"; 
	        String password="1234"; 
	        Class.forName(dname); 
	        Connection con = DriverManager.getConnection(url, username, password);  
			return con; 
	    } 
}
