<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="vote.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.*"%>
<jsp:useBean id="cusdao" class="vote.CusDAO"/>
<%
  try{

  request.setCharacterEncoding("UTF-8");

	String id = request.getParameter("id");
  String autologin = request.getParameter("autologin");
  Boolean success = cusdao.setAutoLogin(id,autologin);
  JSONObject jsonMain = new JSONObject();
  jsonMain.put("success",success);
  out.println(jsonMain);

 } catch(Exception e){
  out.println(e.getMessage());
 }
 %>