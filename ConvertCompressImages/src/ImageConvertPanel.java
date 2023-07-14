import java.io.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;

public class ImageConvertPanel extends JPanel implements ActionListener 
{
	JLabel title = new JLabel("Convert image type and compress");
	JLabel findLabel = new JLabel("Find image(s): ");
	JTextField fileInput = new JTextField(30);
	JButton findFileButton = new JButton("Find file...");
	JFileChooser fileChooser = new JFileChooser();
	
	String desktopDir = "";
	File chosenRecordFile = null;
	
	public ImageConvertPanel() 
	{
		setLayout(new FlowLayout());
		
		desktopDir = System.getProperty("user.home") + File.separator + "Desktop";
		fileChooser.setCurrentDirectory(new File(desktopDir));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FindTypeFilter("wav", "WAV Audio Type"));
		fileChooser.setAcceptAllFileFilterUsed(true);
		
		add(title);
		add(findLabel);
		add(fileInput);
		add(findFileButton);
		add(fileChooser);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
	}
}
