import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.sql.ResultSetMetaData;
import java.sql.Connection;

public class DBResults
{
	//Fields
    private Connection myConnection;
    private Vector<String> columns;
    private ResultSetMetaData data; 
    
    //Constructor
    public DBResults(final Connection Connection) {
        myConnection = Connection;
    }
    
    //Methods
    //Using vector array class for the processing of user Queries
    public Vector<Vector<String>> runQuery(final String myQuery) throws SQLException {
    	
    	//***Constants***
        
    	//Object used for executing a static SQL statement and returning the results it produces.
        final Statement sqlStatement = myConnection.createStatement();
        
        final ResultSet resultsSet = sqlStatement.executeQuery(myQuery);
        data = resultsSet.getMetaData();
        
        final Vector<Vector<String>> results = new Vector<Vector<String>>();
        //***END of Constants 
              
        final int numOfColumns = data.getColumnCount();
        setColumns(numOfColumns, data);
        
        while (resultsSet.next()) {
            final Vector<String> row = new Vector<String>();
            
            for (int j = 1; j <= numOfColumns; ++j) {
                row.add(resultsSet.getString(j));
            }
            
            results.add(row);
        }
        
        return results;
    }
     
    //Getters & Setters 
    public Vector<String> getColumns() throws SQLException {
        return columns;
    }
    
    public void setColumns(final int numOfColumns, final ResultSetMetaData data) throws SQLException{
        columns = new Vector<String>();
        for (int i = 1; i <= numOfColumns; ++i) {
            columns.add(data.getColumnName(i));
        }
    }
    
    public int pushUpdate(final String userQuery) throws SQLException {
    	//Sends sql statement to the database for processing 
        final Statement sqlStatement = myConnection.createStatement();
        return sqlStatement.executeUpdate(userQuery);
    }  
}