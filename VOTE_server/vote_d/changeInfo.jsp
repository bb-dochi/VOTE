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
  String id = request.getParameter("id");
  String name = request.getParameter("name");
  String phone = request.getParameter("phone");
  String mail = request.getParameter("mail");
  String gender = request.getParameter("gender");
    
    try{
  		Class.forName("com.mysql.jdbc.Driver");
  		con=DriverManager.getConnection(url,"root","[wow1133]"); 
  		pstmt = con.prepareStatement("UPDATE Customer SET name =?, phone = ?, email =? , gender =? WHERE id = ?");
  		pstmt.setString(1,name);
  		pstmt.setString(2,phone);
      pstmt.setString(3,mail);
      pstmt.setString(4,gender);
      pstmt.setString(5,id);
 		  pstmt.executeUpdate();

     JSONArray arr = new JSONArray();

     
      JSONObject obj = new JSONObject();
      obj.put("result", true);

      if(obj!=null){
        arr.add(obj);
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