package db;

import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 

public class jdbcmysql {
	private Connection con = null; //Database objects 
	  //連接object 
	  private Statement stat = null; 
	  //執行,傳入之sql為完整字串 
	  private ResultSet rs = null; 
	  //結果集 
	  private PreparedStatement pst = null; 
	  //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
	  //先利用?來做標示 
	  
	  private String dropdbSQL = "DROP TABLE User "; 
	  
	  private String createdbSQL = "CREATE TABLE User (" + 
	    "    id     INTEGER " + 
	    "  , name    VARCHAR(20) " + 
	    "  , passwd  VARCHAR(20)"+
	    "  , grade VARCHAR(20))"; 
	  
	  private String insertdbSQL = "insert into User(id,name,passwd,grade) " + 
	      "select ifNULL(max(id),0)+1,?,?,? FROM User"; 
	  
	  private String selectSQL = "select * from User "; 
	  
	  public jdbcmysql() 
	  { 
	    try { 
	      Class.forName("com.mysql.cj.jdbc.Driver"); 
	      //註冊driver 
	      con = DriverManager.getConnection( 
	      "jdbc:mysql://127.0.0.1:3306/user?serverTimezone=CST&useUnicode=true&characterEncoding=Big5", 
	      "parry1233","parry1233"); 
	      //取得connection

	//jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5
	//localhost是主機名,test是database名
	//useUnicode=true&characterEncoding=Big5使用的編碼 
	      
	    } 
	    catch(ClassNotFoundException e) 
	    { 
	      System.out.println("DriverClassNotFound :"+e.toString()); 
	    }//有可能會產生sqlexception 
	    catch(SQLException x) { 
	      System.out.println("Exception :"+x.toString()); 
	    } 
	    
	  } 
	  //建立table的方式 
	  //可以看看Statement的使用方式 
	  public void createTable() 
	  { 
	    try 
	    { 
	      stat = con.createStatement(); 
	      stat.executeUpdate(createdbSQL); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("CreateDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	  //新增資料 
	  //可以看看PrepareStatement的使用方式 
	  public void insertTable( String name,String passwd,String gr) 
	  { 
	    try 
	    { 
	      pst = con.prepareStatement(insertdbSQL); 
	      
	      pst.setString(1, name); 
	      pst.setString(2, passwd); 
	      pst.setString(3, gr);
	      pst.executeUpdate(); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("InsertDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	  //刪除Table, 
	  //跟建立table很像 
	  public void dropTable() 
	  { 
	    try 
	    { 
	      stat = con.createStatement(); 
	      stat.executeUpdate(dropdbSQL); 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	  //查詢資料 
	  //可以看看回傳結果集及取得資料方式 
	  public void SelectTable() 
	  { 
	    try 
	    { 
	      stat = con.createStatement(); 
	      rs = stat.executeQuery(selectSQL); 
	      System.out.println("ID\t\tName\t\tPASSWORD\tGrade"); 
	      while(rs.next()) 
	      { 
	        System.out.println(rs.getInt("id")+"\t\t"+ 
	            rs.getString("name")+"\t\t"+rs.getString("passwd")+"\t\t"+rs.getString("grade")); 
	      } 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("DropDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	  } 
	  //完整使用完資料庫後,記得要關閉所有Object 
	  //否則在等待Timeout時,可能會有Connection poor的狀況 
	  private void Close() 
	  { 
	    try 
	    { 
	      if(rs!=null) 
	      { 
	        rs.close(); 
	        rs = null; 
	      } 
	      if(stat!=null) 
	      { 
	        stat.close(); 
	        stat = null; 
	      } 
	      if(pst!=null) 
	      { 
	        pst.close(); 
	        pst = null; 
	      } 
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("Close Exception :" + e.toString()); 
	    } 
	  } 
	  

	  public static void main(String[] args) 
	  { 
	    //測看看是否正常 
	    jdbcmysql test = new jdbcmysql(); 
	    test.dropTable(); 
	    test.createTable(); 
	    test.insertTable("yku", "12356","1"); 
	    test.insertTable("yku2", "7890","2"); 
	    test.insertTable("Perry", "1233","1"); 
	    test.insertTable("Alice", "3456","2"); 
	    test.SelectTable(); 
	  
	  } 

}
