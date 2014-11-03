package SafeUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Algo.ClientSync;
import Algo.Prefs;


@SuppressWarnings("serial")
public class CloudPass extends JDialog{

	private final JPanel contentPanel = new JPanel();
	private JPasswordField txtPass;
	private JButton cancelButton,okButton;
	JLabel errorMsg;
	private JTextField txtUser;
	public CloudPass(final String check)

	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(CloudPass.class.getResource("/Resource/key.png")));
		setTitle("Setup Password");

		txtPass = new JPasswordField();
		txtPass.setBounds(119, 57, 301, 30);
		contentPanel.add(txtPass);
		txtPass.setText("");

		JLabel lblUserName = new JLabel("UserName");
		lblUserName.setBounds(10, 11, 144, 30);
		contentPanel.add(lblUserName);


		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 57, 144, 30);
		contentPanel.add(lblPassword);
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
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent arg0) {
						ClientSync cs=new ClientSync(txtUser.getText(),txtPass.getText(),check);
						if(cs.getString().equals("GRANTED")){
							dispose();
							if(check.equals("false"))
								new SafeMain();
							Prefs ps=new Prefs();
							try {
								ps.setProp("User", txtUser.getText());
								ps.setProp("Password", txtPass.getText());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						else if(cs.getString().equals("NO DATABASE"))
						{
							JOptionPane.showMessageDialog(null,"No Database in Account");
							dispose();
							new CloudSelect();
							
						}
						else
						{
							errorMsg.setText("Invalid Account");
							txtUser.setText("");
							txtPass.setText("");
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
		setLocationRelativeTo(this);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPanel.setLayout(null);

		txtUser = new JTextField();
		txtUser.setBounds(119, 11, 301, 30);
		contentPanel.add(txtUser);
		txtUser.setColumns(10);
		setVisible(true);

	}
}
