<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, vote.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="admin_board/board_css.css" rel="stylesheet" type="text/css">

<title>admin page</title>
<style type="text/css">
	#Memupd{ width: 300px; }
	#Memupd label{ display:block;
		 		   width: 100px;
			       float:left;
	}
</style>
<script language="JavaScript">
	
	function ShowTabex(val){
	   for (i=0; i<2; i++) {
	     var tb = document.getElementById('tab_' + i);
	     var st = document.getElementById('st_' + i);
	  	   if (i != val){
	 	   	 	tb.style.display = "none";
	 	   		st.style.background = "#747474";
	  	   } 
	     else{
	    	 tb.style.display = "block";
	    	 st.style.background = "red";
	     }  
	   }
	}
	function ShowTabex2(val){
		   for (i=0; i<6; i++) {
		     var tb = document.getElementById('tab2_' + i);
		     var st = document.getElementById('st2_' + i);
		     if (i != val){
		 	   	 	tb.style.display = "none";
		 	   		st.style.background = "#ddd";
		 	   		st.style.color = "#000000";
		  	   }
		     else{
		    	 tb.style.display = "block";
		    	 st.style.background = "#000000";
		    	 st.style.color = "#ffffff";
		   }
		 }
	}
	function ShowTabex3(val){
		   for (i=0; i<6; i++) {
		     var tb = document.getElementById('tab3_' + i);
		     var st = document.getElementById('st3_' + i);
		     if (i != val){
		 	   	 	tb.style.display = "none";
		 	   		st.style.background = "#ddd";
		 	   		st.style.color = "#000000";
		  	   }
		     else{
		    	 tb.style.display = "block";
		    	 st.style.background = "#000000";
		    	 st.style.color = "#ffffff";
		   }
		 }
	}
	function checkDel(){
		return confirm("삭제하겠습니까?");
			
	}
</script>

