package SafeUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import Algo.Hashing;
import Algo.dbAction;
import Algo.passwordStrength;
import javax.swing.border.EtchedBorder;
import java.awt.Toolkit;


@SuppressWarnings("serial")
public class newPassword extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField currentPass;
	private JPasswordField newPass1;
	private JPasswordField newPass2;
	private JButton cancelButton,okButton;
	JLabel errorMsg;
	JProgressBar pwdStrength;

	public newPassword(JFrame parent) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(newPassword.class.getResource("/Resource/change_password.png")));
		setTitle("Change Password");

		currentPass = new JPasswordField();
		currentPass.setBounds(156, 11, 264, 30);
		contentPanel.add(currentPass);

		newPass1 = new JPasswordField();
		newPass1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		newPass1.setBounds(156, 52, 264, 30);
		newPass1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				passwordStrength ps=new passwordStrength();
				int strength=ps.checkPasswordStrength(String.valueOf(newPass1.getPassword()));	
				System.out.println(strength);
				pwdStrength.setValue(strength);
				pwdStrength.setMaximum(100);
				pwdStrength.setStringPainted(true);
				pwdStrength.setMinimum(10);
				pwdStrength.setString("");
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
		contentPanel.add(newPass1);
		newPass1.setText("");

		newPass2 = new JPasswordField();
		newPass2.setText("");
		newPass2.setBounds(156, 107, 264, 30);
		contentPanel.add(newPass2);

		JLabel lblCurrentPassword = new JLabel("Current Password");
		lblCurrentPassword.setBounds(10, 11, 138, 30);
		contentPanel.add(lblCurrentPassword);

		JLabel lblEnterNewpassword = new JLabel("Enter New Password");
		lblEnterNewpassword.setBounds(10, 52, 138, 30);
		contentPanel.add(lblEnterNewpassword);


		JLabel lblEnterPasswordAgain = new JLabel("Enter Password Again");
		lblEnterPasswordAgain.setBounds(10, 107, 138, 30);
		contentPanel.add(lblEnterPasswordAgain);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 148, 435, 40);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);

			errorMsg = new JLabel("");
			errorMsg.setForeground(Color.RED);
			errorMsg.setBounds(27, 0, 281, 34);
			buttonPane.add(errorMsg);
			{
				okButton = new JButton("OK");
				okButton.setBounds(230, 5, 90, 35);
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setBounds(330, 5, 90, 35);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}

		}

		setModal(true);
		setSize(450, 221);
		setResizable(false);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		contentPanel.setLayout(null);
		
		pwdStrength = new JProgressBar();
		pwdStrength.setFocusable(false);
		pwdStrength.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pwdStrength.setBorderPainted(false);
		pwdStrength.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pwdStrength.setBounds(156, 87, 264, 14);
		contentPanel.add(pwdStrength);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==okButton)
		{
			
			Hashing h=new Hashing();
			dbAction db=new dbAction();
			String newPass1String=String.valueOf(newPass1.getPassword());
			String newPass2String=String.valueOf(newPass2.getPassword());
			String currentPassString=String.valueOf(currentPass.getPassword());
			
			String hashEntered=h.HmacSHA256(currentPassString);
			try {
				if(dbAction.getPass().equals(hashEntered))
				{
					if(newPass1String.equals(newPass2String))
					{
						passwordStrength ps=new passwordStrength();
						int strength=ps.checkPasswordStrength(newPass1String);
						System.out.println(strength);
						db.updatePass(newPass2String);
						dispose();
					}
					else 

					{
						errorMsg.setText("Passwords not match");
						newPass1.setText("");
						newPass2.setText("");
					}
				}
				else 
				{
					errorMsg.setText("Entered Current Password is Wrong");
					currentPass.setText("");
					newPass1.setText("");
					newPass2.setText("");
				}
			} catch (Exception e) {
			}
		}
		if(ae.getSource()==cancelButton)
		{
			dispose();
		}

	}
}
