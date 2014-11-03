package Algo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import SafeUI.SafeMain;
import SafeUI.loadingScreen;

public class dbAction implements Runnable
{
	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db";
	static Connection c = null;
	static String pass=null;
	static loadingScreen ls=new loadingScreen();


	public dbAction(String newPass1String) {
		pass=newPass1String;
	}

	public dbAction() {
	}

	public boolean load() throws IOException {
		boolean dbExist=true;
		try
		{
			File file=new File(dbLocation);
			if(file.exists() && !getPass().equals("NOT FOUND") && !getMaster().equals("NOT FOUND")){
				dbExist= true;
			}
			else if(file.exists() && getPass().equals("") && getMaster().equals("")){
				dbExist= true;
			}
			else
			{
				dbExist= false;
				File dir = new File(System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe");
				dir.mkdir();
			}	

		}
		catch(Exception e)
		{

		}
		return dbExist;
	}

	public static void CloudLoad(){
		try
		{
			File file=new File(dbLocation);
			if(file.exists() && !getPass().equals("NOT FOUND") && !getMaster().equals("NOT FOUND")){

			}
			
			else if(file.exists() && getPass().equals("NOT FOUND") && getMaster().equals("NOT FOUND")){
				System.out.println("FUCKKK");
				createReqTables();
			}
			else
			{
				File dir = new File(System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe");
				dir.mkdir();
				createReqTables();
			}	

		}
		catch(Exception e)
		{
			System.out.println("Cannot Connect to Cloud");
		}
	}


	public void run()
	{

		Thread th = new Thread(ls);
		th.start();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		ls.setStatus("Creating Database...");		
		ls.setProgress(5);
		ls.setStatus("Creating Master Table...");
		createReqTables();		
		ls.setProgress(5);

		ls.setStatus("Creating Sample entries...");
		initializeTemplates();

		ls.setStatus("Initializing Data...");
		initiableTemplateDatas();

		ls.setStatus("Cleaning up...");
		ls.setProgress(10);
		updatePass(pass);
		new SafeMain();
		ls.stop();

	}
	public static String getMaster() {
		Statement stmt = null;
		String  pass = null;
		try {Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM MASTER_TABLE;" );

		rs.close();
		stmt.close();
		c.close();
		} catch ( Exception e ) {
			if(e.getMessage().contains("[SQLITE_ERROR]")){
				//System.err.println( e.getClass().getName() + ": " + e.getMessage()+ "asdasdas12");
				try {
					c.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return "NOT FOUND";
			}
			else if(e.getMessage().contains("[SQLITE_NOTADB]")){
				//System.err.println( e.getClass().getName() + ": " + e.getMessage()+ "HAVA");
				try {
					c.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return "";
			
			}
		}
		System.out.println(pass);
		return pass;
	}


	public static String getPass() {
		Statement stmt = null;
		String  pass = null;
		try {Class.forName("org.sqlite.JDBC");
		c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
		c.setAutoCommit(false);
		System.out.println("Opened database successfully");
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM PASSWORD;" );
		while ( rs.next() ) {
			pass = rs.getString("Pass");

		}
		rs.close();
		stmt.close();
		c.close();
		} catch ( Exception e ) {
			if(e.getMessage().contains("[SQLITE_ERROR]")){
				//System.err.println( e.getClass().getName() + ": " + e.getMessage()+ "asdasdas12");
				return "NOT FOUND";
			}
			else  if(e.getMessage().contains("[SQLITE_NOTADB]")){
			//	System.err.println( e.getClass().getName() + ": " + e.getMessage()+ "HAVA");

				try {
					
					c.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return "";
			}
		}
		System.out.println(pass);
		return pass;
	}

	public void updatePass(String Pass){

		java.util.Date date = new java.util.Date();
		long t = date.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
		Statement stmt = null;
		try {
			Hashing h=new Hashing();
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
			String HashedPass=h.HmacSHA256(Pass);
			pstmt.setString(1, HashedPass);
			pstmt.setTimestamp(2,sqlTimestamp);
			pstmt.execute();
			pstmt.close();	
			stmt.close();
			c.commit();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		System.out.println("update done successfully");
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

		}
		System.out.println("Table created successfully");
	}


	public static void deleteTableEntry(String tableNo)
	{
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

		}
		System.out.println("Table d successfully");
	}

	public static void initializeTemplates()
	{
		java.util.Date date = new java.util.Date();
		long t = date.getTime();
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);

		addEntry("100","Credit Card",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("101","Web Acount",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("102","Email Account",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("103","Login/Password",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("104","Code",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("105","ID/PassPort",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("106","Insurance",1,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("200","Google",0,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("201","Yahoo",0,0,null,sqlTimestamp,0);
		ls.setProgress(2);
		addEntry("202","Facebook",0,0,null,sqlTimestamp,0);
		ls.setProgress(4);
		addEntry("203","XYZ Bank",0,0,null,sqlTimestamp,0);
		ls.setProgress(4);
		addEntry("204","Steam",0,0,null,sqlTimestamp,0);
		ls.setProgress(4);
		addEntry("205","Insurance",0,0,null,sqlTimestamp,0);
		ls.setProgress(4);
	}

	public static void initiableTemplateDatas()
	{

		addEntryTable("100","Number",null,"number");
		addEntryTable("100","Owner",null,"text");
		addEntryTable("100","Expires",null,"date");
		addEntryTable("100","CVV",null,"pin");
		addEntryTable("100","PIN",null,"pin");
		addEntryTable("100","Blockinh",null,"phone");
		ls.setProgress(4);

		addEntryTable("101","Login",null,"login");
		addEntryTable("101","Password",null,"password");
		addEntryTable("101","Website",null,"website");
		ls.setProgress(4);

		addEntryTable("102","Email",null,"email");
		addEntryTable("102","Password",null,"password");
		addEntryTable("102","Website",null,"website");
		ls.setProgress(4);

		addEntryTable("103","Login",null,"login");
		addEntryTable("103","Password",null,"password");
		ls.setProgress(4);

		addEntryTable("104","Code",null,"password");
		ls.setProgress(4);

		addEntryTable("105","Number",null,"text");
		addEntryTable("105","Name",null,"text");
		addEntryTable("105","Birthday",null,"date");
		addEntryTable("105","Issue",null,"date");
		addEntryTable("105","Expires",null,"date");
		ls.setProgress(4);

		addEntryTable("106","Name",null,"text");
		addEntryTable("106","Expires",null,"date");
		addEntryTable("106","Phone",null,"phone");
		ls.setProgress(4);


		addEntryTable("200","Login","Ritesh","login");
		addEntryTable("200","Password","123","password");
		addEntryTable("200","Website","www.google.com","website");
		ls.setProgress(4);

		addEntryTable("201","Login","Ritesh@yahoo.com","email");
		addEntryTable("201","Password","123","password");
		addEntryTable("201","Website","www.yahoo.com","website");
		ls.setProgress(4);

		addEntryTable("202","Login","9841814809","phone");
		addEntryTable("202","Password","123","password");
		addEntryTable("202","Website","www.facebook.com","website");
		ls.setProgress(4);

		addEntryTable("203","Number","06-267-717","text");
		addEntryTable("203","Owner","Ritesh","text");
		addEntryTable("203","Expires","04/18","date");
		addEntryTable("203","PIN","9082","pin");
		addEntryTable("203","Blocking","","phone");
		ls.setProgress(4);

		addEntryTable("204","Login","xbs_ritzz","login");
		addEntryTable("204","Password","123","password");
		ls.setProgress(4);

		addEntryTable("205","Number","45/457-451","text");
		addEntryTable("205","Expires","06/31/71","Date");
		addEntryTable("205","Phone","984184756","Phone");
		ls.setProgress(4);


	}
	//	private static void addType(String Type) {
	//		try {
	//			Class.forName("org.sqlite.JDBC");
	//			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
	//			c.setAutoCommit(false);
	//			PreparedStatement pstmt=c.prepareStatement("INSERT INTO TYPE (NAME) " +
	//					"VALUES (?);");
	//			pstmt.setString(1, Type);
	//			pstmt.execute();
	//			pstmt.close();
	//			c.commit();
	//			c.close();
	//		} 
	//		catch ( Exception e ) 
	//		{
	//			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	//			
	//		}
	//		ls.setProgress(2);
	//		System.out.println("Record created successfully");
	//
	//	}



	//	public static void addLabel(String Name){
	//		try {
	//			Class.forName("org.sqlite.JDBC");
	//			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
	//			c.setAutoCommit(false);
	//			PreparedStatement pstmt=c.prepareStatement("INSERT INTO LABEL (NAME) " +
	//					"VALUES (?);");
	//			pstmt.setString(1, Name);
	//			pstmt.execute();
	//			pstmt.close();
	//			c.commit();
	//			c.close();
	//		} 
	//		catch ( Exception e ) 
	//		{
	//			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	//			
	//		}
	//		System.out.println("Record created successfully");
	//	
	//	}

	public static void addEntry(String ID,String Name,int Template,int Star,String Note,Timestamp tmp,int is_note)
	{

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("INSERT INTO MASTER_TABLE (ID,NAME,TEMPLATE,STAR,NOTES,TIME_STAMP,IS_NOTE) " +
					"VALUES (?,?,?,?,?,?,?);");
			pstmt.setString(1, ID);
			pstmt.setString(2, Name);
			pstmt.setInt(3, Template);
			pstmt.setInt(4, Star);
			pstmt.setString(5, Note);
			pstmt.setTimestamp(6, tmp);
			pstmt.setInt(7, is_note);
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

		}
		System.out.println("Record created successfully");
	}

	public static void addEntryTable(String ID,String FieldName,String Value,String Type)
	{

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

		}
		System.out.println("Data added to "+ID);
	}
	//	public static void select()
	//	{
	//		Statement stmt = null;
	//		try {Class.forName("org.sqlite.JDBC");
	//		c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
	//		c.setAutoCommit(false);
	//		System.out.println("Opened database successfully");
	//		stmt = c.createStatement();
	//		ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
	//		while ( rs.next() ) {
	//			int id = rs.getInt("id");
	//			String  name = rs.getString("name");
	//			int age  = rs.getInt("age");
	//			String  address = rs.getString("address");
	//			float salary = rs.getFloat("salary");
	//			System.out.println( "ID = " + id );
	//			System.out.println( "NAME = " + name );
	//			System.out.println( "AGE = " + age );
	//			System.out.println( "ADDRESS = " + address );
	//			System.out.println( "SALARY = " + salary );
	//			System.out.println();
	//		}
	//		rs.close();
	//		stmt.close();
	//		c.close();
	//		} catch ( Exception e ) {
	//			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	//			
	//		}
	//		System.out.println("Operation done successfully");
	//	}



	public void delete() {

		File file=new File(dbLocation);
		file.delete();
	}



	public static void updateMaster(String ID,String Name,int Template,int Star,String Note, Timestamp sqlTimestamp) 
	{
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("UPDATE MASTER_TABLE SET NAME=?,TEMPLATE=?,STAR=?,NOTES=?,TIME_STAMP=? WHERE ID=?");
			pstmt.setString(1, Name);
			pstmt.setInt(2, Template);
			pstmt.setInt(3, Star);
			pstmt.setString(4, Note);
			pstmt.setTimestamp(5, sqlTimestamp);
			pstmt.setString(6, ID);
			pstmt.execute();
			pstmt.close();
			c.commit();
			c.close();
		} 
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		System.out.println("Record created successfully");


	}

	public void moveTrash(String ID,int Trash)
	{
		try 
		{
			java.util.Date date = new java.util.Date();
			long t = date.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("UPDATE MASTER_TABLE SET Trash=?,Time_stamp=?  WHERE ID=?");
			pstmt.setInt(1, Trash);
			pstmt.setTimestamp(2, sqlTimestamp);
			pstmt.setString(3, ID);
			pstmt.execute();
			System.out.println(pstmt.toString());
			pstmt.close();
			c.commit();
			c.close();
		} 
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		System.out.println("Record created successfully");


	}

	public void moveFav(String ID,int fav)
	{
		try {

			java.util.Date date = new java.util.Date();
			long t = date.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("UPDATE MASTER_TABLE SET Star=?,Time_stamp=? WHERE ID=?");
			pstmt.setInt(1, fav);
			pstmt.setTimestamp(2, sqlTimestamp);
			pstmt.setString(3, ID);
			pstmt.execute();
			System.out.println(pstmt.toString());
			pstmt.close();
			c.commit();
			c.close();
		} 
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		System.out.println("Record created successfully");


	}


	public void deleteCard(String ID) {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			PreparedStatement pstmt=c.prepareStatement("DELETE FROM master_table WHERE ID=?");
			pstmt.setString(1, ID);
			pstmt.execute();
			pstmt.close();
			c.commit();
			c.close();
		} 
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		deleteTableEntry(ID);
	}

	public static String compareTimeStamp(String ID, Timestamp locTimestamp) {
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

		}
		return chk;
	}
	public static void addEntry(String ID,String Name,Timestamp cardTime,int Template,int Trash,int Star,int is_note,String Note)
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

		}
		System.out.println("Record created successfully");
	}
	public static void updateMaster(String ID,String Name,Timestamp cardTime,int Template,int Trash,int Star,int is_note,String Note) 
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

		}
		System.out.println("Record created successfully");


	}
	public static void updatePass(String Pass,Timestamp sqlTimestamp){
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

		}
		System.out.println("update done successfully");
	}

	public static boolean comparePassTimeStamp(Timestamp locTimestamp){
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

		}
		return chk;	
	}

}