package SafeUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings({ "rawtypes", "serial", "unchecked" })

public class TemplateSelect extends JDialog {
	static String dbLocation=System.getProperty("user.home") + File.separatorChar + "My Documents\\CloudSafe\\Cloudsafe.db";
	private final JPanel contentPanel = new JPanel();
	JList lstCards;
	String index[]=new String[10];
	public TemplateSelect(final JFrame Parent) {
		setTitle("Select Template");
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load(Parent);				
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		DefaultListModel listModel = new DefaultListModel();

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from master_table where template and not trash;");
			int i=0;
			while ( rs.next() ) {
				listModel.addElement(rs.getString("Name"));
				index[i]=rs.getString("ID");
				System.out.println();
				i++;
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			
		}

		lstCards= new JList(listModel);
		lstCards.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					load(Parent);
				}
			}
		});
		lstCards.setSelectedIndex(0);
		lstCards.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lstCards.setSelectionForeground(new Color(0, 0, 0));
		lstCards.setFixedCellHeight(50);
		lstCards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstCards.setSelectionBackground(new Color(209, 229, 252));
		lstCards.setForeground(Color.BLACK);
		scrollPane = new JScrollPane(lstCards);
		contentPanel.add(scrollPane);
		setSize(300,450);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(Parent);
		setVisible(true);


	}
	protected void load(JFrame Parent) {
		dispose();
		new newCard(index[lstCards.getSelectedIndex()]);		
	}

}
