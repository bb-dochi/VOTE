
/*
fileName:DAO.java
ver: 2017_11_22
DB작업 관련 sql문 종합 파일
*/

package vote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import vote.DBConnect;
import vote.BoardData;
import vote.Customer;
import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class DAO {
	
	DBConnect dbconnect = null;
	String sql="";	
	
	public DAO() {
		dbconnect = new DBConnect();
	}
	
	public String pasing(String data) {
		try {
			data = new String(data.getBytes("8859_1"), "UTF-8");
		}catch (Exception e){ }
		return data;
	}
	public String insertBoard(String category, String id, String title,String memo,String list1,String list2,String img1,String img2, int noname,String date) {
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		boolean result = false;

		try {
			if( img1 != null && img2 !=null ){
			sql = "INSERT INTO Board(category,id,title,memo,list1,list2,img1,img2,noname,alarm) VALUES(?,?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, category);
			pstmt.setString(2, id);			
			pstmt.setString(3, title);			
			pstmt.setString(4, memo);			
			pstmt.setString(5, list1);			
			pstmt.setString(6, list2);			
			pstmt.setString(7, img1);			
			pstmt.setString(8, img2);			
			pstmt.setInt(9, noname);
			pstmt.setString(10,date);
			pstmt.execute();
			result = true;
		}else if(img1 != null && img2 == null){
			sql = "INSERT INTO Board(category,id,title,memo,list1,list2,img1,noname,alarm) VALUES(?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, category);
			pstmt.setString(2, id);			
			pstmt.setString(3, title);			
			pstmt.setString(4, memo);			
			pstmt.setString(5, list1);			
			pstmt.setString(6, list2);			
			pstmt.setString(7, img1);			
					
			pstmt.setInt(8, noname);
			pstmt.setString(9,date);
			pstmt.execute();
			result = true;
		}else if(img1 == null && img2 != null){
			sql = "INSERT INTO Board(category,id,title,memo,list1,list2,img2,noname,alarm) VALUES(?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, category);
			pstmt.setString(2, id);			
			pstmt.setString(3, title);			
			pstmt.setString(4, memo);			
			pstmt.setString(5, list1);			
			pstmt.setString(6, list2);			
			pstmt.setString(7, img2);			
					
			pstmt.setInt(8, noname);
			pstmt.setString(9,date);
			pstmt.execute();
			result = true;
		}else{
			sql = "INSERT INTO Board(category,id,title,memo,list1,list2,noname,alarm) VALUES(?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, category);
			pstmt.setString(2, id);			
			pstmt.setString(3, title);			
			pstmt.setString(4, memo);			
			pstmt.setString(5, list1);			
			pstmt.setString(6, list2);			
						
					
			pstmt.setInt(7, noname);
			pstmt.setString(8,date);
			pstmt.execute();
			result = true;

		}

		}catch(Exception e) {
			System.out.println("Exception :" + e);
			return result+"/"+e;
		}finally {
			dbconnect.close(con,pstmt);
		}
		return result+"성공";
	}
	
	public int count(String category)
	{
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			sql = "SELECT COUNT(*) FROM Board where category=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,category);	
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt=rs.getInt(1);
			}
		}catch(Exception e) {
			
		}finally {
			dbconnect.close(con,pstmt,rs);
		}
		return cnt;
	}

	public int idcount(String id)
	{//해당 id가 쓴 게시물 개수
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			//sql = "SELECT COUNT(*) FROM Board where id=?";
			pstmt = con.prepareStatement("SELECT COUNT(*) FROM Board WHERE id=?");
			pstmt.setString(1,id);	
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt=rs.getInt(1);
			}
		}catch(Exception e) {
			
		}finally {
			dbconnect.close(con,pstmt,rs);
		}
		return cnt;
	}


	public ArrayList<BoardData> selectBoardData(String category)
	{//각 카테고리별 or 모든 게시물 목록
		ArrayList<BoardData> list = new ArrayList<BoardData>();
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		try{	
			if(category.equals("all")){
				sql="SELECT * FROM Board ORDER BY Board.index DESC";
				pstmt=con.prepareStatement(sql);
			}else{
				sql="SELECT * FROM Board where category=? ORDER BY Board.index DESC";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1,category);
			}
			rs=pstmt.executeQuery();
			while(rs.next()){
				BoardData bd = new BoardData();
				bd.setIndex(rs.getInt(1));
				bd.setCategory(rs.getString(2));
				bd.setId(rs.getString(3));
				bd.setTitle(rs.getString(4));
				bd.setMemo(rs.getString(5));
				bd.setList1(rs.getString(6));
				bd.setList2(rs.getString(7));
				bd.setImg1(rs.getString(8));
				bd.setImg2(rs.getString(9));
				bd.setVote1(rs.getInt(10));
				bd.setVote2(rs.getInt(11));
				bd.setNoname(rs.getInt(12));
				bd.setAlarm(rs.getString(13));
				list.add(bd);
			}
		}catch(Exception e){return null;}
		finally{ dbconnect.close(con,pstmt,rs);}
		return list;
	}
	
	public ArrayList<BoardData> allBoardData(String id)
	{//해당 id 가 작성한 글 목록
		ArrayList<BoardData> list = new ArrayList<BoardData>();
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		try{
			//sql="SELECT * FROM Board where id=?";
			pstmt=con.prepareStatement("SELECT * FROM Board WHERE id=?");
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			while(rs.next()){
				BoardData bd = new BoardData();
				bd.setIndex(rs.getInt(1));
				bd.setCategory(rs.getString(2));
				bd.setId(rs.getString(3));
				bd.setTitle(rs.getString(4));
				bd.setMemo(rs.getString(5));
				bd.setList1(rs.getString(6));
				bd.setList2(rs.getString(7));
				bd.setImg1(rs.getString(8));
				bd.setImg2(rs.getString(9));
				bd.setVote1(rs.getInt(10));
				bd.setVote2(rs.getInt(11));
				bd.setNoname(rs.getInt(12));
				bd.setAlarm(rs.getString(13));
				list.add(bd);
			}
		}catch(Exception e){return null;}
		finally{ dbconnect.close(con,pstmt,rs);}
		return list;
	}

	public int deleteBoard(int index)
	{
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			sql="DELETE FROM Board where Board.index=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,index);
			pstmt.executeUpdate();
			return 1;
		}catch(Exception e){return 0;}	
	}

	public int IsVote (int index, String id)
	{//현재 글에 투표했었는지
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			sql="SELECT * FROM VoteTable where boardNum =? and id =?";
			pstmt= con.prepareStatement(sql);
			pstmt.setInt(1,index);
			pstmt.setString(2,id);
			rs = pstmt.executeQuery();
			if(rs.next())
				return 1;
			else{
				return 0;
			}
		}catch(Exception e){
			return -1;
		}
	}

	public String VoteNow(int index, String id, int checkNum)
	{//투표기능
		int vote1=0,vote2=0;
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			sql="SELECT vote1,vote2 from Board where Board.index=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,index);
			rs = pstmt.executeQuery();
			if(rs.next()){
				vote1=rs.getInt(1);
				vote2=rs.getInt(2);
			}
		}catch(Exception e){return "error1"+e;}
		finally{dbconnect.close(con,pstmt,rs);}
		
		con = dbconnect.getConnection();
		pstmt = null;
		rs = null;
		try{
			if(checkNum==1){
				sql="UPDATE Board SET vote1=? where Board.index=?";
				pstmt= con.prepareStatement(sql);
				pstmt.setInt(1,vote1+1);
			}
			else{
				sql="UPDATE Board SET vote2=? where Board.index=?";	
				pstmt= con.prepareStatement(sql);
				pstmt.setInt(1,vote2+1);
			}
			pstmt.setInt(2,index);
			pstmt.executeUpdate();

			sql = "INSERT INTO VoteTable(boardNum,id) VALUES(?,?)";
			pstmt= con.prepareStatement(sql);
			pstmt.setInt(1,index);
			pstmt.setString(2,id);
			pstmt.execute();
		}catch(Exception e){return "error2"+e;}
		finally{dbconnect.close(con,pstmt,rs);}
		return "회원님의 소중한 한 표를 행사했습니다";
	}

	public ArrayList<BoardData> voteBoardData(String id, int total)
	{//해당 id 투표한 글 정보
		ArrayList<BoardData> list = new ArrayList<BoardData>();
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		PreparedStatement pstmt2 =null;
		ResultSet rs =null;
		ResultSet rs2 =null;
		try{
			//sql="SELECT * FROM Board where id=?";
			pstmt=con.prepareStatement("SELECT boardNum FROM VoteTable WHERE id=?");
			pstmt.setString(1,id);
			rs=pstmt.executeQuery();
			while(rs.next()) {
        	int boardNum = rs.getInt("boardNum");
       		pstmt2 = con.prepareStatement("select * from Board where Board.index = ?");
        	pstmt2.setInt(1,boardNum);
        	rs2 = pstmt2.executeQuery();
       		while(rs2.next()){
            BoardData bd = new BoardData();
            bd.setIndex(rs2.getInt(1));
            bd.setCategory(rs2.getString(2));
            bd.setId(rs2.getString(3));
            bd.setTitle(rs2.getString(4));
            bd.setMemo(rs2.getString(5));
            bd.setList1(rs2.getString(6));
            bd.setList2(rs2.getString(7));
            bd.setImg1(rs2.getString(8));
            bd.setImg2(rs2.getString(9));
            bd.setVote1(rs2.getInt(10));
            bd.setVote2(rs2.getInt(11));
            bd.setNoname(rs2.getInt(12));
            bd.setAlarm(rs2.getString(13));
            list.add(bd);
        }
      }

		}catch(Exception e){return null;}
		finally{ dbconnect.close(con,pstmt,rs);}
		return list;
	}

	public int votecount(String id)
	{
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			//sql = "SELECT COUNT(*) FROM Board where id=?";
			pstmt = con.prepareStatement("SELECT COUNT(*) FROM VoteTable WHERE id=?");
			pstmt.setString(1,id);	
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt=rs.getInt(1);
			}
		}catch(Exception e) {
			
		}finally {
			dbconnect.close(con,pstmt,rs);
		}
		return cnt;
	}

	public int totalvote(){

		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			//sql = "SELECT COUNT(*) FROM Board where id=?";
			pstmt = con.prepareStatement("SELECT COUNT(*) FROM VoteTable");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt=rs.getInt(1);
			}
		}catch(Exception e) {
			
		}finally {
			dbconnect.close(con,pstmt,rs);
		}
		return cnt;
	}

	public int totalcount(){

		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;
		
		try {
			//sql = "SELECT COUNT(*) FROM Board where id=?";
			pstmt = con.prepareStatement("SELECT COUNT(*) FROM Board");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				cnt=rs.getInt(1);
			}
		}catch(Exception e) {
			
		}finally {
			dbconnect.close(con,pstmt,rs);
		}
		return cnt;
	}
	
} 
