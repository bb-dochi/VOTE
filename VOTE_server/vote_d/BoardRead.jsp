<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="vote.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.json.simple.*"%>
<jsp:useBean id="dao" class="vote.DAO"/>
<%
try{
request.setCharacterEncoding("UTF-8");
String category=request.getParameter("category");
int total = dao.count(category);
ArrayList<BoardData> list = dao.selectBoardData(category);
JSONObject jsonMain = new JSONObject();
JSONArray jArray = new JSONArray();

for(int i=0;i<total;i++)
{
	BoardData bd = list.get(i);
	JSONObject obj = new JSONObject();
	obj.put("index",bd.getIndex());		//0
	obj.put("category",bd.getCategory());	//1
	obj.put("id",bd.getId());		//2
	obj.put("title",bd.getTitle());		//3
	obj.put("memo",bd.getMemo());		//4
	obj.put("list1",bd.getList1());		//5
	obj.put("list2",bd.getList2());		//6
	obj.put("img1",bd.getImg1());		//7
	obj.put("img2",bd.getImg2());		//8
	obj.put("vote1",bd.getVote1());		//9
	obj.put("vote2",bd.getVote2());		//10
	obj.put("noname",bd.getNoname());	//11
	obj.put("alarm",bd.getAlarm());		//12
	
	jArray.add(i,obj);	
}

jsonMain.put("BoardData",jArray);
out.println(jsonMain);
out.flush();
}catch(Exception e){out.print(e);}

%>