</head>
<header><a href="admin.jsp"><img src="imglogo.jpg" width="166px" height="47px"></a></header>
<body>
<%
	String loginName=(String)session.getAttribute("loginad");
 	boolean login= loginName==null ? false:true;
 	if(login){%>
 		<form action="adminLogout.jsp" method="get">
			<span><input type="submit" value="logout" style="float:right"></span>
<span style="float:right"><%=loginName%>님, 환영합니다</span>

		</form>

	 		<div style="text-align: left;">
	   <div style="border: 0px solid #ddd;">
	   
	   <span id="st_0" onclick="ShowTabex('0')" style="background:red; color:#ffffff;  padding: 0pt 5px; cursor: pointer;">회원 관리</span>
	   <span id="st_1" onclick="ShowTabex('1')" style="background:#747474; color:#ffffff;  padding: 0pt 5px; cursor: pointer;">게시물 관리</span>

	   </div>
	 </div>
	 <div style="text-align: center;">
	   <div id="tab_0" style="width: 100%; display: block;">
	    <br>
		<br>
		<h3>회원정보</h3>
		<table bordercolor="#ed1e24" border="1" style="margin-left:auto; margin-right:auto">
		<tr>
		   <td><strong>ID</strong></td>
		   <td><strong>PASSWD</strong></td>
		   <td><strong>NAME</strong></td>
		   <td><strong>GENDER</strong></td>
		   <td><strong>PHONE</strong></td>
		   <td><strong>EMAIL</strong></td>
		</tr>

		<jsp:useBean id="Mem" class="vote.CusDAO" scope="page" />	
		<% ArrayList<Customer> memlist = Mem.allCustomerData();
			int counter = memlist.size();
			for(int i=0; i<counter; i++){
	   			Customer ad = (Customer)memlist.get(i); %>
		<tr>
		<td><%=ad.getId()%><input type="hidden" id="idxId" value="<%=ad.getId()%>"></td>
		<td><%=ad.getPw()%></td>
		<td><%=ad.getName()%></td>
		<td><%=ad.getGender()%></td>
		<td><%=ad.getPhone()%></td>
		<td><%=ad.getEmail()%></td>
		<td><a href="/vote_d/admin/adMemDelete.jsp?id=<%=ad.getId()%>" onclick="return checkDel();">삭제</a></td>
		<td><a href="/vote_d/admin/adMemModify.jsp?id=<%=ad.getId()%>">수정 </a></td>
		<% } %>
		</tr>
		</table>
	
		</div>
		<br/>
		<br/>
	
		 
	   </div>

	   <div id="tab_1" style="width: 100%; display: none;">
			<div style="text-align: left;">
				<hr color=#ffffff>
			 <div style="border: 0px solid #ddd;">
  
			   <span id="st2_0" onclick="ShowTabex2('0')" style="background:#000000; color:#ffffff; padding: 0pt 5px; cursor: pointer;">사회</span>
			   <span id="st2_1" onclick="ShowTabex2('1')" style="background:#ddd; padding: 0pt 5px; cursor: pointer;">쇼핑</span>
			   <span id="st2_2" onclick="ShowTabex2('2')" style="background:#ddd; padding: 0pt 5px; cursor: pointer;">일상</span>
			  
			   </div>
			</div>
		 	<div style="text-align: center;">
			   <div id="tab2_0" style="width: 100%; display: block; ">
			  	
<br>
				<br>	  
					
					  <h3>사회 게시판</h3>
					<table bordercolor="#ed1e24" border="1" style="margin-left:auto; margin-right:auto">
		<tr>
		   <td><strong>index</strong></td>
		   <td><strong>category</strong></td>
		   <td><strong>id</strong></td>
		   <td><strong>title</strong></td>
		   <td><strong>memo</strong></td>
		   <td><strong>list1</strong></td>
		   <td><strong>list2</strong></td>
		   <td><strong>img1</strong></td>
		   <td><strong>img2</strong></td>		   
		   <td><strong>vote1</strong></td>
		   <td><strong>vote2</strong></td>
		   <td><strong>noname</strong></td>
		   <td><strong>alarm</strong></td>
		</tr>

		<jsp:useBean id="dao" class="vote.DAO" scope="page" />	
		<% ArrayList<BoardData> boardlist1 = dao.selectBoardData("food");
			int bdCount1 = boardlist1.size();
			for(int i=0; i<bdCount1; i++){
	   			BoardData bd = (BoardData)boardlist1.get(i); %>
		<tr>
		<td><%=bd.getIndex()%><input type="hidden" id="idxId" value="<%=bd.getIndex()%>"></td>
		<td><%=bd.getCategory()%></td>
		<td><%=bd.getId()%></td>
		<td><%=bd.getTitle()%></td>
		<td><%=bd.getMemo()%></td>
		<td><%=bd.getList1()%></td>
		<td><%=bd.getList2()%></td>
		<td><%=bd.getImg1()%></td>
		<td><%=bd.getImg2()%></td>
		<td><%=bd.getVote1()%></td>
		<td><%=bd.getVote2()%></td>
		<td><%=bd.getNoname()%></td>
		<td><%=bd.getAlarm()%></td>
		<td><a href="/vote_d/admin/adBoardDelete.jsp?idx=<%=bd.getIndex()%>" onclick="return checkDel();">삭제</a></td>
		<td><a href="/vote_d/admin/adBoardModify.jsp?idx=<%=bd.getIndex()%>">수정 </a></td>
		<% } %>
		</tr>
		</table>
			   </div>
			   <div id="tab2_1" style="width: 100%; display: none;">
			    	  <br>
					  <br>
					  <h3>쇼핑 게시판</h3>
					  <table bordercolor="#ed1e24" border="1" style="margin-left:auto; margin-right:auto">
		<tr>		   <td><strong>index</strong></td>
		   <td><strong>category</strong></td>
		   <td><strong>id</strong></td>
		   <td><strong>title</strong></td>
		   <td><strong>memo</strong></td>
		   <td><strong>list1</strong></td>
		   <td><strong>list2</strong></td>
		   <td><strong>img1</strong></td>
		   <td><strong>img2</strong></td>		   
		   <td><strong>vote1</strong></td>
		   <td><strong>vote2</strong></td>
		   <td><strong>noname</strong></td>
		   <td><strong>alarm</strong></td>
		</tr>

		<% ArrayList<BoardData> boardlist2 = dao.selectBoardData("shop");
			int bdCount2 = boardlist2.size();
			for(int i=0; i<bdCount2; i++){
	   			BoardData bd = (BoardData)boardlist2.get(i); %>
		<tr>
		<td><%=bd.getIndex()%><input type="hidden" id="idxId" value="<%=bd.getIndex()%>"></td>
		<td><%=bd.getCategory()%></td>
		<td><%=bd.getId()%></td>
		<td><%=bd.getTitle()%></td>
		<td><%=bd.getMemo()%></td>
		<td><%=bd.getList1()%></td>
		<td><%=bd.getList2()%></td>
		<td><%=bd.getImg1()%></td>
		<td><%=bd.getImg2()%></td>
		<td><%=bd.getVote1()%></td>
		<td><%=bd.getVote2()%></td>
		<td><%=bd.getNoname()%></td>
		<td><%=bd.getAlarm()%></td>
		<td><a href="/vote_d/admin/adBoardDelete.jsp?idx=<%=bd.getIndex()%>" onclick="return checkDel();">삭제</a></td>
		<td><a href="/vote_d/admin/adBoardModify.jsp?idx=<%=bd.getIndex()%>">수정 </a></td>
		<% } %>
		</tr>
		</table>
			   </div>
			   <div id="tab2_2" style="width: 100%; display: none;">
			    	  <br>
					  <br>
					  <h3>일상 게시판</h3>
					 <table bordercolor="#ed1e24" border="1" style="margin-left:auto; margin-right:auto">
		<tr>		   <td><strong>index</strong></td>
		   <td><strong>category</strong></td>
		   <td><strong>id</strong></td>
		   <td><strong>title</strong></td>
		   <td><strong>memo</strong></td>
		   <td><strong>list1</strong></td>
		   <td><strong>list2</strong></td>
		   <td><strong>img1</strong></td>
		   <td><strong>img2</strong></td>		   
		   <td><strong>vote1</strong></td>
		   <td><strong>vote2</strong></td>
		   <td><strong>noname</strong></td>
		   <td><strong>alarm</strong></td>
		</tr>

		<% ArrayList<BoardData> boardlist3 = dao.selectBoardData("day");
			int bdCount3 = boardlist3.size();
			for(int i=0; i<bdCount3; i++){
	   			BoardData bd = (BoardData)boardlist3.get(i); %>
		<tr>
		<td><%=bd.getIndex()%><input type="hidden" id="idxId" value="<%=bd.getIndex()%>"></td>
		<td><%=bd.getCategory()%></td>
		<td><%=bd.getId()%></td>
		<td><%=bd.getTitle()%></td>
		<td><%=bd.getMemo()%></td>
		<td><%=bd.getList1()%></td>
		<td><%=bd.getList2()%></td>
		<td><%=bd.getImg1()%></td>
		<td><%=bd.getImg2()%></td>
		<td><%=bd.getVote1()%></td>
		<td><%=bd.getVote2()%></td>
		<td><%=bd.getNoname()%></td>
		<td><%=bd.getAlarm()%></td>
		<td><a href="/vote_d/admin/adBoardDelete.jsp?idx=<%=bd.getIndex()%>" onclick="return checkDel();">삭제</a></td>
		<td><a href="/vote_d/admin/adBoardModify.jsp?idx=<%=bd.getIndex()%>">수정 </a></td>
		<% } %>
		</tr>
		</table>
			   </div>
			   <div id="tab2_3" style="width: 100%; display: none;">
			  		
			 </div>
			     
	   </div>
	 </div>
<% }else{
	%>
		<script>
		alert("로그인이 필요합니다.");
		location.href="adminLogin.jsp";
		</script>	
<%}%>    
</body>
</html>
