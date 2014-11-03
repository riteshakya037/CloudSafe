package SafeUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import Algo.PassGenerate;
import Algo.dbAction;

@SuppressWarnings("serial")
public class newCard extends JDialog implements ActionListener {
	public JTextField txtCardName;
	public JTabbedPane tabbedPane;
	public JPanel fieldPanel,panelAdd,panelFields;
	static int i =0,tempCheck=1;
	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db",tableNumber;
	JButton btnSave,btnOrganize;
	JLabel[] lblAdd=new JLabel[10];
	JTextField[] txtAdd=new JTextField[10];
	String[] type=new String[10];
	JTextPane txtNote;
	JMenuItem miSave,miSaveTemplate,miDiscard,miCopy,miClose,miCut,miPaste,miDelete;
	private JButton btnGeneratePassword;
	public newCard(String tableNo) {
		Connection c = null;
		Statement stmt = null;
		getContentPane().setLayout(null);
		tableNumber=tableNo;
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBounds(0, 0, 444, 23);
		getContentPane().add(toolBar);

		btnSave = new JButton("Save and Close");
		btnSave.setIcon(new ImageIcon(newCard.class.getResource("/Resource/disk.png")));
		toolBar.add(btnSave);
		btnSave.addActionListener(this);

		//		btnOrganize = new JButton("Organize fileds");
		//		btnOrganize.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent arg0) {
		//			}
		//		});
		//		toolBar.add(btnOrganize);

		btnGeneratePassword = new JButton("Generate Password");
		btnGeneratePassword.setIcon(new ImageIcon(newCard.class.getResource("/Resource/key.png")));
		toolBar.add(btnGeneratePassword);
		btnGeneratePassword.addActionListener(this);

		JPanel panel = new JPanel();
		panel.setBounds(0, 23, 444, 85);
		getContentPane().add(panel);
		panel.setLayout(null);



		JLabel lblCardIcon = new JLabel("");
		lblCardIcon.setBackground(Color.BLACK);
		lblCardIcon.setBounds(348, 11, 55, 58);
		panel.add(lblCardIcon);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 108, 444, 408);
		getContentPane().add(tabbedPane);

		fieldPanel = new JPanel();
		tabbedPane.addTab("Fields", null, fieldPanel, null);
		fieldPanel.setLayout(new BorderLayout(0, 0));

		//		JButton btnAddNew = new JButton("Add Another Field");
		//		btnAddNew.addActionListener(new ActionListener() {
		//			public void actionPerformed(ActionEvent arg0) {
		//				addField("Test","Lame","Text");
		//			}
		//		});
		//		panelAdd.add(btnAddNew);


