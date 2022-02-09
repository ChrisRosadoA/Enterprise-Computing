<%@ page import="java.util.Vector" import="java.io.PrintWriter"
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CNT4714 Remote Database System</title>

<!-- SCRIPT FOR BUTTON TO REMOVE RESULTS TABLE -->
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>


<script type="text/javascript">
        function eraseData(){

	$("#table").remove();

} </script>


</head>
<body style="background-color: #FFFFFF">


<style>
body {

  background-image: url('https://pxwall.com/wp-content/uploads/2018/06/Wallpaper%20Christmas,%20New%20Year,%20snow,%20winter,%20snowman,%208k,%20Holidays%207241319520.jpg');
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-size: cover;
}

</style>



	<h1 style="text-align: center; color: red;">Welcome to the Fall 2021 Project 4 Enterprise Database System</h1>

	<h2 style="text-align: center; color: green;">A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat Container</h2>

	
	<div id="middle" style="text-align: center; color:blue; font-family:Times New Roman">


	<!-- FIRST DIVISION LINE -->
	<hr style="color: white;">



        <font size="+2"> You are now connected to the Project 4 Enterprise System database as a
			root user. <br> Please enter any valid SQL query or update
			command in the box below.</font> 

	<p><br>


		<form method="POST" action='ChrisWebDisplay' name="ChristianRosado">
			<textarea id="textarea" name="textarea" rows="4" cols="50"
				style="height: 300px; width: 600px; margin-left: 0px; margin-right: 0px;"></textarea>
                
 		<!-- BUTTONS-->
		<br />  
                     
			<input type="submit" value="Execute" name= "execute"  style= "font-size : 18px; background-color: darkgrey; color: lightgreen; width: 178px"/> 
			<input type="reset" value="Reset Form" name="clear" style= " font-size : 18px; background-color: darkgrey; color: red; width: 178px;"/>
			<input type="button" value="Clear Results" style= "font-size : 18px; background-color: darkgrey; color: yellow; width: 178px;"  onclick="javascript:eraseData();"/>
                      
                                                                                                          
		</form>
                 

                <p style="color: blue;"></p> 

               <!-- <font size="+2">All execution results will appear below this line:</font> *HIDDEN FOR NOW-->

		

	</div>

        <!-- SECOND DIVISION LINE -->
	<hr style="color: black;">

        <font size="+1">
 	<div id="footer" style="text-align: center; color: black; font-family:Calibri">
	<h3>Database Results:</h3>
	</font>


   

	<div id="footer" style="text-align: center; color: black;">
        <font size="+1">
		<%
		String myResult = "Run a Query";
		myResult = (String) request.getAttribute("results");
		PrintWriter mOut = response.getWriter();
		if (myResult == null) {
			myResult = "";
		}
		%>
		<%=myResult%> </font>

	</div>

</body>
</html>
