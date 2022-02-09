// A TableModel that supplies ResultSet data to a JTable.
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import java.util.Properties;
//import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource; 


// ResultSet rows and columns are counted from 1 and JTable 
// rows and columns are counted from 0. When processing 
// ResultSet rows or columns for use in a JTable, it is 
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is 
// ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class ResultSetTableModel extends AbstractTableModel 
{
//****Given Variables****
   private Connection connection;
   private Statement statement;
   private ResultSet resultSet;
   private ResultSetMetaData metaData;
   private int numberOfRows;
   
   private Statement rootStatement; //TEST

   // keep track of database connection status ***GIVEN
   private boolean connectedToDatabase = false;
   
   // constructor passes in connection and query string
   // determines number of rows
   public ResultSetTableModel( String query , Connection connection) //Modified from given 
      throws SQLException, ClassNotFoundException, IOException
   {         
	   //Check Connection 	   	   
	   try {
	    	
   	        this.connection = connection;
   	             
   	        if(!this.connection.isClosed()) 
   	        {
   	        	connectedToDatabase = true;
	
            // create Statement to query database
            statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
            connectedToDatabase = true;
                  
        	if(query.contains("select")){
				try 
				{
					setQuery(query);
				} catch (IllegalStateException | SQLException e) 
				
				{
					 JOptionPane.showMessageDialog( null, 
		                        e.getMessage(), "Database error", 
		                        JOptionPane.ERROR_MESSAGE );
					e.printStackTrace();
				}
			}
			else
			{
				try 
				{
					setUpdate(query);
				} catch (IllegalStateException | SQLException e) 
				{
					 JOptionPane.showMessageDialog( null, 
		                        e.getMessage(), "Database error", 
		                        JOptionPane.ERROR_MESSAGE );
					e.printStackTrace();
				}
			}

   	        } 
   	        
	  }catch ( SQLException sqlException) 
	   
      {
         sqlException.printStackTrace();
         System.exit( 1 );
      } 
	   
   } // end constructor ResultSetTableModel

 
   //get class that represents column type
   public Class getColumnClass( int column ) throws IllegalStateException
   {
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // determine Java class of column
      try 
      {
         String className = metaData.getColumnClassName( column + 1 );
         
         // return Class object that represents className
         return Class.forName( className );
      } // end try
      catch ( Exception exception ) 
      {
         exception.printStackTrace();
      } // end catch
      
      return Object.class; // if problems occur above, assume type Object
   } // end method getColumnClass

   // get number of columns in ResultSet
   public int getColumnCount() throws IllegalStateException
   {   
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // determine number of columns
      try 
      {
         return metaData.getColumnCount(); 
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return 0; // if problems occur above, return 0 for number of columns
   } // end method getColumnCount

   // get name of a particular column in ResultSet
   public String getColumnName( int column ) throws IllegalStateException
   {    
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // determine column name
      try 
      {
         return metaData.getColumnName( column + 1 );  
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string for column name
   } // end method getColumnName

   // return number of rows in ResultSet
   public int getRowCount() throws IllegalStateException
   {      
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );
 
      return numberOfRows;
   } // end method getRowCount

   // obtain value in particular row and column
   public Object getValueAt( int row, int column ) 
      throws IllegalStateException
   {
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // obtain a value at specified ResultSet row and column
      try 
      {
		   resultSet.next();  /* fixes a bug in MySQL/Java with date format */
         resultSet.absolute( row + 1 );
         return resultSet.getObject( column + 1 );
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
      } // end catch
      
      return ""; // if problems, return empty string object
   } // end method getValueAt
   
   // set new database query string
   public void setQuery( String query ) 
      throws SQLException, IllegalStateException 
   {
      // ensure database connection is available
      if ( !connectedToDatabase ){
         throw new IllegalStateException( "Not Connected to Database" );
      }

      // specify query and execute it
      resultSet = statement.executeQuery( query );

      // obtain meta data for ResultSet
      metaData = resultSet.getMetaData();

      // determine number of rows in ResultSet
      resultSet.last();                   // move to last row
      numberOfRows = resultSet.getRow();  // get row number      
      
      
      //*****UPDATE OPERATIONS LOG DB: as a root user client: +1 to query count*****
      //Connection to the  database operations log     
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setUser("root");
      dataSource.setPassword("Wolfy218!");
      dataSource.setUrl("jdbc:mysql://localhost:3306/operationslog");
      
      Connection rootConnection = dataSource.getConnection();
      
      //Testing Connection 
      System.out.println("Operations Log Database Connected: Ready to Update Queries!\n");
      
      //Use the connection object to CREATE a STATEMENT OBJECT on the connection
      //Using the statement object send an update command to the operations log db to increment number of queries
      
      rootStatement = rootConnection.createStatement();
      
      
      //Run command: update operationscount set num_queries = num_queries + 1;
       rootStatement.executeUpdate("update operationscount set num_queries = num_queries + 1");  
      
      
      //Close connection to operations log database
      rootConnection.close();
      
      //Testing Query Increment
      //System.out.println("+ 1 Query\n");
      
      
      //*****END OF update operationslog database*****
      
      // notify JTable that model has changed
      fireTableStructureChanged();
   } // end method setQuery


// set new database update-query string
   public void setUpdate( String query ) throws SQLException, IllegalStateException {
	    int res;
      // ensure database connection is available
      if ( !connectedToDatabase ) 
         throw new IllegalStateException( "Not Connected to Database" );

      // specify update and execute it
      res = statement.executeUpdate( query );
      
      //*****UPDATE OPERATIONS LOG DB: as a root user client: +1 to update count*****
      //Connection to the  database operations log     
      MysqlDataSource dataSource = new MysqlDataSource();
      dataSource.setUser("root");
      dataSource.setPassword("Wolfy218!");
      dataSource.setUrl("jdbc:mysql://localhost:3306/operationslog");
      
      Connection rootConnection = dataSource.getConnection();
      
      //Testing Connection
      System.out.println("Operations Log Database Connected: Ready to Update!\n");
      
      //Use the connection object to CREATE a STATEMENT OBJECT on the connection
      //Using the statement object send an update command to the operations log db to increment number of updates
      
      rootStatement = rootConnection.createStatement();
      
      
      //Run command: update operationscount set num_updates = num_updates + 1;
       rootStatement.executeUpdate("update operationscount set num_updates = num_updates + 1");  
      
      
      //Close connection to operations log database
      rootConnection.close();
         
      // notify JTable that model has changed
      fireTableStructureChanged();
   } // end method setUpdate
         
}  // end class ResultSetTableModel




