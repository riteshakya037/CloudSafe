package SafeUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Algo.ClientSync;
import Algo.Prefs;
import javax.swing.JTabbedPane;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class optionUI extends JDialog {

	private final JPanel contentPanel = new JPanel();

	JSlider sliderLock,sliderClear;
	JLabel lblLock,lblClear, lblCloudsafe;
	int valLock,valClear;
	JRadioButton rdbtnCloudSafe;
	JPanel panelCloud;
	public optionUI() {


		Prefs ps=new Prefs();
		try {
			valLock=Integer.parseInt(ps.getProp("lockDelay"));
			valClear=Integer.parseInt(ps.getProp("clearDelay"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane);
			{
				panelCloud = new JPanel();
				tabbedPane.addTab("Cloud Select", null, panelCloud, null);
				panelCloud.setLayout(new BorderLayout(0, 0));
				{
					JPanel panelRadio = new JPanel();
					panelRadio.setPreferredSize(new Dimension(30, 10));
					panelCloud.add(panelRadio, BorderLayout.WEST);
					panelRadio.setLayout(new GridLayout(4, 0, 0, 0));
					{
						JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("");
						rdbtnNewRadioButton_1.setEnabled(false);
						rdbtnNewRadioButton_1.setHorizontalAlignment(SwingConstants.CENTER);
						panelRadio.add(rdbtnNewRadioButton_1);
					}
					{
						JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("");
						rdbtnNewRadioButton_2.setEnabled(false);
						rdbtnNewRadioButton_2.setHorizontalAlignment(SwingConstants.CENTER);
						panelRadio.add(rdbtnNewRadioButton_2);
					}
					{
						JRadioButton rdbtnNewRadioButton = new JRadioButton("");
						rdbtnNewRadioButton.setEnabled(false);
						rdbtnNewRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
						panelRadio.add(rdbtnNewRadioButton);
					}
					{
						rdbtnCloudSafe = new JRadioButton("");
						rdbtnCloudSafe.setSelected(true);
						rdbtnCloudSafe.setHorizontalAlignment(SwingConstants.CENTER);
						panelRadio.add(rdbtnCloudSafe);
					}
				}
				{
					JPanel paneLabels = new JPanel();
					panelCloud.add(paneLabels, BorderLayout.CENTER);
					paneLabels.setLayout(new GridLayout(4, 0, 0, 0));
					{
						JLabel lblNewLabel = new JLabel("GoogleDrive");
						lblNewLabel.setIconTextGap(30);
						lblNewLabel.setIcon(new ImageIcon(optionUI.class.getResource("/Resource/Google-Drive-icon.png")));
						lblNewLabel.setEnabled(false);
						paneLabels.add(lblNewLabel);
					}
					{
						JLabel lblNewLabel_1 = new JLabel("DropBox");
						lblNewLabel_1.setIconTextGap(30);
						lblNewLabel_1.setIcon(new ImageIcon(optionUI.class.getResource("/Resource/App-dropbox-icon.png")));
						lblNewLabel_1.setEnabled(false);
						paneLabels.add(lblNewLabel_1);
					}
					{
						JLabel lblNewLabel_2 = new JLabel("OneDrive");
						lblNewLabel_2.setIconTextGap(30);
						lblNewLabel_2.setIcon(new ImageIcon(optionUI.class.getResource("/Resource/App-Skydrive-icon.png")));
						lblNewLabel_2.setEnabled(false);
						paneLabels.add(lblNewLabel_2);
					}
					{
						lblCloudsafe = new JLabel("CloudSafe");
						lblCloudsafe.setIconTextGap(30);
						lblCloudsafe.setIcon(new ImageIcon(optionUI.class.getResource("/Resource/Cloudsafe.png")));
						paneLabels.add(lblCloudsafe);
					}
				}
			}
			{
				JPanel panelSecurity = new JPanel();
				tabbedPane.addTab("Security", null, panelSecurity, null);
				panelSecurity.setLayout(new GridLayout(2, 0, 0, 0));
				{
					JPanel panelLock = new JPanel();
					panelSecurity.add(panelLock);
					panelLock.setLayout(new GridLayout(0, 1, 0, 0));
					{
						JLabel lblAutoLockIn = new JLabel("Auto Lock in Background after: ");
						lblAutoLockIn.setHorizontalAlignment(SwingConstants.CENTER);
						lblAutoLockIn.setHorizontalTextPosition(SwingConstants.CENTER);
						panelLock.add(lblAutoLockIn);
					}
					{
						lblLock = new JLabel("");
						lblLock.setHorizontalAlignment(SwingConstants.CENTER);
						lblLock.setHorizontalTextPosition(SwingConstants.CENTER);
						panelLock.add(lblLock);
					}
					{
						sliderLock = new JSlider();
						sliderLock.addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent e) {
								lblLock.setText(sliderLock.getValue()+" secs.");
							}
						});
						sliderLock.setMinorTickSpacing(1);
						sliderLock.setPaintTicks(true);
						sliderLock.setSnapToTicks(true);
						sliderLock.setMajorTickSpacing(4);
						sliderLock.setMaximum(20);
						sliderLock.setValue(valLock);
						panelLock.add(sliderLock);
					}
				}
				{
					JPanel panelClear = new JPanel();
					panelSecurity.add(panelClear);
					panelClear.setLayout(new GridLayout(3, 0, 0, 0));
					{
						JLabel lblClearClipboardContent = new JLabel("Clear ClipBoard Content after: ");
						lblClearClipboardContent.setHorizontalAlignment(SwingConstants.CENTER);
						lblClearClipboardContent.setHorizontalTextPosition(SwingConstants.CENTER);
						panelClear.add(lblClearClipboardContent);
					}
					{
						lblClear = new JLabel("");
						lblClear.setHorizontalAlignment(SwingConstants.CENTER);
						lblClear.setHorizontalTextPosition(SwingConstants.CENTER);
						panelClear.add(lblClear);
					}
					{
						sliderClear = new JSlider();
						sliderClear.addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent arg0) {
								lblClear.setText(sliderClear.getValue()+" secs.");
							}
						});
						sliderClear.setMinorTickSpacing(1);
						sliderClear.setPaintTicks(true);
						sliderClear.setSnapToTicks(true);
						sliderClear.setMajorTickSpacing(4);
						sliderClear.setMaximum(20);
						sliderClear.setValue(valClear);
						panelClear.add(sliderClear);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setPreferredSize(new Dimension(90, 30));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Prefs ps=new Prefs();
						ClientSync cs=new ClientSync();
						String chk=cs.getString();
						try {
							ps.setProp("lockDelay",Integer.toString(sliderLock.getValue()));
							ps.setProp("clearDelay", Integer.toString(sliderClear.getValue()));
							if(rdbtnCloudSafe.isSelected() && !chk.equals(""))
								new CloudPass("true");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setPreferredSize(new Dimension(90, 30));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		ClientSync cs=new ClientSync();
		String chk=cs.getString();
		if(chk.equals("")){
			System.out.println("No Connection");
			rdbtnCloudSafe.setEnabled(false);
			lblCloudsafe.setEnabled(false);
		}

		setModal(true);
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
