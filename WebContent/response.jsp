<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="back.SearchQueryProcessor"%> 
 <%@ page import="back.PhaseSearcher"%>
 <%@ page import="back.Ranker"%>
 <%@ page import="back.Transfer"%>
  <%@ page import="back.Main"%>  
<%@page import="java.util.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.sql.*"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="org.tartarus.snowball.ext.PorterStemmer"%>
<%@page import="java.io.File"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results</title>
<link rel="stylesheet" type="text/css" href="css/response.css">
</head>
<body>
<% 
  final String USERNAME = "root";
  final String PASSWORD = "";
   final String CONN_STRING = "jdbc:mysql://localhost:3306/apt";
   
 final Connection connection;
  Statement stat;
try {
	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
    connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
    System.out.println("Database connected!");
    stat = connection.createStatement();
    String q =request.getParameter("query");
    //Main.main(null, q, 1);
    //request.setAttribute("query", q);
    //Transfer.setQuery(q);
    
    Transfer.start(connection,q);
    
} catch (SQLException ex2) {
    throw new IllegalStateException("Can't Open Connection", ex2);
}
Vector<String> results = Transfer.getResult();
%>
<div class="header">
     <img src="img/2.png" class="logo">
     <form action="response.jsp" method="get">
           <textarea type="text" name="query" required><%=request.getParameter("query")%></textarea>
           <input type="submit" value="">
     </form>
</div>
<div class="result">
<%
if (results.size()==0){
	
	 %>
	  <div class="r">
	      
	       <p>NO RESULTS FOUND.</p>
	  </div>
	  <%
}
else{
	
	Transfer.searchFiles(request.getParameter("query"),connection);
	Vector<String> statments = Transfer.getStatment();
for (int i = 0; i < results.size(); i++){
  %>
  <div class="r">
       <a href="<%= results.get(i)%>"><h2><%= results.get(i)%></h2></a>
       
      <% if( statments.size() !=0){ %>
       <p><%=statments.get(i)%></p>
       <%} %>
  </div>
  <%
      }
}
%>
</div>
</body>
</html>