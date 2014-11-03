package SafeUI;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import Algo.Prefs;

@SuppressWarnings("serial")
public class ToolBar extends JFrame {
	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db",Login,Password;
	private JPanel contentPane;
	public ToolBar(String tableNo) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblName = new JLabel("Asd");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(1, 6, 384, 26);
		contentPane.add(lblName);

		JPanel panel = new JPanel();
		panel.setBounds(0, 29, 384, 33);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblClickButtonTo = new JLabel("Click Button to Copy");
		lblClickButtonTo.setBounds(10, 9, 134, 14);
		panel.add(lblClickButtonTo);

		JSeparator separator = new JSeparator();
		separator.setBounds(149, 15, 0, 2);
		panel.add(separator);

		Connection c;
		Statement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Master_table where ID="+tableNo+";");
			lblName.setText(rs.getString("Name"));
			rs.close();
			stmt.close();

			stmt = c.createStatement();
			rs = stmt.executeQuery("select * from entry"+tableNo);
			while ( rs.next() ) {
				System.out.println("Form:::"+rs.getString("FieldName"));

				if(rs.getString("FieldName").startsWith("Login"))
				{

					Login=rs.getString("Value");
				}
				if(rs.getString("FieldName").startsWith("Password"))
				{
					System.out.println("Form:::"+rs.getString("Value"));

					Password=rs.getString("Value");
				}
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception ex ) {
			System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );

		}	

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection testData;

				//  Add some test data

				testData = new StringSelection(Login);

				c.setContents(testData, testData);

				//  Get clipboard contents, as a String

				Transferable t = c.getContents( null );

				if ( t.isDataFlavorSupported(DataFlavor.stringFlavor) )
				{
					try {
						String data = (String)t.getTransferData( DataFlavor.stringFlavor );
						System.out.println( "Clipboard contents: " + data );
					} catch (UnsupportedFlavorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {

						Prefs ps=new Prefs();

						int delay=Integer.parseInt(ps.getProp("clearDelay"));

						Thread.sleep(delay*1000);
						testData = new StringSelection("");

						c.setContents(testData, testData);

						//  Get clipboard contents, as a String

						t = c.getContents( null );

						if ( t.isDataFlavorSupported(DataFlavor.stringFlavor) )
						{
							try {
								String data = (String)t.getTransferData( DataFlavor.stringFlavor );
								System.out.println( "Clipboard contents: " + data );
							} catch (UnsupportedFlavorException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} catch (InterruptedException | NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
		btnLogin.setBounds(171, 6, 89, 23);
		panel.add(btnLogin);

		JButton btnPassword = new JButton("Password");
		btnPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection testData;

				//  Add some test data

				testData = new StringSelection(Password);

				c.setContents(testData, testData);

				//  Get clipboard contents, as a String

				Transferable t = c.getContents( null );

				if ( t.isDataFlavorSupported(DataFlavor.stringFlavor) )
				{
					try {
						String data = (String)t.getTransferData( DataFlavor.stringFlavor );
						System.out.println( "Clipboard contents: " + data );
					} catch (UnsupportedFlavorException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				try {

					Prefs ps=new Prefs();

					int delay=Integer.parseInt(ps.getProp("clearDelay"));

					Thread.sleep(delay*1000);
					testData = new StringSelection("");

					c.setContents(testData, testData);

					//  Get clipboard contents, as a String

					t = c.getContents( null );

					if ( t.isDataFlavorSupported(DataFlavor.stringFlavor) )
					{
						try {
							String data = (String)t.getTransferData( DataFlavor.stringFlavor );
							System.out.println( "Clipboard contents: " + data );
						} catch (UnsupportedFlavorException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (InterruptedException | NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPassword.setBounds(279, 5, 89, 23);
		panel.add(btnPassword);
		setSize(400,100);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
	}

}
