package Algo;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class PassGenerate extends JDialog implements ChangeListener, ItemListener {

	private final JPanel contentPanel = new JPanel();
	static JSlider slider;
	static JCheckBox checkBoxA,checkBoxa,checkBox,checkBoxNum;
	static Boolean ckNum,ckA,cka,ck;
	JLabel lblPass;
	static StringBuilder password;
	String jtextfield;

	public PassGenerate() {
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 359, 159);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			lblPass = new JLabel();
			lblPass.setHorizontalAlignment(SwingConstants.CENTER);
			lblPass.setBounds(5, 5, 349, 24);
			contentPanel.add(lblPass);
		}
		{
			slider= new JSlider();
			slider.addChangeListener(this);

			slider.setBounds(15, 30, 325, 31);
			slider.setMinorTickSpacing(1);
			slider.setValue(8);
			slider.setMinimum(6);
			slider.setMaximum(24);
			slider.setSnapToTicks(true);
			slider.setPaintTicks(true);
			contentPanel.add(slider);
		}
		{
			checkBoxA = new JCheckBox("A-Z");
			checkBoxA.setEnabled(false);
			checkBoxA.setSelected(true);
			checkBoxA.setBounds(15, 88, 97, 23);
			checkBoxA.addItemListener(this);
			contentPanel.add(checkBoxA);
		}
		{
			checkBoxa = new JCheckBox("a-z");
			checkBoxa.setEnabled(false);
			checkBoxa.setSelected(true);
			checkBoxa.setBounds(15, 129, 97, 23);
			checkBoxa.addItemListener(this);
			contentPanel.add(checkBoxa);
		}
		{
			checkBox = new JCheckBox("@#$%&*-+!?");
			checkBox.setEnabled(false);
			checkBox.setSelected(true);
			checkBox.setBounds(187, 88, 97, 23);
			checkBox.addItemListener(this);
			contentPanel.add(checkBox);
		}
		{
			checkBoxNum = new JCheckBox("0-9");
			checkBoxNum.setEnabled(false);
			checkBoxNum.setSelected(true);
			checkBoxNum.setBounds(187, 129, 97, 23);
			checkBoxNum.addItemListener(this);
			contentPanel.add(checkBoxNum);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 158, 359, 54);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton btnSet = new JButton("Set");
				btnSet.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
						StringSelection testData;

						//  Add some test data

						testData = new StringSelection(password.toString());
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
						dispose();
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
				btnSet.setBounds(59, 11, 80, 25);
				buttonPane.add(btnSet);
				rootPane.setDefaultButton(btnSet);
			}
			{
				JButton btnRefresh = new JButton("Refresh");
				btnRefresh.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						lblPass.setText(getRandomPassword());
					}
				});
				btnRefresh.setBounds(149, 11, 80, 25);
				buttonPane.add(btnRefresh);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setBounds(242, 11, 80, 25);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		lblPass.setText(getRandomPassword());

		setSize(375,250);
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);

		ckNum=checkBoxNum.isSelected();
		ckA=checkBoxA.isSelected();
		cka=checkBoxa.isSelected();
		ck=checkBox.isSelected();
	}


	private static final String NUMBERS = "0123456789";
	private static final String UPPER_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER_ALPHABETS = "abcdefghijklmnopqrstuvwxyz";
	private static final String SPECIALCHARACTERS = "@#$%&*-+!?";

	public static String getRandomPassword() {
		password = new StringBuilder();
		for (int i = 0; i < slider.getValue(); i++) {
			Random rnd = new Random();
			int j = 0 + rnd.nextInt(4);

			password.append(getRandomPasswordCharacters(j));

		}
		return password.toString();
	}

	private static String getRandomPasswordCharacters(int pos) {
		Random randomNum = new Random();
		StringBuilder randomChar = new StringBuilder();
		switch (pos) {
		case 0:
			randomChar.append(NUMBERS.charAt(randomNum.nextInt(NUMBERS.length() - 1)));
			break;
		case 1:
			randomChar.append(UPPER_ALPHABETS.charAt(randomNum.nextInt(UPPER_ALPHABETS.length() - 1)));
			break;
		case 2:
			randomChar.append(SPECIALCHARACTERS.charAt(randomNum.nextInt(SPECIALCHARACTERS.length() - 1)));
			break;
		case 3:
			randomChar.append(LOWER_ALPHABETS.charAt(randomNum.nextInt(LOWER_ALPHABETS.length() - 1)));
			break;
		}
		return randomChar.toString();
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		if(ce.getSource()==slider)
		{
			lblPass.setText(getRandomPassword());

		}
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		if(ie.getSource()==checkBoxA)
		{
			ckA=!ckA;
			System.out.println(ckA);
		}
		if(ie.getSource()==checkBoxa)
		{
			cka=!cka;
			System.out.println(cka);
		}
		if(ie.getSource()==checkBox)
		{
			ck=!ck;
			System.out.println(ck);
		}
		if(ie.getSource()==checkBoxNum)
		{
			ckNum=!ckNum;
			System.out.println(ckNum);
		}
	}
}

