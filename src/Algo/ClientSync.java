package Algo;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Scanner;

public class ClientSync {

	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db";
	static Socket authenSOCK,dataSOCK,backSOCK;
	static String HOST = "192.168.86.1";
	static int cardID=0,cardName=1,cardTime=2,cardTemplate=3,cardTrash=4,cardStar=5,cardIsNote=6,cardNotes=7;
	static boolean chk=true;
	String User,Pass;
	String check="";

	public ClientSync(String User,String Pass,String create) {
		try
		{
			final int PORT = 2444;
			authenSOCK = new Socket(HOST,PORT);
			PrintWriter OUT = new PrintWriter(authenSOCK.getOutputStream());
			//Send Uname and Password as Value
			System.out.println(User);
			System.out.println(Pass);
			String[] delivery = {User,Pass};
			OUT.println(create);
			OUT.println(delivery[0]);
			OUT.println(delivery[1]);
			OUT.flush();

			//Waiting for Authorization
			String MESSAGE = "";
			while(MESSAGE == "") //wait for response
			{
				Scanner INPUT = new Scanner(authenSOCK.getInputStream());	
				MESSAGE = INPUT.nextLine(); 
				INPUT.close();
				
			}
			//Validation of Authorization
			if (MESSAGE.contains("INCORRECT"))
			{	
				System.out.println("INVALID ACCOUNT 123");	

				check="NOT GRANTED";
			}
			else if(MESSAGE.contains("NO DATABASE")){
				check="NO DATABASE";
			}
			else
			{
				System.out.println("Access Granted for "+delivery[0]);
				sendData();
				recData();
				check="GRANTED";
			}
		}

		catch(Exception X)
		{
			System.out.print("hava"+X);

		}
	}
	
	public ClientSync() {
		try
		{
			final int PORT = 2444;
			authenSOCK = new Socket(HOST,PORT);
			PrintWriter OUT = new PrintWriter(authenSOCK.getOutputStream());
			//Send Uname and Password as Value
			
			String[] delivery = {User,Pass};
			OUT.println(false);
			OUT.println(delivery[0]);
			OUT.println(delivery[1]);
			OUT.flush();

			//Waiting for Authorization
			String MESSAGE = "";
			while(MESSAGE == "") //wait for response
			{
				Scanner INPUT = new Scanner(authenSOCK.getInputStream());	
				MESSAGE = INPUT.nextLine(); 
				INPUT.close();
			}
			//Validation of Authorization
			if (MESSAGE.contains("INCORRECT"))
			{	
				check="INCORRECT";
			}
			else
			{
				System.out.println("Access Granted for "+delivery[0]);
			}
			authenSOCK.close();
		}

		catch(Exception X)
		{
			System.out.print("hava"+X);

		}
	}