		panelFields = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panelFields);
		scrollPane.setMaximumSize(new Dimension(439, 346));
		panelFields.setLayout(new GridLayout(0,1));
		scrollPane.setBorder(null);
		fieldPanel.add(scrollPane, BorderLayout.NORTH);




		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			i=0;
			ResultSet rs = stmt.executeQuery("select * from entry"+tableNo);
			while ( rs.next() ) {
				addField(rs.getString("FieldName"),rs.getString("Value"),rs.getString("Type"));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			
		}
		JPanel notePanel = new JPanel();
		tabbedPane.addTab("Notes", null, notePanel, null);
		notePanel.setLayout(new BorderLayout(0, 0));

		txtNote = new JTextPane();

		notePanel.add(txtNote);

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Master_table where ID="+tableNo+";");
			while ( rs.next() ) {
				tempCheck=rs.getInt("Template");
				if(tempCheck==1){
					txtCardName = new JTextField();
					setTitle("Add Card");
					setIconImage(Toolkit.getDefaultToolkit().getImage(newCard.class.getResource("/Resource/vcard_add.png")));

				}
				else{
					txtCardName = new JTextField(rs.getString("Name"));
					setTitle("Edit Card");		
					setIconImage(Toolkit.getDefaultToolkit().getImage(newCard.class.getResource("/Resource/vcard_edit.png")));


				}
				System.out.println(rs.getString("ID"));
				txtNote.setText(rs.getString("notes"));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			
		}
		txtCardName.setBounds(24, 22, 275, 32);
		panel.add(txtCardName);
		txtCardName.setColumns(10);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mCard = new JMenu("Card");
		mCard.setMnemonic('c');
		menuBar.add(mCard);

		miSave = new JMenuItem("Exit and Save");
		miSave.setIcon(new ImageIcon(newCard.class.getResource("/Resource/disk.png")));
		miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSave.addActionListener(this);
		mCard.add(miSave);

		miSaveTemplate = new JMenuItem("Save as Template");
		miSaveTemplate.setIcon(new ImageIcon(newCard.class.getResource("/Resource/save_as.png")));
		miSaveTemplate.addActionListener(this);
		mCard.add(miSaveTemplate);

		miDiscard = new JMenuItem("Discard Changes");
		miDiscard.addActionListener(this);;
		mCard.add(miDiscard);

		mCard.addSeparator();

		miClose = new JMenuItem("Close Window");
		miClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		miClose.addActionListener(this);;
		mCard.add(miClose);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setEnabled(false);
		mnEdit.setMnemonic('e');
		menuBar.add(mnEdit);

		miCut = new JMenuItem("Cut");
		miCut.setIcon(new ImageIcon(newCard.class.getResource("/Resource/cut.png")));
		miCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnEdit.add(miCut);

		miCopy = new JMenuItem("Copy");
		miCopy.setIcon(new ImageIcon(newCard.class.getResource("/Resource/page_copy.png")));
		miCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnEdit.add(miCopy);

		miPaste = new JMenuItem("Paste");
		miPaste.setIcon(new ImageIcon(newCard.class.getResource("/Resource/paste_plain.png")));
		miPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnEdit.add(miPaste);

		miDelete = new JMenuItem("Delete");
		miDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		mnEdit.add(miDelete);

		setModal(true);
		setResizable(false);
		setSize(450, 565);setLocationRelativeTo(null);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private void addField(String FieldName,String Value,String Type) {
		lblAdd[i]=new JLabel();
		txtAdd[i]=new JTextField();
		panelAdd=new JPanel();
		panelAdd.setLayout(null);
		panelAdd.add(lblAdd[i]);
		panelAdd.add(txtAdd[i]);

		lblAdd[i].setText(FieldName);
		lblAdd[i].setBounds(5, 5,100, 25);
		txtAdd[i].setText(Value);
		type[i]=Type;
		txtAdd[i].setBounds(5, 35, 400, 25);
		if(Type.equals("password")){
			txtAdd[i].setBounds(5, 35, 368, 25);
			JButton rpg=new JButton();
			rpg.setIcon(new ImageIcon(SafeMain.class.getResource("/Resource/key.png")));
			rpg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new PassGenerate();

				}
			});
			rpg.setBounds(368, 35, 32, 25);
			panelAdd.add(rpg);
		}

		i++;
		panelAdd.setPreferredSize(new Dimension(100,60));
		panelFields.add(panelAdd);	
		validate();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {


		if(ae.getSource()==btnSave|| ae.getSource()==miSave)
		{
			System.out.println("IN"+i);
			java.util.Date date = new java.util.Date();
			long t = date.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
			if(tempCheck==1){
				Random rnd = new Random();
				int n = 100000 + rnd.nextInt(900000);
				dbAction.addEntry(Integer.toString(n),txtCardName.getText().toString(),0,0,txtNote.getText().toString(),sqlTimestamp,0);
				for(int j=0;j<i;j++)
				{
					dbAction.addEntryTable(Integer.toString(n),lblAdd[j].getText(),txtAdd[j].getText().toString(),type[j]);
					System.out.println(lblAdd[j].getText().toString()+" "+ txtAdd[j].getText().toString()+ " "+ type[j]);
				}
			}
			else{
				dbAction.updateMaster(tableNumber,txtCardName.getText().toString(),0,0,txtNote.getText().toString(),sqlTimestamp);
				dbAction.deleteTableEntry(tableNumber);
				for(int j=0;j<i;j++)
				{
					dbAction.addEntryTable(tableNumber,lblAdd[j].getText(),txtAdd[j].getText().toString(),type[j]);
					System.out.println(lblAdd[j].getText().toString()+" "+ txtAdd[j].getText().toString()+ " "+ type[j]);
				}

			}
			this.dispose();
			SafeMain.updateCardList();
		}
		if(ae.getSource()==miSaveTemplate){
			Random rnd = new Random();
			int n = 100000 + rnd.nextInt(900000);
			System.out.println("IN"+i);
			java.util.Date date = new java.util.Date();
			long t = date.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(t);
			dbAction.addEntry(Integer.toString(n),txtCardName.getText().toString(),1,0,txtNote.getText().toString(),sqlTimestamp,0);
			for(int j=0;j<i;j++)
			{
				dbAction.addEntryTable(Integer.toString(n),lblAdd[j].getText(),null,type[j]);
				System.out.println(lblAdd[j].getText().toString()+" "+ txtAdd[j].getText().toString()+ " "+ type[j]);
			}

		}
		if(ae.getSource()==miDiscard || ae.getSource()==miClose){
			this.dispose();

		}
		if(ae.getSource()==btnGeneratePassword){
			new PassGenerate();
		}
	}
}
