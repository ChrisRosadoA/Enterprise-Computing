/*Name: Christian D. Rosado Arroyo
 Course: CNT 4714 – Fall 2021
 
 Assignment title: Project 4 - CNT 4714 – Project Four – Fall 2021

 Date: Thursday December 2, 2021 by 11:59 pm
*/
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.mysql.cj.jdbc.MysqlDataSource;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileInputStream;
import java.io.IOException;

import java.util.Map;
import java.util.Properties;


@WebServlet(urlPatterns = { "/ChrisWebDisplay" })
public class ChrisWebDisplay extends HttpServlet
{
    
	//FIELDS
    private static final long serialVersionUID = 1L;
    private DBConnection dbConnection; 
    private DBResults dbResult;
    
    
    //HTML Messages:
    //Java Generating HTML ERROR
    private String htmlError(final String htmlMessage) {
        String htmlStr = "<div id='table' style='margin:auto;text-align:center;width: 630px;'><table border='1' style='margin: auto;background-color: red;'><tr>";
        htmlStr = String.valueOf(htmlStr) + "<td>Error executing the SQL statement:<br/>" + htmlMessage + "</td>";
        return htmlStr;
    }
    
    private String htmlSuccess(final int columns, final int suppliers) {
    	String htmlStr = "<div id='table' style='margin:auto;text-align:center;width: 630px;'><table border='1' style='margin: auto;background-color: green;'><tr>";
    	htmlStr = String.valueOf(htmlStr) + "<td>The SQL statement completed successfully:<br/>" + "Business Logic Detected! - Updating Suppliers Status</td>";
    	return htmlStr;
    }
    
    private String htmlSuccess(final int columns) {
        String htmlStr = "<div id='table' style='margin:auto;text-align:center;width: 630px;'><table border='1' style='margin: auto;background-color: green;'><tr>";
        htmlStr = String.valueOf(htmlStr) + "<td>The SQL statement completed successfully:<br/>" + "Business Logic Not Triggered!</td>";
        return htmlStr;
    }    
                     
    //DO GET Method 
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final Map<?, ?> parameterMap = request.getParameterMap();
        final String jspDisplay = "/index.jsp";
        
