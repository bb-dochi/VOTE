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
	   String pw = request.getParameter("pw");
     JSONObject obj = new JSONObject();
     Customer cus = cusdao.Login(id,pw);
     if(cus.getId() != null){
      String userid = cus.getId();
      String userpw = cus.getPw();
      String username = cus.getName();
      String userphone = cus.getPhone();
      String usermail = cus.getEmail();
      String usergender = cus.getGender();
      String userfcm = cus.getUserFCM();

      obj.put("result", true);
      obj.put("userid", userid);
      obj.put("userpw", userpw);
      obj.put("username",username);
      obj.put("userphone", userphone);
      obj.put("usermail",usermail);
      obj.put("usergender",usergender);
      obj.put("userfcm",userfcm);
 
    }else{
      obj.put("result", false);
    }
    
      out.print(obj);

 } catch(Exception e){
  out.println(e.getMessage());
 }
 %>