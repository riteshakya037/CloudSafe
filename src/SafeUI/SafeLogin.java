package SafeUI;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;

import Algo.Hashing;
import Algo.dbAction;

@SuppressWarnings("serial")
public class SafeLogin extends JFrame implements ActionListener{
	JPasswordField txtPass;
	JLabel lblPass,lblInfo,lblError;
	JButton btnOK,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnClr,btnOSB;
	static int count=5;
	protected SafeLogin()
	{
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(SafeLogin.class.getResource("/Resource/Icon.png")));

		getContentPane().setLayout(null);
		txtPass=new JPasswordField(50);
		txtPass.setOpaque(false);
		txtPass.setBackground(SystemColor.control);
		txtPass.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.LIGHT_GRAY));
//		txtPass.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		lblPass=new JLabel("Enter Password:");
		lblInfo=new JLabel("256-bit Advanced Encryption Standard");
		btnOK=new JButton("OK");
		btn1=new JButton("1");
		btn2=new JButton("2");
		btn3=new JButton("3");
		btn4=new JButton("4");
		btn5=new JButton("5");
		btn6=new JButton("6");
		btn7=new JButton("7");
		btn8=new JButton("8");
		btn9=new JButton("9");
		btn0=new JButton("0");
		btnOSB=new JButton("ABC");
		btnClr=new JButton("CLR");
		getContentPane().add(lblPass);
		lblPass.setBounds(70, 55, 130, 25);
		getContentPane().add(txtPass);
		txtPass.setBounds(70,90,170,30);


		getContentPane().add(btnOK);
		btnOK.setBounds(250, 90, 50, 30);
		btnOK.addActionListener(this);
		getRootPane().setDefaultButton(btnOK);
		getContentPane().add(btn1);
		btn1.setBounds(70, 160, 70, 30);
		btn1.addActionListener(this);
		getContentPane().add(btn2);
		btn2.setBounds(150, 160, 70, 30);
		btn2.addActionListener(this);
		getContentPane().add(btn3);
		btn3.setBounds(230, 160, 70, 30);
		btn3.addActionListener(this);
		getContentPane().add(btn4);
		btn4.setBounds(70, 200, 70, 30);
		btn4.addActionListener(this);
		getContentPane().add(btn5);
		btn5.setBounds(150, 200, 70, 30);
		btn5.addActionListener(this);
		getContentPane().add(btn6);
		btn6.setBounds(230, 200, 70, 30);
		btn6.addActionListener(this);
		getContentPane().add(btn7);
		btn7.setBounds(70, 240, 70, 30);
		btn7.addActionListener(this);
		getContentPane().add(btn8);
		btn8.setBounds(150, 240, 70, 30);
		btn8.addActionListener(this);
		getContentPane().add(btn9);
		btn9.setBounds(230, 240, 70, 30);
		btn9.addActionListener(this);
		getContentPane().add(btn0);
		btn0.setBounds(150, 280, 70, 30);
		btn0.addActionListener(this);
		getContentPane().add(btnClr);
		btnClr.setBounds(230, 280, 70, 30);
		btnClr.addActionListener(this);
		getContentPane().add(btnOSB);
		btnOSB.setBounds(70, 280, 70, 30);
		btnOSB.addActionListener(this);
		getContentPane().add(lblInfo);
		lblInfo.setBounds(70, 400, 230, 30);
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblError = new JLabel("");
		lblError.setBounds(70, 125, 230, 25);
		getContentPane().add(lblError);

		setSize(375, 475);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("CloudSafe");
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		addWindowListener(new WindowAdapter() { 
			@Override
			public void windowClosing(WindowEvent windowEvent) 
			{
				int result = JOptionPane.showConfirmDialog(null,"Are you sure to close CloudSafe?", "Really Closing?",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) 
				{

					System.exit(0);
				}
			}
		});
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==btnOK){
			try 
			{
				if(count==0){
					dbAction db=new dbAction();
					db.delete();
					System.exit(0);
				}
				else
					chkLogin();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		if(ae.getSource()==btnOSB)
		{
			try {
				Runtime.getRuntime().exec("cmd /c C:\\Windows\\System32\\osk.exe");
				txtPass.transferFocus();
			} catch (IOException ex) {
				Logger.getLogger(SafeLogin.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if(ae.getSource()==btn1){
			txtPass.setText(getPass()+1);
		}
		if(ae.getSource()==btn2){
			txtPass.setText(getPass()+2);
		}
		if(ae.getSource()==btn3){
			txtPass.setText(getPass()+3);
		}
		if(ae.getSource()==btn4){
			txtPass.setText(getPass()+4);
		}
		if(ae.getSource()==btn5){
			txtPass.setText(getPass()+5);
		}
		if(ae.getSource()==btn6){
			txtPass.setText(getPass()+6);
		}
		if(ae.getSource()==btn7){
			txtPass.setText(getPass()+7);
		}
		if(ae.getSource()==btn8){
			txtPass.setText(getPass()+8);
		}
		if(ae.getSource()==btn9){
			txtPass.setText(getPass()+9);
		}
		if(ae.getSource()==btn0){
			txtPass.setText(getPass()+0);
		}
		if(ae.getSource()==btnClr){
			txtPass.setText("");
		}      
	}

	protected void chkLogin() throws Exception 
	{
		Hashing h=new Hashing();
		String hashEntered=h.HmacSHA256(getPass());
		System.out.println("HmacSha of Entered Password :"+hashEntered);
		//h.SHA256(getPass());
		if(dbAction.getPass().equals(hashEntered))
		{
			new SafeMain();
			this.dispose();
		}
		else
		{
			lblError.setText("Error in Password (Trires left "+count+(")"));
			txtPass.setText("");
			count--;
		}
	}
	public static void main(String[] args) throws Exception {

		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex) {
		    ex.printStackTrace();
		}
		 
		dbAction db=new dbAction();
		if(db.load()==true)
		{
			new SafeLogin();
		}
		else
		{
			new initMenu();
		}


	}
	protected String getPass()
	{
		char[]	pass=txtPass.getPassword();
		return String.valueOf(pass);

	}
}
