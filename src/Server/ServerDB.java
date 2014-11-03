package Server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;


public class ServerDB {
	static String dbLocation=null;
	String check=null;
	String create;
	public ServerDB(String User) {

		dbLocation="src\\Storage\\"+User+"\\CloudSafe.db";
		try
		{
			File file=new File(dbLocation);
			if(file.exists()){
				check="Exist";
			}
			else if(create.equals("true")){
				System.out.println(System.getProperty("user.home") + File.separatorChar + "workspace\\CloudSafe\\src\\Storage\\"+User);

				File dir = new File(System.getProperty("user.home") + File.separatorChar + "workspace\\CloudSafe\\src\\Storage\\"+User);
				dir.mkdir();
				createReqTables();
			}
			else
			{
				System.out.println("No Database Found in Account");
				check="No Database";
			}	

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public ServerDB(String User, String create) {
		this.create=create;	
		dbLocation="src\\Storage\\"+User+"\\CloudSafe.db";
		System.out.println(dbLocation);
		try
		{
			File file=new File(dbLocation);
			if(file.exists()){
				check="Exist";
			}
			else if(create.equals("true")){
				System.out.println(System.getProperty("user.home") + File.separatorChar + "workspace\\CloudSafe\\src\\Storage\\"+User);

				File dir = new File(System.getProperty("user.home") + File.separatorChar + "workspace\\CloudSafe\\src\\Storage\\"+User);
				
				dir.mkdir();
				createReqTables();
				check="Create";
			}
			else
			{
				System.out.println("No Database Found in Account");
				check="No Database";
			}	

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public static void createReqTables()
	{
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			stmt = c.createStatement();
			String sql = "CREATE TABLE MASTER_TABLE"+
					"(ID 			TEXT 		PRIMARY KEY     			NOT NULL UNIQUE, " +
					" NAME			CHAR(50)    							NOT NULL, " + 
					" TIME_STAMP	TIMESTAMP 								NOT NULL, " + 
					" TEMPLATE		BOOLEAN     							NOT NULL, " + 
					" TRASH			BOOLEAN     DEFAULT 0					NOT NULL, " + 
					" STAR			BOOLEAN     DEFAULT 0					NOT NULL, " +  
					" IS_NOTE		BOOLEAN     DEFAULT 0					NOT NULL, " +  
					" NOTES         CHAR(50))"; 
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE PASSWORD" +
					"(Pass	CHAR(256)," +
					"TIME_STAMP	TIMESTAMP )";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	public void updatePass(String Pass,Timestamp sqlTimestamp){
		Connection c;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "DELETE FROM PASSWORD;";
			stmt.executeUpdate(sql);
			System.out.println("Delete database successfully");
			PreparedStatement pstmt=c.prepareStatement("INSERT INTO PASSWORD (Pass,Time_stamp) " +
					"VALUES (?,?);");
			pstmt.setString(1, Pass);
			pstmt.setTimestamp(2,sqlTimestamp);
			pstmt.execute();
			pstmt.close();	
			stmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("update done successfully");
	}

	public boolean comparePassTimeStamp(Timestamp locTimestamp){
		Connection c;
		Statement stmt;
		boolean chk = false;
		Timestamp CloudTimestamp=null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Password");

			if(rs.next())
			{
				CloudTimestamp=rs.getTimestamp("Time_Stamp");

				boolean time=locTimestamp.after(CloudTimestamp);
				if(time){
					chk= true;
				}
				else
				{
					chk=false;
				}
			}
			else
			{
				chk=true;
			}
			rs.close();
			stmt.close();
			c.close();

		} 
		catch (Exception ex ) 
		{
			System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
			System.exit(0);
		}
		return chk;	
	}

	public String compareTimeStamp(String ID,Timestamp locTimestamp){
		Connection c=null;
		Statement stmt=null;
		String chk="Error";

		Timestamp CloudTimestamp=null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Master_table where ID="+ID);

			if(rs.next())
			{
				CloudTimestamp=rs.getTimestamp("Time_Stamp");

				boolean time=locTimestamp.after(CloudTimestamp);
				if(time){
					chk= "UPDATE";
				}
				else
				{
					chk= "SKIP";
				}
			}
			else
			{
				chk= "NEW";
			}
			rs.close();
			stmt.close();
			c.close();

		} 
		catch (Exception ex ) 
		{
			System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
			System.exit(0);
		}
		return chk;
	}

	public void addEntry(String ID,String Name,Timestamp cardTime,int Template,int Trash,int Star,int is_note,String Note)
	{
		Connection c;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("INSERT INTO MASTER_TABLE (ID,NAME,TEMPLATE,STAR,NOTES,TIME_STAMP,IS_NOTE,TRASH) " +
					"VALUES (?,?,?,?,?,?,?,?);");
			pstmt.setString(1, ID);
			pstmt.setString(2, Name);
			pstmt.setInt(3, Template);
			pstmt.setInt(4, Star);
			pstmt.setString(5, Note);
			pstmt.setTimestamp(6, cardTime);
			pstmt.setInt(7, is_note);
			pstmt.setInt(8, Trash);
			pstmt.execute();

			pstmt=c.prepareStatement("CREATE TABLE entry"+ID +
					"(FIELDNAME	CHAR(50)	NOT NULL, " +
					"VALUE	CHAR(50)	, " +
					" TYPE		CHAR(50)	NOT NULL)"); 
			pstmt.execute();
			pstmt.close();
			c.commit();
			c.close();
		} 
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Record created successfully");
	}



	public void addEntryTable(String ID,String FieldName,String Value,String Type)
	{
		Connection c;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("INSERT INTO entry"+ ID +"(FIELDNAME,VALUE,TYPE) " +
					"VALUES (?,?,?);");
			pstmt.setString(1,FieldName);
			pstmt.setString(2,Value);
			pstmt.setString(3, Type);
			pstmt.execute();
			pstmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Data added to "+ID);
	}

	public void updateMaster(String ID,String Name,Timestamp cardTime,int Template,int Trash,int Star,int is_note,String Note) 
	{
		Connection c;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("UPDATE MASTER_TABLE SET NAME=?,TIME_STAMP=?,TEMPLATE=?,TRASH=?,STAR=?,IS_NOTE=?,NOTES=? WHERE ID=?");
			pstmt.setString(1, Name);
			pstmt.setTimestamp(2, cardTime);
			pstmt.setInt(3, Template);
			pstmt.setInt(4, Trash);
			pstmt.setInt(5, Star);
			pstmt.setInt(6, is_note);
			pstmt.setString(7, Note);
			pstmt.setString(8, ID);
			pstmt.executeUpdate();
			pstmt.close();
			c.commit();
			c.close();
		} 
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Record created successfully");


	}
	public void deleteTableEntry(String tableNo)
	{
		Connection c;
		Statement stmt = null;
		try {Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
		stmt = c.createStatement();
		String sql = "delete from entry"+tableNo; 
		stmt.executeUpdate(sql);
		stmt.close();
		c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Table d successfully");
	}
	public String getString(){
		return check;
	}
}
