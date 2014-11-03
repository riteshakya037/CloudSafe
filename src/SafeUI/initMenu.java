package SafeUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import Algo.ClientSync;



@SuppressWarnings("serial")
public class initMenu extends JFrame implements ActionListener {
	private final JPanel contentPanel = new JPanel();
	JLabel lblTitle;
	JRadioButton rbCreate,rbLoadCloud;
	JButton btnNext;
	final ButtonGroup grp;

	public initMenu() {
		setTitle("CloudSafe");
		lblTitle=new JLabel("Database Setup");
		rbCreate=new JRadioButton("Create a new Database and set a Password for it");
		rbCreate.setSelected(true);
		rbLoadCloud=new JRadioButton("Restore a databse from Cloud");
		
		btnNext=new JButton("Next");
		grp=new ButtonGroup();
		grp.add(rbCreate);
		grp.add(rbLoadCloud);

		contentPanel.add(lblTitle);
		lblTitle.setBounds(25,25,150,30);

		contentPanel.add(rbCreate);
		rbCreate.setBounds(25,75,350,30);

		contentPanel.add(rbLoadCloud);
		rbLoadCloud.setBounds(25,105,350,30);

		contentPanel.add(btnNext);
		getRootPane().setDefaultButton(btnNext);
		btnNext.addActionListener(this);
		btnNext.setBounds(485,425,90,30);

		ClientSync cs=new ClientSync();
		String chk=cs.getString();
		if(chk.equals("")){
			System.out.println("No Connection");
			rbLoadCloud.setEnabled(false);
		}
		
		setSize(600	, 500);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		
		addWindowListener(new WindowAdapter() { 
			@Override
			public void windowClosing(WindowEvent windowEvent) 
			{
				close();
			}

		});

	}
	protected void close() {
		int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to close CloudSafe?", "Really Closing?",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) 
		{
			System.exit(0);
		}

	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==btnNext)
		{
			if(rbCreate.isSelected())
			{				
				new initPassword(this);
			}
			if(rbLoadCloud.isSelected())
			{				
				new CloudSelect();
				this.dispose();
			}

		}
	}

}

