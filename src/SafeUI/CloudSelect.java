package SafeUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

@SuppressWarnings("serial")
public class CloudSelect extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	JRadioButton rdbtnTemporaryCloud;
	JButton btnNext,btnBack;
	public CloudSelect() {
		setTitle("CloudSafe");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel titlePanel = new JPanel();
			contentPanel.add(titlePanel, BorderLayout.NORTH);
			titlePanel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblTitle = new JLabel("<Html>Restore Database<br/>Select A cloud to sync your encrypted database.</html>");
				titlePanel.add(lblTitle, BorderLayout.NORTH);
			}
		}
		{
			JPanel cloudSelect = new JPanel();
			contentPanel.add(cloudSelect, BorderLayout.WEST);
			cloudSelect.setLayout(new GridLayout(4, 2, 0, 0));
			{
				JRadioButton rdbtnGoogleDrive = new JRadioButton("");
				rdbtnGoogleDrive.setHorizontalAlignment(SwingConstants.CENTER);
				rdbtnGoogleDrive.setIconTextGap(40);
				rdbtnGoogleDrive.setPreferredSize(new Dimension(40, 100));
				rdbtnGoogleDrive.setEnabled(false);
				cloudSelect.add(rdbtnGoogleDrive);
			}
			{
				JRadioButton rdbtnDropBox = new JRadioButton("");
				rdbtnDropBox.setHorizontalAlignment(SwingConstants.CENTER);
				rdbtnDropBox.setIconTextGap(40);
				rdbtnDropBox.setPreferredSize(new Dimension(40, 100));
				rdbtnDropBox.setEnabled(false);
				cloudSelect.add(rdbtnDropBox);
			}
			{
				JRadioButton rdbtnOneDrive = new JRadioButton("");
				rdbtnOneDrive.setHorizontalAlignment(SwingConstants.CENTER);
				rdbtnOneDrive.setIconTextGap(40);
				rdbtnOneDrive.setPreferredSize(new Dimension(40, 100));
				rdbtnOneDrive.setEnabled(false);
				cloudSelect.add(rdbtnOneDrive);
			}
			{
				rdbtnTemporaryCloud = new JRadioButton("");
				rdbtnTemporaryCloud.setHorizontalAlignment(SwingConstants.CENTER);
				rdbtnTemporaryCloud.setIconTextGap(40);
				rdbtnTemporaryCloud.setSelected(true);
				rdbtnTemporaryCloud.setPreferredSize(new Dimension(40, 100));
				cloudSelect.add(rdbtnTemporaryCloud);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setPreferredSize(new Dimension(30, 10));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new GridLayout(4, 0, 0, 0));
			{
				JLabel lblGoogleDrive = new JLabel("Google Drive");
				lblGoogleDrive.setEnabled(false);
				lblGoogleDrive.setIconTextGap(30);
				lblGoogleDrive.setIcon(new ImageIcon(CloudSelect.class.getResource("/Resource/Google-Drive-icon.png")));
				panel.add(lblGoogleDrive);
			}
			{
				JLabel lblDropbox = new JLabel("DropBox");
				lblDropbox.setEnabled(false);
				lblDropbox.setIcon(new ImageIcon(CloudSelect.class.getResource("/Resource/App-dropbox-icon.png")));
				lblDropbox.setIconTextGap(30);
				panel.add(lblDropbox);
			}
			{
				JLabel lblOnedrive = new JLabel("OneDrive");
				lblOnedrive.setEnabled(false);
				lblOnedrive.setIcon(new ImageIcon(CloudSelect.class.getResource("/Resource/App-Skydrive-icon.png")));
				lblOnedrive.setIconTextGap(30);
				panel.add(lblOnedrive);
			}
			{
				JLabel lblCloudsafe = new JLabel("CloudSafe");
				lblCloudsafe.setIconTextGap(30);
				lblCloudsafe.setIcon(new ImageIcon(CloudSelect.class.getResource("/Resource/Cloudsafe.png")));
				panel.add(lblCloudsafe);
			}
		}


		{
			JPanel buttonPane = new JPanel();
			buttonPane.setPreferredSize(new Dimension(10, 50));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(null);
			{
				btnBack = new JButton("Back");
				btnBack.setBounds(84, 11, 90, 30);
				buttonPane.add(btnBack);
				btnBack.addActionListener(this);
			}
			{
				btnNext = new JButton("Next");
				btnNext.setBounds(184, 11, 90, 30);
				buttonPane.add(btnNext);
				btnNext.addActionListener(this);
				getRootPane().setDefaultButton(btnNext);
			}
		}
		setSize(300	, 500);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==btnNext)	{
			
			if(rdbtnTemporaryCloud.isSelected())
			{	
				this.dispose();
				new CloudPass("false");
				
			}

		}
		if(ae.getSource()==btnBack)	{
			this.dispose();
			new initMenu();
		}
	}
}
