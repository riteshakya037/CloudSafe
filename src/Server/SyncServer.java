package Server;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import SafeUI.SafeMain;

public class SyncServer {
	static int cardID=0,cardName=1,cardTime=2,cardTemplate=3,cardTrash=4,cardStar=5,cardIsNote=6,cardNotes=7;
	private static int check=0;
	static TrayIcon trayIcon;
	static SystemTray tray;


	public SyncServer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try
		{
			HideToTray();
			// HOST A SERVER SOCKET
			final int authenPORT = 2444;
			ServerSocket authenServerSocket = new ServerSocket(authenPORT);
			System.out.println("Waiting for clients..");
			final int dataPORT = 1445;
			ServerSocket dataServerSocket = new ServerSocket(dataPORT);

			while(true){
				Socket authenSOCK = authenServerSocket.accept();	//Accepts if Request else loops
				Scanner INPUT = new Scanner(authenSOCK.getInputStream());
				String create=INPUT.nextLine();
				String user = INPUT.nextLine();
				String pass = INPUT.nextLine();
				System.out.println(user);
				System.out.println(pass);
				Authentication(user,pass,authenSOCK,create);
				INPUT.close();
				if(check==1){
					new ServerHandler(dataServerSocket.accept(),user).start();	
				}
			}
		}
		catch(Exception X)	
		{
			System.out.print(X);	
		}

	}

	public static void Authentication(String User,String pass,Socket SOCK, String create) throws IOException
	{

		if((User.equals("Ritesh") && pass.equals("123"))||User.equals("Salome") && pass.equals("123")||User.equals("Ruhi") && pass.equals("123"))
		{	
			System.out.println("correct");
			ServerDB sDB=new ServerDB(User,create);
			System.out.println(create+"  "+sDB.getString());
			
			String chk=sDB.getString();
			if(chk.equals("Create")){
				PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());	
				OUT.println("MATCH");	
				OUT.flush();
				check=1;
			}
			else if(chk.equals("No Database"))
			{
				PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
				OUT.println("NO DATABASE");	
				check=0;
				OUT.flush();
			}
			else if(chk.equals("Exist")){
				PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());	
				OUT.println("MATCH");	
				OUT.flush();
				check=1;
			}
			SOCK.close();
		}

		else
		{
			System.out.print("incorrect");
			PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
			OUT.println("INCORRECT");	
			check=0;
			OUT.flush();
			SOCK.close();
		}
	}
	private static void HideToTray(){
		if(SystemTray.isSupported()){

			tray=SystemTray.getSystemTray();

			Image image=Toolkit.getDefaultToolkit().getImage(SafeMain.class.getResource("/Resource/Icon.png"));
			ActionListener exitListener=new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);

				}
			};




			PopupMenu popup=new PopupMenu();
			MenuItem defaultItem=new MenuItem("Exit");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);


			trayIcon=new TrayIcon(image, "CloudSafe Server", popup);
			trayIcon.setImageAutoSize(true);
		}
		else
		{
		}
		try 
		{
			tray.add(trayIcon);
			trayIcon.displayMessage("Server Started ", "Waiting For Clients", TrayIcon.MessageType.NONE);
		} 
		catch(Exception Ex){

		}


	}
}
