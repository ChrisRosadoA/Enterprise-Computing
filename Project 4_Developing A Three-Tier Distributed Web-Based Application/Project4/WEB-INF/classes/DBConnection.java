import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;


//Fields
public class DBConnection
{
    //Fields
    private String username;
    private String password;
    private String URL;
    private Connection connectionFlag;
    
    //Class
    public DBConnection() {
    	
    	Properties properties = new Properties();
   	 	FileInputStream filein = null;
    	
    	try {
     		filein = new FileInputStream("C:\\Program Files\\Apache Software Foundation\\Tomcat 10.0\\webapps\\Project4\\WEB-INF\\lib\\db.properties.txt");	                          
     		properties.load(filein);
            
     		//Set values for Fields 
     		this.setURL((properties.getProperty("MYSQL_DB_URL")));
     		this.setUsername((properties.getProperty("MYSQL_DB_USERNAME")));
     		this.setPassword((properties.getProperty("MYSQL_DB_PASSWORD")));
     					        	  		   		     		
     		}   
     		catch (IOException e) {
     			e.printStackTrace();
     		}
    }  
    
    
    //METHODS
    public void EstablishConnection() throws SQLException, ClassNotFoundException { 
        	
    	Class.forName("com.mysql.cj.jdbc.Driver");
    	connectionFlag = DriverManager.getConnection(this.getURL(), this.getUsername(), this.getPassword());
    					
    }
    
    public void closeConnection() throws SQLException {
        this.connectionFlag.close();
    }
    
    public Connection getConnection() {
        return this.connectionFlag;
    }

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}