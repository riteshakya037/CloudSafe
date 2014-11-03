package SafeUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import Algo.AESEncrypter;
import Algo.ClientSync;
import Algo.PassGenerate;
import Algo.Prefs;
import Algo.dbAction;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
public class SafeMain extends JFrame implements ActionListener, ListSelectionListener, MouseListener {
	JTextField txtCardSearch;
	JMenuBar mb;
	JMenu mFile,mEdit,mTools,mHelp;
	JMenuItem miAddCard,miAddNote,miSync,miLock,miClose,miCut,miCopy,miPaste,miDelete,miChangePassword,miGeneratePassword,miBackup,miRestore,miEraseData,miOption,miHelp,miAbout;
	JPanel mainPanel,labelPanel,cardListPanel,cardDisplayPanel,cardListSearch;
	TrayIcon trayIcon;
	SystemTray tray;
	private JList lstLabel;
	JPanel panelAdd;
	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db",labelQuery="SELECT * FROM Master_Table where not trash and not template  ORDER BY name COLLATE NOCASE;";
	private JScrollPane scrollPane;
	private static JList lstCards;
	private JPanel panelCard;
	final static String index[]=new String[100];
	private JToolBar toolBar;
	private JPanel panel;
	private JButton btnAddCard,btnAddNote,btnTrash,btnEdit,btnSync,btnLock;
	private static JLabel lblNull;
	private JButton btnRestore;
	private JLabel btnErase;
	private JButton btnFavourite;
	private JButton btnRemoveFavourite;
	private JLabel Site,Pass;
	private String url;
	private JLabel lblSearchIco;
	private JPanel panel_1;
	private JButton btnDelete;
	public static JLabel lblSyncing;

	public SafeMain() {
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				System.out.println("SUUUU");
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(SafeMain.class.getResource("/Resource/Icon.png")));

		mb=new JMenuBar();
		mb.setFocusable(false);
		mb.setBorderPainted(false);
		mb.setBorder(null);

		mFile=new JMenu("File");
		mFile.setBackground(Color.BLACK);
		mEdit=new JMenu("Edit");
		mTools=new JMenu("Tools");
		mHelp=new JMenu("Help");

