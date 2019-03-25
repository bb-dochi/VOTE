<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="vote.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.*"%>
<jsp:useBean id="cusdao" class="vote.CusDAO"/>

<%
	request.setCharacterEncoding("UTF-8");

	try{
	String id = request.getParameter("id");
	String pw = request.getParameter("pw");
	String name = request.getParameter("name"); 
	String gender = request.getParameter("gender");
	String phone = request.getParameter("phone");
	String mail = request.getParameter("mail");
	String fcm = request.getParameter("fcm");
	
	Boolean success =  cusdao.registerCustomer(id,pw,name,gender,phone,mail,fcm);   	
  	
 		 JSONObject jsonMain = new JSONObject();
 		 jsonMain.put("success",success);

 		out.println(jsonMain);
 	} catch(Exception se){
 		 out.println(se.getMessage());
 	}	
%>
