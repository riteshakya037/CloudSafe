package SafeUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;

import Algo.dbAction;
import Algo.passwordStrength;
import java.awt.Toolkit;


@SuppressWarnings("serial")
public class initPassword extends JDialog{

	private final JPanel contentPanel = new JPanel();
	private JPasswordField newPass1;
	private JPasswordField newPass2;
	private JButton cancelButton,okButton;
	JLabel errorMsg;
	private JProgressBar pwdStrength;
	public initPassword(final JFrame parent)

	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(initPassword.class.getResource("/Resource/key.png")));
		setTitle("Setup Password");

		newPass1 = new JPasswordField();
		newPass1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				passwordStrength ps=new passwordStrength();
				int strength=ps.checkPasswordStrength(String.valueOf(newPass1.getPassword()));	
				pwdStrength.setValue(strength);
				pwdStrength.setMaximum(100);
				pwdStrength.setStringPainted(false);
				pwdStrength.setMinimum(10);
				if(strength<=20)
				{

					pwdStrength.setForeground(Color.RED);
				}
				else if(strength<=60)
				{

					pwdStrength.setForeground(Color.YELLOW);
					
				}
				else if(strength>60)
				{

					pwdStrength.setForeground(Color.GREEN);
				}
				
				
			}
		});
		newPass1.setBounds(164, 11, 256, 30);
		contentPanel.add(newPass1);
		newPass1.setText("");

		newPass2 = new JPasswordField();
		newPass2.setText("");
		newPass2.setBounds(164, 65, 256, 30);
		contentPanel.add(newPass2);

		JLabel lblEnterNewpassword = new JLabel("Enter New Password");
		lblEnterNewpassword.setBounds(10, 11, 144, 30);
		contentPanel.add(lblEnterNewpassword);


		JLabel lblEnterPasswordAgain = new JLabel("Enter Password Again");
		lblEnterPasswordAgain.setBounds(10, 65, 144, 30);
		contentPanel.add(lblEnterPasswordAgain);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 102, 435, 40);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);

			errorMsg = new JLabel("");
			errorMsg.setForeground(Color.RED);
			errorMsg.setBounds(27, 0, 281, 40);
			buttonPane.add(errorMsg);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String newPass1String=String.valueOf(newPass1.getPassword());
						String newPass2String=String.valueOf(newPass2.getPassword());
						if(newPass1String.equals(newPass2String))
						{
							dispose();
							parent.dispose();
							dbAction db=new dbAction(newPass1String);
							Thread th1=new Thread(db);
							th1.start();
							
						}
						else 

						{
							errorMsg.setText("Passwords not match");
							newPass1.setText("");
							newPass2.setText("");
						}
							
					}
				});
				okButton.setBounds(220, 5, 90, 30);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setBounds(330, 5, 90, 30);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}

		}

		setModal(true);
		setSize(450, 175);
		setResizable(false);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPanel.setLayout(null);
		
		pwdStrength = new JProgressBar();
		pwdStrength.setBounds(164, 45, 256, 14);
		contentPanel.add(pwdStrength);
		setVisible(true);

	}


}
