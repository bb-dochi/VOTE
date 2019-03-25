<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.json.simple.*" %>
<%
	Connection con = null;  
  String url = "jdbc:mysql://ec2-13-125-65-179.ap-northeast-2.compute.amazonaws.com:3306/vote";
	PreparedStatement  pstmt = null;
	ResultSet rs = null;
	String sql = null;
	String returns = null;

	String fcm = request.getParameter("fcm");

    
    try{
  		Class.forName("com.mysql.jdbc.Driver");
  		con=DriverManager.getConnection(url,"root","[wow1133]"); 
  		pstmt = con.prepareStatement("select * from Customer where userFCM = ?");
  		pstmt.setString(1,fcm);
 		   rs = pstmt.executeQuery();

     JSONArray arr = new JSONArray();

     while(rs.next()) {

      String userid = rs.getString("id");
      String userpw = rs.getString("pw");
      String username = rs.getString("name");
      String userphone = rs.getString("phone");
      String usermail = rs.getString("email");
      String usergender = rs.getString("gender");
      String userfcm = rs.getString("userFCM");
      int autologin = rs.getInt("autologin");

      JSONObject obj = new JSONObject();
      obj.put("result", true);
      obj.put("userid", userid);
      obj.put("userpw", userpw);
      obj.put("username",username);
      obj.put("userphone", userphone);
      obj.put("usermail",usermail);
      obj.put("usergender",usergender);
      obj.put("userfcm", userfcm);
      obj.put("auto", autologin);

      if(obj!=null){
        arr.add(obj);
       }
     }
     out.print(arr);

 } catch(SQLException se){
  out.println(se.getMessage());
 }finally {
  if(rs != null) rs.close();
     if(pstmt != null) pstmt.close();
     if(con != null) con.close();
 }
 %>