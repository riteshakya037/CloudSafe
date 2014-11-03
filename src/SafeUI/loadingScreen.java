package SafeUI;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class loadingScreen extends JDialog implements Runnable{
	JLabel lblStatus;
	private int progressVal=0;
	JProgressBar progressBar;
	public loadingScreen() {

		setSize(500,100);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		progressBar = new JProgressBar();
		progressBar.setMaximum(100);
		progressBar.setStringPainted(false);
		progressBar.setMinimum(10);
		getContentPane().add(progressBar, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		lblStatus= new JLabel("Initializing...");
		panel.add(lblStatus);
	}
	@Override
	public void run() {
		setVisible(true);
	}
	public void stop(){
		this.dispose();
	}
	public void setStatus(String txtStatus){
		lblStatus.setText(txtStatus);
	}
	public void setProgress(int val){
		progressVal+=val;
		progressBar.setValue(progressVal);
	}

}
