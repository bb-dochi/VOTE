/*
fileName : DBConnect
ver : 2017_11_22
DB 연결 파일입니다.
*/ 

package vote;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class DBConnect {
	public DBConnect() {}
	
	public Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/vote?useUnicode=yes&characterEncoding=UTF-8"; //디비명적기
		String id = "root"; //id
		String pass = "[wow1133]"; //비밀번호
		
		Connection con = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,id,pass);
		}catch(Exception e) {
			System.out.println(e);
		}
		return con;
	}
	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {
			try {
				if(rs!=null) { rs.close(); rs=null;}
			}catch(Exception e) {}
			
			try {
				if(pstmt!=null) { pstmt.close(); pstmt=null; }
			}catch(Exception e) {}
			
			try {
				if(con!=null) { con.close(); con=null; }
			}catch(Exception e) {}
		}catch(Exception e) {}
	}
	
	public static void close(Connection con, PreparedStatement pstmt) {
		try {
			try {
				if(pstmt!=null) { pstmt.close(); pstmt=null; }
			}catch(Exception e) {}
			
			try {
				if(con!=null) { con.close(); con=null; }
			}catch(Exception e) {}
		}catch(Exception e) {}
	}
}
