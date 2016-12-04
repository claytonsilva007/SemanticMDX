package br.com.ontologia.dw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDW {

	private static ConnectionDW dbIsntance;
	private static Connection con ;

	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "foodmart";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "root";

	private ConnectionDW() {

	}

	public static ConnectionDW getInstance(){
		if(dbIsntance==null){
			dbIsntance= new ConnectionDW();
		}
		return dbIsntance;
	}

	public  Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		if(con==null){
			try {
				Class.forName(driver).newInstance();
		        con = DriverManager.getConnection(url + dbName, userName,password);
			} catch (SQLException ex) {
				Logger.getLogger(ConnectionDW.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return con;
	}
}
