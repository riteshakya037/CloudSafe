package SafeUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import Algo.dbAction;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class newNote extends JDialog implements ActionListener {
	public JTextField txtCardName;
	public JTabbedPane tabbedPane;
	public JPanel panelAdd;
	static int i =0,tempCheck=1;
	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db",tableNumber;
	JButton btnSave,btnOrganize;
	JLabel[] lblAdd=new JLabel[10];
	JTextField[] txtAdd=new JTextField[10];
	String[] type=new String[10];
	JTextPane txtNote;
	JMenuItem miSave,miDiscard,miCopy,miClose,miCut,miPaste,miDelete;
	private String tableNo;
	public newNote(String tableNo) {
		this.tableNo=tableNo;
		setTitle("Add Note");
		setIconImage(Toolkit.getDefaultToolkit().getImage(newNote.class.getResource("/Resource/note.png")));
		getContentPane().setLayout(null);
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 444, 23);
		getContentPane().add(toolBar);

		btnSave = new JButton("Save and Close");
		btnSave.setIcon(new ImageIcon(newNote.class.getResource("/Resource/disk.png")));
		toolBar.add(btnSave);
		btnSave.addActionListener(this);

		JPanel panel = new JPanel();
		panel.setBounds(0, 23, 444, 85);
		getContentPane().add(panel);
		panel.setLayout(null);

		txtCardName=new JTextField();
		txtCardName.setBounds(24, 22, 275, 32);
		panel.add(txtCardName);
		txtCardName.setColumns(10);

		JLabel lblCardIcon = new JLabel("");
		lblCardIcon.setBackground(Color.BLACK);
		lblCardIcon.setBounds(348, 11, 55, 58);
		panel.add(lblCardIcon);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 108, 444, 374);
		getContentPane().add(tabbedPane);

		JPanel notePanel = new JPanel();
		tabbedPane.addTab("Notes", null, notePanel, null);
		notePanel.setLayout(new BorderLayout(0, 0));

		txtNote = new JTextPane();
		txtNote.setText("");
		notePanel.add(txtNote);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mCard = new JMenu("Card");
		mCard.setMnemonic('c');
		menuBar.add(mCard);

		miSave = new JMenuItem("Exit and Save");
		miSave.setIcon(new ImageIcon(newNote.class.getResource("/Resource/disk.png")));
		miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSave.addActionListener(this);
		mCard.add(miSave);

		miDiscard = new JMenuItem("Discard Changes");
		miDiscard.addActionListener(this);;
		mCard.add(miDiscard);

		mCard.addSeparator();

		miClose = new JMenuItem("Close Window");
		miClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		miClose.addActionListener(this);;
		mCard.add(miClose);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('e');
		menuBar.add(mnEdit);

		miCut = new JMenuItem("Cut");
		miCut.setIcon(new ImageIcon(newNote.class.getResource("/Resource/cut.png")));
		miCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnEdit.add(miCut);

		miCopy = new JMenuItem("Copy");
		miCopy.setIcon(new ImageIcon(newNote.class.getResource("/Resource/page_copy.png")));
		miCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnEdit.add(miCopy);

		miPaste = new JMenuItem("Paste");
		miPaste.setIcon(new ImageIcon(newNote.class.getResource("/Resource/paste_plain.png")));
		miPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnEdit.add(miPaste);

		miDelete = new JMenuItem("Delete");
		miDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		mnEdit.add(miDelete);
		
		if(tableNo!="1"){
			Connection c;
			Statement stmt;
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");
				setTitle("Edit Note");

				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("select * from Master_table where ID="+tableNo+";");
				txtCardName.setText(rs.getString("Name"));
				txtNote.setText(rs.getString("Notes"));
				rs.close();
				stmt.close();
				c.close();
			} catch (Exception ex ) {
				System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
				
			}		
		}

		setModal(true);
		setResizable(false);
		setSize(450, 531);setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtCardName, txtNote}));
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent ae) {


		if(ae.getSource()==btnSave|| ae.getSource()==miSave){

			java.util.Date date = new java.util.Date();
			long t = date.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
			if(tableNo=="1"){
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			dbAction.addEntry(Integer.toString(n),txtCardName.getText().toString(),0,0,txtNote.getText().toString(),sqlTimestamp,1);
			
			}
			else
			{
				dbAction.updateMaster(tableNo, txtCardName.getText().toString(), 0,0, txtNote.getText().toString(), sqlTimestamp);
			}
			this.dispose();
			SafeMain.updateCardList();
		}
		if(ae.getSource()==miDiscard || ae.getSource()==miClose){
			this.dispose();

		}
	}
}