        //If 'Execute' statement then trigger connection to database via db Properties File 
        if (parameterMap.containsKey("execute")){
        
        	dbConnection = new DBConnection();
            dbResult = new DBResults(this.dbConnection.getConnection());
                 	    
        }
        
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(jspDisplay);
        requestDispatcher.forward((ServletRequest)request, (ServletResponse)response);
    }
    
    //DO POST Method 
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    	Vector<Vector<String>> results = new Vector<Vector<String>>();
        Vector<String> col = new Vector<String>();
        final String jspDisplay = "/index.jsp";
        String htmlMessage;
        String userQuery = request.getParameter("textarea");
            
        //Generates Connection Objection 
        this.dbConnection = new DBConnection();
                                       
        try {
            this.dbConnection.EstablishConnection();
            
            //Holds Connection 
            this.dbResult = new DBResults(this.dbConnection.getConnection());
        }
        
        
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        
       
        
        if (userQuery.toLowerCase().startsWith("select")){
        	
            try {
                results = (Vector<Vector<String>>)this.dbResult.runQuery(userQuery);
                col = (Vector<String>)this.dbResult.getColumns();
                htmlMessage = this.genHTML(results, col);
            }
            
            catch (SQLException e3) {
                htmlMessage = this.htmlError(e3.getMessage());
            }
        }
        
        else{
            try {
            	boolean supplierUpdate = false;
            	boolean shipmentUpdate = true;
                String snum = "";
                boolean temp = false;
                String tmpQuery = "";
                
                if(userQuery.toLowerCase().contains("update shipments")){  
                	
                	//Removes '(' & ')' from the query 
                	if(userQuery.contains(")") && userQuery.contains("(")){
                     	int first = userQuery.indexOf("("); //final?
                        int last = userQuery.indexOf(")");              
                        tmpQuery = userQuery.substring(first + 1, last);
                    }
                   	
                	else{
                    	tmpQuery = userQuery;
                    }
                	
                    tmpQuery = tmpQuery.replaceAll("'", "");
                    tmpQuery = tmpQuery.replaceAll(" ", "");
                                                                      
                    final String[] newString = tmpQuery.split(",");//parsed 
                    String[] strArray;
                    
                    int size = (strArray = newString).length;
                    
                    for (int j = 0; j < size; ++j) {
                        final String str = strArray[j];
                        
                        try {
                            if (Integer.valueOf(str) >= 100) {
                                supplierUpdate = true;
                                
                            }                           
                           /* else {
                            	shipmentUpdate = true; //never reaching?
                            }*/                                                       
                        }
                                                                                             
                        catch (NumberFormatException e5) {
                            if (str.startsWith("S")) {
                                snum = str;
                            }
                        }
                        
                        if(supplierUpdate == false) {
                        	shipmentUpdate = true;	
                        }
                        
                    }                                      
               }
                
                if(userQuery.toLowerCase().contains("insert into shipments")) {
                	
                	//Removes '(' & ')' from the query 
                	if(userQuery.contains(")") && userQuery.contains("(")){
                     final	int first = userQuery.indexOf("("); //final?
                      final  int last = userQuery.indexOf(")");              
                        tmpQuery = userQuery.substring(first + 1, last);
                    }
                   	
                	else{
                    	tmpQuery = userQuery;
                    }
                	
                    tmpQuery = tmpQuery.replaceAll("'", "");
                    tmpQuery = tmpQuery.replaceAll(" ", "");
                                                                           
                    final String[] newString = tmpQuery.split(",");
                    String[] strArray;
                    
                    int size = (strArray = newString).length;//TEST Line
                   
                    for (int i = 0; i < size; ++i) {
                        final String str = strArray[i];
                        try {
                            if (Integer.valueOf(str) >= 100) {//******
                                supplierUpdate = true;
                            }                        
                          
                        }
                        catch (NumberFormatException e5) {
                            if (str.startsWith("S")) {
                                snum = str;
                            }
                        }
                        
                    }	
                	
                	
                }
                
                //Business Logic 
                //Triggered: if update shipment && quantity >= 100
                if (supplierUpdate) {
                    final Vector<Vector<String>> secondTMP = (Vector<Vector<String>>)this.dbResult.runQuery("select DISTINCT(suppliers.snum) from suppliers join shipments on suppliers.snum = shipments.snum and shipments.quantity >= 100");
                    String input = "";
                    for (final Vector row : secondTMP) {
                        if (input == "") {
                            input = String.valueOf(input) + "'" + row.get(0) + "'";
                        }
                        else {
                            input = String.valueOf(input) + ",'" + row.get(0) + "'";
                        }
                    }
                    if (snum != "") {
                        input = String.valueOf(input) + ",'" + snum + "'";
                    }
                    
                    final String updateString = "UPDATE suppliers set status = (status + 5) where snum IN (" + input + ")";
                    htmlMessage = this.htmlSuccess(this.dbResult.pushUpdate(userQuery), this.dbResult.pushUpdate(updateString)); //Displaying Logic Triggered
                }
                
                
                //Triggered: else if for update shipments 
                else if(userQuery.toLowerCase().contains("update shipments") && supplierUpdate!=true ){
                     //Execute Logic
                     final String updateString = "update suppliers set status = status + 5 where snum in (select snum from shipments where quantity > 99)";
                     htmlMessage = this.htmlSuccess(this.dbResult.pushUpdate(userQuery), this.dbResult.pushUpdate(updateString));
                     shipmentUpdate = false;
                }
                
                //Business Logic NOT Triggered 
                else {
                    htmlMessage = this.htmlSuccess(this.dbResult.pushUpdate(userQuery));
                }
                                                                                           
            }
            
            
            catch (SQLException e3) {
                htmlMessage = this.htmlError(e3.getMessage());
            }
            
        }
        
        
        request.setAttribute("results", (Object)htmlMessage);
                   
        
        final RequestDispatcher dispatcher = request.getRequestDispatcher(jspDisplay);
        dispatcher.forward((ServletRequest)request, (ServletResponse)response);
        
        try {
            this.dbConnection.closeConnection();
        }
        catch (SQLException e4) {
            e4.printStackTrace();
        }
    }
    
    //GENERATE HTML Table
    private String genHTML(final Vector<Vector<String>> resultsOutput, final Vector<String> col) {
        String htmlStr = "<div id='table' style='margin:auto;text-align:center;width: 626px;'><table border='1' style='margin: auto;'><tr>";
        for (final String columns : col) {
            htmlStr = String.valueOf(htmlStr) + "<td style='background-color: red;'>" + columns + "</td>";
        }
        htmlStr = String.valueOf(htmlStr) + "</tr>";
        for (final Vector row : resultsOutput) {
        	
        	//Changing Colors of Row Cells
            htmlStr = String.valueOf(htmlStr) + "<tr style='background-color: white;'>";
           
            for (int i = 0; i < row.size(); ++i) {
                htmlStr = String.valueOf(htmlStr) + "<td>" + row.get(i) + "</td>" ;             
            }      
            
            htmlStr = String.valueOf(htmlStr) + "</tr>";
        }
        return htmlStr = String.valueOf(htmlStr) + "</table></div>";
    }
}