	private static void recData() {

		try {
			final int backPORT = 1446;
			backSOCK = new Socket(HOST,backPORT);
			System.out.println("Reached");

			dbAction.CloudLoad();
			ObjectInputStream dataInput = new ObjectInputStream(backSOCK.getInputStream());
			int count=dataInput.read();
			String[] recPass=(String[]) dataInput.readObject();
			String[][] recCard=(String[][]) dataInput.readObject();
			String[][][] recVal=(String[][][]) dataInput.readObject();
			chk=false;
			dataInput.close();
			if(dbAction.comparePassTimeStamp(Timestamp.valueOf(recPass[1]))){
				dbAction.updatePass(recPass[0], Timestamp.valueOf(recPass[1]));
			}

			for(int i=0;i<count;i++){

				String ID=recCard[i][cardID];
				String Name=recCard[i][cardName];
				Timestamp Time=Timestamp.valueOf(recCard[i][cardTime]);
				int Template=Integer.parseInt(recCard[i][cardTemplate]);
				int Trash=Integer.parseInt(recCard[i][cardTrash]);
				int Star=Integer.parseInt(recCard[i][cardStar]);
				int Is_Note=Integer.parseInt(recCard[i][cardIsNote]);
				String Notes=recCard[i][cardNotes];


				String chk=dbAction.compareTimeStamp(recCard[i][cardID], Time);



				if(chk=="UPDATE"){
					System.out.println(recCard[i][cardID]+" Update");
					dbAction.updateMaster(ID, Name, Time, Template, Trash, Star, Is_Note, Notes);
					dbAction.deleteTableEntry(ID);
					for(int j=0;j<recVal[i].length;j++)
					{
						if(recVal[i][j][0]!=null)
						{
							dbAction.addEntryTable(ID, recVal[i][j][0], recVal[i][j][1], recVal[i][j][2]);
							System.out.println(recVal[i][j][0]+" "+recVal[i][j][1]+" "+recVal[i][j][2]);
						}

					}
					System.out.println();
				}
				else if(chk=="SKIP"){
					System.out.println(recCard[i][cardID]+" Skip");
				}
				else if(chk=="NEW"){				
					dbAction.addEntry(ID, Name, Time, Template, Trash, Star, Is_Note, Notes);
					System.out.println(recCard[i][cardID]+" New");
					for(int j=0;j<recVal[i].length;j++)
					{
						if(recVal[i][j][0]!=null)
						{
							dbAction.addEntryTable(ID, recVal[i][j][0], recVal[i][j][1], recVal[i][j][2]);
						}
					}
				}


			}


		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}


	}


	private static void sendData() throws IOException {
		Connection c=null;
		Statement stmt=null;

		final int dataPORT = 1445;

		dbAction.CloudLoad();
		dataSOCK = new Socket(HOST,dataPORT);

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			int count=0,rowCount=1;
			String[] sendPass=new String[2];
			String[][] sendID=new String[100][8];
			String[][][] sendVal=new String[100][10][3];
			//String[] sendTimestamp=new String[100];
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Master_table");
			while(rs.next()){
				sendID[count][cardID]=rs.getString("ID");
				sendID[count][cardName]=rs.getString("Name");
				sendID[count][cardTime]=rs.getTimestamp("Time_Stamp").toString();
				sendID[count][cardTemplate]=Integer.toString(rs.getInt("Template"));
				sendID[count][cardTrash]=Integer.toString(rs.getInt("Trash"));
				sendID[count][cardStar]=Integer.toString(rs.getInt("Star"));
				sendID[count][cardIsNote]=Integer.toString(rs.getInt("Is_note"));
				sendID[count][cardNotes]=rs.getString("notes");
				Statement stmt1 = c.createStatement();
				ResultSet rs1 = stmt1.executeQuery("select * from entry"+sendID[count][0]);

				System.out.println(sendID[count][0]+" "+sendID[count][1]+" "+sendID[count][2]+" "+sendID[count][3]+" "+sendID[count][4]+" "+sendID[count][5]+" "+sendID[count][6]+" "+sendID[count][7]);
				rowCount=0;
				while(rs1.next()){
					sendVal[count][rowCount][0]=rs1.getString("FieldName");
					sendVal[count][rowCount][1]=rs1.getString("Value");
					sendVal[count][rowCount][2]=rs1.getString("type");
					System.out.println(sendVal[count][rowCount][0]+" "+sendVal[count][rowCount][1]+" "+sendVal[count][rowCount][2]);
					rowCount++;
				}
				System.out.println();
				rs1.close();stmt1.close();
				count++;
			}
			rs.close();
			stmt.close();
			stmt = c.createStatement();
			rs = stmt.executeQuery("select * from Password");
			while(rs.next()){
				sendPass[0]=rs.getString("pass");
				sendPass[1]=rs.getTimestamp("Time_Stamp").toString();
			}
			c.close();

			ObjectOutputStream OUT = new ObjectOutputStream(dataSOCK.getOutputStream());
			OUT.write(count);
			OUT.writeObject(sendPass);
			OUT.writeObject(sendID);
			OUT.writeObject(sendVal);
			OUT.close();



		} catch (Exception ex ) {
			System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
			
		}	

	}


	public String getString() {
		// TODO Auto-generated method stub
		return check;
	}

}
