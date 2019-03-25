
/*
fileName:CusDAO.java
ver: 2017_12_06
회원 DB작업 관련 sql문 종합 파일
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

public class CusDAO {
	
	DBConnect dbconnect = null;
	String sql="";	
	
	public CusDAO() {
		dbconnect = new DBConnect();
	}
	
	public String pasing(String data) {
		try {
			data = new String(data.getBytes("8859_1"), "UTF-8");
		}catch (Exception e){ }
		return data;
	}


	//모든회원검색
	public ArrayList<Customer> allCustomerData()
	{
		ArrayList<Customer> list = new ArrayList<Customer>();
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		try{
			//sql="SELECT * FROM Board where id=?";
			pstmt=con.prepareStatement("SELECT * FROM Customer");
			rs=pstmt.executeQuery();
			while(rs.next()){
				Customer cm = new Customer();
				cm.setId(rs.getString(1));
				cm.setPw(rs.getString(2));
				cm.setName(rs.getString(3));
				cm.setGender(rs.getString(4));
				cm.setPhone(rs.getString(5));
				cm.setEmail(rs.getString(6));
				cm.setUserFCM(rs.getString(7));
				list.add(cm);
			}
		}catch(Exception e){return null;}
		finally{ 
			dbconnect.close(con,pstmt,rs);
		}
		return list;
	}

	//회원삭제
	public String deleteCustomer(String id){
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		try{
			pstmt=con.prepareStatement("DELETE FROM Customer WHERE id = ?");
			pstmt.setString(1,id);
			pstmt.executeUpdate();
		}catch(Exception e){return null;}
		finally{ dbconnect.close(con,pstmt);}
		return "회원삭제가 완료되었습니다";
	}

	//자동로그인 설정
	public Boolean setAutoLogin(String id, String autologin){
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		try{
			if(autologin.equals("ok")){
  		     pstmt = con.prepareStatement("UPDATE Customer SET autologin = 1 WHERE id = ?");
  		     pstmt.setString(1,id);
 		     pstmt.executeUpdate();
      		} else {
         	 pstmt = con.prepareStatement("UPDATE Customer SET autologin = 0 WHERE id = ?");
         	 pstmt.setString(1,id);
         	 pstmt.executeUpdate();
      }
		}catch(Exception e){return null;}
		finally{ dbconnect.close(con,pstmt);}
		return true;
	}

	//회원가입
	public Boolean registerCustomer(String id,String pw, String name, String gender, String phone, String mail, String fcm){
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		try{
      		pstmt = con.prepareStatement("INSERT INTO Customer VALUES(?,?,?,?,?,?,?,0)");
  		    pstmt.setString(1,id);
  		    pstmt.setString(2,pw);
  		    pstmt.setString(3,name);
  		    pstmt.setString(4,gender);
  		    pstmt.setString(5,phone);
  		    pstmt.setString(6,mail);
  		    pstmt.setString(7,fcm);
 		    pstmt.executeUpdate();
		}catch(Exception e){return null;}
		finally{ dbconnect.close(con,pstmt);}
		return true;
	}

	//로그인
	public Customer Login(String id, String pw){
		Customer cm = new Customer();
		Connection con = dbconnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs =null;
		try{
			pstmt =con.prepareStatement("SELECT * FROM Customer WHERE id =? and pw = ?");
         	pstmt.setString(1,id);
         	pstmt.setString(2,pw);
			rs=pstmt.executeQuery();
			while(rs.next()){
				cm.setId(rs.getString(1));
				cm.setPw(rs.getString(2));
				cm.setName(rs.getString(3));
				cm.setGender(rs.getString(4));
				cm.setPhone(rs.getString(5));
				cm.setEmail(rs.getString(6));
				cm.setUserFCM(rs.getString(7));
			}
		}catch(Exception e){return null;}
		finally{ 
			dbconnect.close(con,pstmt,rs);
		}
		return cm;
	}
	//관리자확인
	public Boolean AdminCheck(String id, String pw){
		try{
			Connection con = dbconnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			sql="SELECT * from Admin where Admin.id=? and pw=?";
			pstmt= con.prepareStatement(sql);
			pstmt.setString(1,id);
			pstmt.setString(2,pw);
			rs=pstmt.executeQuery();
	
			if(rs.next())
				return true;
			else
				return false;
		}catch(Exception e){return false;}
	}


} 