		miAddCard=new JMenuItem("Add Card");
		miAddCard.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/vcard_add.png")));
		miAddNote=new JMenuItem("Add Note   ");
		miAddNote.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/note.png")));
		miAddNote.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));

		miSync=new JMenuItem("Sync");
		miSync.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/arrow_refresh.png")));
		miLock=new JMenuItem("Lock");
		miLock.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/lock.png")));
		miClose=new JMenuItem("Close");
		miCut=new JMenuItem("Cut");
		miCut.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/cut.png")));
		miCopy=new JMenuItem("Copy");
		miCopy.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/page_copy.png")));
		miPaste=new JMenuItem("Paste");
		miPaste.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/paste_plain.png")));
		miDelete=new JMenuItem("Delete          ");
		miChangePassword=new JMenuItem("Change Password");
		miChangePassword.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/change_password.png")));
		miGeneratePassword=new JMenuItem("Generate Password");
		miGeneratePassword.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/key.png")));
		miGeneratePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PassGenerate();
			}
		});
		miBackup=new JMenuItem("Backup");
		miRestore=new JMenuItem("Restore");
		miEraseData=new JMenuItem("Erase Data");
		miOption=new JMenuItem("Options");
		miOption.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/cog.png")));
		miHelp =new JMenuItem("Help         ");
		miHelp.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/help.png")));
		miAbout=new JMenuItem("About");
		miAbout.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/information.png")));

		mFile.setMnemonic(KeyEvent.VK_F);
		mEdit.setMnemonic(KeyEvent.VK_E);
		mTools.setMnemonic(KeyEvent.VK_T);
		mHelp.setMnemonic(KeyEvent.VK_H);

		miLock.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		miCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		miPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		miCut.  setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		miDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
		miAddCard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		miClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		miOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
		miHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));

		mainPanel=new JPanel(new BorderLayout());
		labelPanel=new JPanel();
		labelPanel.setPreferredSize(new Dimension(175,0));

		cardListPanel=new JPanel(new BorderLayout());
		cardDisplayPanel=new JPanel();
		cardDisplayPanel.setBackground(Color.GRAY);
		cardDisplayPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cardDisplayPanel.setPreferredSize(new Dimension(450, 150));

		cardListSearch=new JPanel(new BorderLayout());
		cardListSearch.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		cardListSearch.setBackground(Color.WHITE);

		cardListPanel.add(cardListSearch,BorderLayout.PAGE_START);
		cardListPanel.setBorder(new MatteBorder(0, 1, 0, 1, (Color) new Color(0, 0, 0)));


		setContentPane(mainPanel);
		mainPanel.add(labelPanel, BorderLayout.LINE_START);

		initizlizeLabels();
		labelPanel.setLayout(new BorderLayout(0, 0));
		labelPanel.add(lstLabel);

		lblSyncing = new JLabel("Syncing...");
		lblSyncing.setVisible(false);
		lblSyncing.setHorizontalAlignment(SwingConstants.CENTER);
		labelPanel.add(lblSyncing, BorderLayout.SOUTH);
		mainPanel.add(cardListPanel, BorderLayout.CENTER);
		mainPanel.add(cardDisplayPanel, BorderLayout.LINE_END);
		cardDisplayPanel.setLayout(new BorderLayout(0, 0));

		panelCard = new JPanel();
		panelCard.setBackground(Color.WHITE);
		cardDisplayPanel.add(panelCard, BorderLayout.NORTH);
		panelCard.setLayout(new BoxLayout(panelCard, BoxLayout.Y_AXIS));

		txtCardSearch=new JTextField();
		txtCardSearch.setForeground(new Color(0, 0, 0));
		txtCardSearch.setBackground(Color.WHITE);
		txtCardSearch.setPreferredSize(new Dimension(0, 0));
		txtCardSearch.setBorder(new MatteBorder(0, 0, 4, 0, (Color) Color.LIGHT_GRAY));
		txtCardSearch.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if(txtCardSearch.getText().length()==0){
					searchCardList(labelQuery);
					btnErase.setEnabled(false);
				}
				else
				{
					searchCardList("SELECT * FROM Master_Table where name like '"+ txtCardSearch.getText().toString()+"%'");
					btnErase.setEnabled(true);
				}

			}

		});
		cardListSearch.add(txtCardSearch, BorderLayout.CENTER);

		btnErase = new JLabel();
		btnErase.setBackground(new Color(211, 211, 211));
		btnErase.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/cross.png")));
		btnErase.setEnabled(false);
		btnErase.setHorizontalAlignment(SwingConstants.CENTER);
		btnErase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtCardSearch.setText(null);
				updateCardList();
			}
		});

		btnErase.setFocusable(false);
		btnErase.setFocusTraversalKeysEnabled(false);

		btnErase.setPreferredSize(new Dimension(32, 32));
		cardListSearch.add(btnErase, BorderLayout.EAST);

		lblSearchIco = new JLabel("");
		lblSearchIco.setBackground(new Color(211, 211, 211));
		lblSearchIco.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchIco.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/zoom.png")));
		lblSearchIco.setPreferredSize(new Dimension(32, 32));
		cardListSearch.add(lblSearchIco, BorderLayout.WEST);


		initizlizeCardList("SELECT * FROM Master_Table WHERE not trash AND not template ORDER BY name COLLATE NOCASE;");


		cardListPanel.add(scrollPane, BorderLayout.CENTER);

		lblNull = new JLabel("No Cards Found");

		lblNull.setHorizontalAlignment(SwingConstants.CENTER);
		lblNull.setVisible(false);
		cardListPanel.add(lblNull, BorderLayout.SOUTH);

		panel = new JPanel();
		mainPanel.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		toolBar = new JToolBar();
		toolBar.setBorder(null);
		panel.add(toolBar);

		btnAddCard = new JButton("Add Card");
		btnAddCard.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/vcard_add.png")));
		btnAddCard.setMnemonic('C');
		btnAddCard.setFocusable(false);
		btnAddCard.setPreferredSize(new Dimension(80, 5));
		btnAddCard.setBorder(new CompoundBorder());
		toolBar.add(btnAddCard);

		btnAddNote = new JButton("Add Note");
		btnAddNote.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/note.png")));
		btnAddNote.setFocusable(false);
		btnAddNote.setPreferredSize(new Dimension(80, 30));
		btnAddNote.setBorder(new CompoundBorder());
		toolBar.add(btnAddNote);

		btnTrash = new JButton("Trash");
		btnTrash.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/bin_recycle.png")));
		btnTrash.setFocusable(false);
		btnTrash.setPreferredSize(new Dimension(80, 30));
		btnTrash.setBorder(new CompoundBorder());
		toolBar.add(btnTrash);

		btnRestore = new JButton("Restore");
		btnRestore.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/arrow_undo.png")));
		btnRestore.setFocusable(false);
		btnRestore.setVisible(false);
		btnRestore.setPreferredSize(new Dimension(80, 30));
		btnRestore.setBorder(new CompoundBorder());
		toolBar.add(btnRestore);
		btnRestore.addActionListener(this);

		btnDelete = new JButton("Delete");
		btnDelete.setVisible(false);
		btnDelete.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/delete.png")));
		btnDelete.setPreferredSize(new Dimension(80, 30));
		btnDelete.setFocusable(false);
		btnDelete.setBorder(new CompoundBorder());
		toolBar.add(btnDelete);
		btnDelete.addActionListener(this);

		btnEdit = new JButton("Edit");
		btnEdit.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/pencil.png")));
		btnEdit.setFocusable(false);
		btnEdit.setPreferredSize(new Dimension(80, 30));
		btnEdit.setBorder(new CompoundBorder());

		toolBar.add(btnEdit);

		btnFavourite = new JButton("Favourite");
		btnFavourite.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/star.png")));
		btnFavourite.setPreferredSize(new Dimension(80, 30));
		btnFavourite.setFocusable(false);
		btnFavourite.setBorder(new CompoundBorder());
		toolBar.add(btnFavourite);

		btnRemoveFavourite = new JButton("DeFavourite");
		btnRemoveFavourite.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/draw_star.png")));
		btnRemoveFavourite.setVisible(false);
		btnRemoveFavourite.setPreferredSize(new Dimension(80, 30));
		btnRemoveFavourite.setFocusable(false);
		btnRemoveFavourite.setBorder(new CompoundBorder());
		toolBar.add(btnRemoveFavourite);

		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setBackground(new Color(210, 210, 210));
		toolBar.add(panel_1);

		btnSync = new JButton("Sync");
		btnSync.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/arrow_refresh.png")));
		btnSync.setFocusable(false);
		btnSync.setPreferredSize(new Dimension(60, 30));
		btnSync.setBorder(new CompoundBorder());
		toolBar.add(btnSync);

		btnLock = new JButton("Lock");
		btnLock.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/lock.png")));
		btnLock.setFocusable(false);
		btnLock.setPreferredSize(new Dimension(80, 30));
		btnLock.setBorder(new CompoundBorder());
		toolBar.add(btnLock);
		setJMenuBar(mb);
		mb.add(mFile);
		mb.add(mEdit);
		mb.add(mTools);
		mb.add(mHelp);

		mFile.add(miAddCard);
		mFile.add(miAddNote);
		mFile.addSeparator();
		mFile.add(miSync);
		mFile.add(miLock);
		mFile.addSeparator();
		mFile.add(miClose);

		mEdit.add(miCut);
		mEdit.add(miCopy);
		mEdit.add(miPaste);
		mEdit.add(miDelete);

		mTools.add(miChangePassword);
		mTools.add(miGeneratePassword);
		mTools.addSeparator();
		mTools.add(miBackup);
		mTools.add(miRestore);
		mTools.add(miEraseData);
		mTools.addSeparator();
		mTools.add(miOption);

		mHelp.add(miHelp);
		mHelp.add(miAbout);

		miAddCard.addActionListener(this);
		miClose.addActionListener(this);
		miChangePassword.addActionListener(this);
		miLock.addActionListener(this);
		miEraseData.addActionListener(this);
		miAddNote.addActionListener(this);
		miSync.addActionListener(this);
		btnAddNote.addActionListener(this);
		btnEdit.addActionListener(this);
		btnLock.addActionListener(this);
		btnTrash.addActionListener(this);
		btnAddCard.addActionListener(this);
		btnFavourite.addActionListener(this);
		btnRemoveFavourite.addActionListener(this);
		btnSync.addActionListener(this);
		miOption.addActionListener(this);

		ClientSync cs=new ClientSync();
		String chk=cs.getString();
		if(chk.equals("")){
			System.out.println("No Connection");
			btnSync.setEnabled(false);
			miSync.setEnabled(false);
		}

		setMinimumSize(new Dimension(950,600));
		setSize(950 ,600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("CloudSafe");
		setLocationRelativeTo(null);
		//		setResizable(false);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtCardSearch, lstCards}));
		setVisible(true);
		addWindowListener(new WindowAdapter() { 
			@Override
			public void windowClosing(WindowEvent windowEvent) 
			{

				close();
			}

		});

	}

	//	private void HideToTray(){
	//		if(SystemTray.isSupported()){
	//			tray=SystemTray.getSystemTray();
	//
	//			Image image=Toolkit.getDefaultToolkit().getImage(SafeMain.class.getResource(""));
	//			ActionListener exitListener=new ActionListener() {
	//				public void actionPerformed(ActionEvent e) {
	//					close();
	//
	//				}
	//			};
	//
	//
	//
	//
	//			PopupMenu popup=new PopupMenu();
	//			MenuItem defaultItem=new MenuItem("Exit");
	//			defaultItem.addActionListener(exitListener);
	//			popup.add(defaultItem);
	//
	//			defaultItem=new MenuItem("Open");
	//			defaultItem.addActionListener(new ActionListener() {
	//				public void actionPerformed(ActionEvent e) {
	//					dispose();
	//					new SafeLogin();
	//					setExtendedState(JFrame.NORMAL);
	//					tray.remove(trayIcon);
	//
	//
	//				}
	//			});
	//			popup.add(defaultItem);
	//			trayIcon=new TrayIcon(image, "CloudSafe", popup);
	//			trayIcon.setImageAutoSize(true);
	//		}
	//		else
	//		{
	//		}
	//		try 
	//		{
	//			tray.add(trayIcon);
	//			trayIcon.displayMessage("CloudSafe", "Minimized", TrayIcon.MessageType.INFO);
	//			setVisible(false);
	//		} 
	//		catch (AWTException ex) {
	//		}
	//				addWindowStateListener(new WindowStateListener() {
	//					public void windowStateChanged(WindowEvent e) {
	//						if(e.getNewState()==ICONIFIED){
	//							try {
	//								tray.add(trayIcon);
	//								setVisible(false);
	//								System.out.println("added to SystemTray");
	//							} catch (AWTException ex) {
	//								System.out.println("unable to add to tray");
	//							}
	//						}
	//						if(e.getNewState()==7){
	//							try{
	//								tray.add(trayIcon);
	//								setVisible(false);
	//								System.out.println("added to SystemTray");
	//							}catch(AWTException ex){
	//								System.out.println("unable to add to system tray");
	//							}
	//						}
	//						if(e.getNewState()==MAXIMIZED_BOTH){
	//							tray.remove(trayIcon);
	//							setVisible(true);
	//							System.out.println("Tray icon removed");
	//						}
	//						if(e.getNewState()==NORMAL){
	//							tray.remove(trayIcon);
	//							setVisible(true);
	//							System.out.println("Tray icon removed");
	//						}
	//					}
	//				});
	//
	//	}

	protected void searchCardList(String sql) {
		DefaultListModel listModel = new DefaultListModel();
		listModel.removeAllElements();
		Connection c = null;
		Statement stmt = null;
		int i=0;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int j = 0;
			while(rs.next()) {
				j++;
				System.out.print(j);
			}
			if(j==0)
				lblNull.setVisible(true);
			else
				lblNull.setVisible(false);
			String[] count= new String[j];

			rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				count[i]=rs.getString("Name");
				listModel.addElement(rs.getString("Name"));
				System.out.println(rs.getString("Name"));
				index[i]=rs.getString("ID");
				i++;
			}
			rs.close();
			stmt.close();
			c.close();
			lstCards.setListData(count);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}


	}

	private void initizlizeCardList(String sql) {

		DefaultListModel listModel = new DefaultListModel();
		Connection c = null;
		Statement stmt = null;
		int i=0;
		String[] count=null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int j = 0;
			while(rs.next()) {
				j++;
			}
			count= new String[j];
			rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				count[i]=rs.getString("Name");
				listModel.addElement(rs.getString("Name"));
				System.out.println(rs.getString("Name"));
				index[i]=rs.getString("ID");
				i++;

			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
		lstCards=new JList(count);
		lstCards.setBorder(null);
		lstCards.setBackground(Color.WHITE);
		lstCards.setSelectedIndex(0);
		lstCards.setSelectionForeground(new Color(0, 0, 0));
		lstCards.setFixedCellHeight(50);
		lstCards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstCards.setSelectionBackground(new Color(209, 229, 252));
		lstCards.setForeground(Color.BLACK);

		scrollPane = new JScrollPane(lstCards);
		scrollPane.setBorder(null);
		lstCards.addMouseListener(this);
	}
	public static void updateCardList() {

		DefaultListModel listModel = new DefaultListModel();
		listModel.removeAllElements();
		Connection c = null;
		Statement stmt = null;
		int i=0;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(labelQuery);
			int j = 0;
			while(rs.next()) {
				j++;
				System.out.print(j);
			}
			if(j==0)
				lblNull.setVisible(true);
			else
				lblNull.setVisible(false);
			String[] count= new String[j];

			rs = stmt.executeQuery(labelQuery);
			while ( rs.next() ) {
				count[i]=rs.getString("Name");
				listModel.addElement(rs.getString("Name"));
				System.out.println(rs.getString("Name"));
				index[i]=rs.getString("ID");
				i++;
			}
			rs.close();
			stmt.close();
			c.close();
			lstCards.setListData(count);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}



	}

	private void initizlizeLabels() {
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("All Cards");
		listModel.addElement("Favourite");
		listModel.addElement("Templates");
		listModel.addElement("Trash");
		lstLabel=new JList();
		lstLabel.setModel(listModel);
		lstLabel.setFixedCellHeight(32);
		lstLabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstLabel.setSelectionBackground(new Color(146, 146, 146));
		lstLabel.setForeground(Color.WHITE);
		lstLabel.setBackground(Color.GRAY);
		lstLabel.addListSelectionListener(this);
	}

	protected void close() {
		int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to close CloudSafe?", "Really Closing?",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		ClientSync cs=new ClientSync();
		String chk=cs.getString();
		if (result == JOptionPane.YES_OPTION) 
		{
			if(!chk.equals(""))
			{
				String user = null,pass = null;
				Prefs ps=new Prefs();
				try {
					user=ps.getProp("User");
					pass=ps.getProp("Password");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new ClientSync(user,pass,"true");
			}
			AESEncrypter aes;
			try {
				aes = new AESEncrypter();
				aes.encrypt();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==miClose)
		{
			close();
		}
		if(e.getSource()==miAddCard || e.getSource()==btnAddCard)
		{
			new TemplateSelect(this);
		}

		if(e.getSource()==miChangePassword)
		{
			new newPassword(this);
		}
		if(e.getSource()==miLock || e.getSource()==btnLock)
		{
			this.dispose();
			new SafeLogin();

		}
		if(e.getSource()==miEraseData)
		{
			int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete Database?", "Really Delete?",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) 
			{
				dbAction db=new dbAction();
				db.delete();
			}
		}
		if(e.getSource()==btnEdit){
			formSelect();

		}
		if(e.getSource()==btnAddNote || e.getSource()==miAddNote){
			new newNote("1");
		}
		if(e.getSource()==btnTrash){
			dbAction db=new dbAction();
			db.moveTrash(index[lstCards.getSelectedIndex()], 1);
			updateCardList();		
		}
		if(e.getSource()==btnDelete){
			int result = JOptionPane.showConfirmDialog(null,"Do you want to delete "+lstCards.getSelectedValue()+"?", "Really Closing?",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.YES_OPTION) 
			{
				dbAction db=new dbAction();
				db.deleteCard(index[lstCards.getSelectedIndex()]);
			}
			updateCardList();

		}

		if(e.getSource()==btnRestore){
			dbAction db=new dbAction();
			db.moveTrash(index[lstCards.getSelectedIndex()], 0);
			updateCardList();		
		}
		if(e.getSource()==btnFavourite){
			dbAction db=new dbAction();
			db.moveFav(index[lstCards.getSelectedIndex()], 1);
			updateCardList();		
		}
		if(e.getSource()==btnRemoveFavourite){
			dbAction db=new dbAction();
			db.moveFav(index[lstCards.getSelectedIndex()], 0);
			updateCardList();		
		}
		if(e.getSource()==btnSync || e.getSource()==miSync){
			String user = null,pass = null;
			Prefs ps=new Prefs();
			try {
				user=ps.getProp("User");
				pass=ps.getProp("Password");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new ClientSync(user,pass,"true");
			updateCardList();
		}
		if(e.getSource()==miOption){
			new optionUI();
		}
	}

	private void formSelect() {
		Connection c=null;
		Statement stmt=null;
		int check=1;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Master_table where ID="+index[lstCards.getSelectedIndex()]+";");
			check=rs.getInt("IS_NOTE");
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception ex ) {
			System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );

		}	
		if(check==1)
		{
			new newNote(index[lstCards.getSelectedIndex()]);
		}
		else
		{
			new newCard(index[lstCards.getSelectedIndex()]);

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource()==lstLabel){
			System.out.print("Change");
			if(lstLabel.getSelectedValue().toString() == "Trash"){
				labelQuery="Select * from Master_Table where Trash order by name COLLATE NOCASE;";
				updateCardList();
				btnDelete.setVisible(true);
				btnTrash.setVisible(false);
				btnRestore.setVisible(true);
				btnRemoveFavourite.setVisible(false);
				btnFavourite.setVisible(false);
			}
			if(lstLabel.getSelectedValue().toString() == "Templates"){
				labelQuery="Select * from Master_Table where Template and not trash order by name COLLATE NOCASE;";
				updateCardList();
				btnDelete.setVisible(false);
				btnTrash.setVisible(true);
				btnRestore.setVisible(false);
				btnRemoveFavourite.setVisible(false);
				btnFavourite.setVisible(false);
			}
			if(lstLabel.getSelectedValue().toString() == "All Cards"){
				labelQuery="SELECT * FROM Master_Table where not trash and not template order by name COLLATE NOCASE;";
				updateCardList();
				btnDelete.setVisible(false);
				btnTrash.setVisible(true);
				btnRestore.setVisible(false);
				btnRemoveFavourite.setVisible(false);
				btnFavourite.setVisible(true);
			}
			if(lstLabel.getSelectedValue().toString() == "Favourite"){
				labelQuery="Select * from Master_Table where Star and not trash order by name COLLATE NOCASE;";
				updateCardList();
				btnDelete.setVisible(false);
				btnTrash.setVisible(true);
				btnRestore.setVisible(false);
				btnRemoveFavourite.setVisible(true);
				btnFavourite.setVisible(false);
			}
		}


	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getClickCount() == 1 && evt.getSource()==lstCards) {
			if(lstCards.getModel().getSize()!=0)
				displayCard();

		}
		if (evt.getClickCount() == 2 && evt.getSource()==lstCards) {
			if(lstCards.getModel().getSize()!=0)
				//				new newCard(index[lstCards.getSelectedIndex()]);
				formSelect();
		}
	}


	public void displayCard() {
		panelCard.removeAll();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println(index[lstCards.getSelectedIndex()]);
			int i =0;
			stmt = c.createStatement();
			ResultSet rs;
			rs= stmt.executeQuery("select * from Master_table where ID ="+index[lstCards.getSelectedIndex()]);

			JLabel lblNote=new JLabel();
			JPanel panelNote=new JPanel();
			panelNote.setLayout(null);

			lblNote.setText(rs.getString("Name"));
			panelNote.add(lblNote);
			lblNote.setBounds(35, 10, 400, 25);
			lblNote.setHorizontalAlignment(SwingConstants.CENTER);
			lblNote.setFont(new Font("Tahoma", Font.PLAIN, 18));

			JLabel txtNote=new JLabel();
			panelNote.add(txtNote);
			if(rs.getString("Notes")==null)
				txtNote.setText("");
			else
				txtNote.setText("Notes: "+rs.getString("Notes"));	
			txtNote.setBounds(35, 60, 400, 25);

			JLabel txtTime=new JLabel();
			txtTime.setHorizontalAlignment(SwingConstants.CENTER);
			panelNote.add(txtTime);

			txtTime.setText("Last Modified: "+rs.getTimestamp("TIME_STAMP"));
			txtTime.setBounds(35, 35, 400, 25);


			panelNote.setBackground(Color.WHITE);
			panelNote.setPreferredSize(new Dimension(100,90));
			panelCard.add(panelNote);
			stmt = c.createStatement();
			JLabel[] lblAdd=new JLabel[10];
			JLabel[] txtAdd=new JLabel[10];
			rs = stmt.executeQuery("select * from entry"+index[lstCards.getSelectedIndex()]);

			while ( rs.next() ) {
				lblAdd[i]=new JLabel();
				txtAdd[i]=new JLabel();
				panelAdd=new JPanel();
				panelAdd.setLayout(null);
				panelAdd.add(lblAdd[i]);

				lblAdd[i].setText(rs.getString("FieldName"));
				lblAdd[i].setBounds(5, 5,100, 25);
				if(rs.getString("type").startsWith("website")){
					Site=new JLabel();
					url=rs.getString("Value");
					Site.setCursor(new Cursor(Cursor.HAND_CURSOR));
					Site.setToolTipText("Open in Browser");
					Site.setText("<html><a href='#'>"+rs.getString("Value")+"</a></html>");
					Site.addMouseListener(this);
					panelAdd.add(Site);
					Site.setBounds(35, 35, 400, 25);
				}
				else if(rs.getString("type").startsWith("password")){
					Pass=new JLabel();
					Pass.setCursor(new Cursor(Cursor.HAND_CURSOR));
					Pass.setToolTipText("Copy to ClipBoard");
					Pass.setText(rs.getString("Value"));
					Pass.addMouseListener(this);
					panelAdd.add(Pass);
					Pass.setBounds(35, 35, 400, 25);
				}
				else{
					txtAdd[i].setText(rs.getString("Value"));
					panelAdd.add(txtAdd[i]);
					txtAdd[i].setBounds(35, 35, 400, 25);
				}
				i++;
				panelAdd.setBackground(Color.WHITE);
				panelAdd.setPreferredSize(new Dimension(100,60));
				panelCard.add(panelAdd);
				validate();
				System.out.println(rs.getString("FieldName")+" "+ rs.getString("Value"));
			}
			rs.close();
			validate();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getSource()==Site){
			try {
				new ToolBar(index[lstCards.getSelectedIndex()]);
				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception ex) {
				//It looks like there's a problem
			}
		}
		if(arg0.getSource()==Pass){
			Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection testData;

			//  Add some test data

			testData = new StringSelection(Pass.getText());

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
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public static void chgSyncLabel(boolean cdn){
		lblSyncing.setVisible(cdn);
	}

}
