import java.io.*;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
//import javax.imageio.ImageWriter;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class ImageConvertPanel extends JPanel implements ActionListener 
{
	JLabel title = new JLabel("Convert image type and compress");
	JLabel findLabel = new JLabel("Find image(s): ");
	JTextField fileInput = new JTextField(30);
	JButton findFileButton = new JButton("Find file...");
	//JLabel fileExtentionLabel = new JLabel("Convert to: ");
	//JTextField fileExtentionInput = new JTextField(".PNG");
	JLabel compressionLabel = new JLabel("Quality %: ");
	JTextField compressionInput = new JTextField("100");
	JCheckBox transparentCheckbox = new JCheckBox("Transparent background");
	JButton bgColorButton = new JButton("Background color (JPEG only)");
	JButton convertButton = new JButton("Export image...");
	
	JFileChooser fileChooser = new JFileChooser();
	
	Color bgColor = Color.black;
	
	String desktopDir = "";
	File currentFile = null;
	File exportFile = null;
	BufferedImage currentImg = null;
	
	public ImageConvertPanel() 
	{
		setLayout(new FlowLayout());
		
		desktopDir = System.getProperty("user.home") + File.separator + "Desktop";
		fileChooser.setCurrentDirectory(new File(desktopDir));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FindTypeFilter("png", "PNG Image Type"));
		fileChooser.addChoosableFileFilter(new FindTypeFilter("jpg", "JPG Image Type"));
		fileChooser.addChoosableFileFilter(new FindTypeFilter("jpeg", "JPEG Image Type"));
		fileChooser.addChoosableFileFilter(new FindTypeFilter("bmp", "Bitmap Image"));
		fileChooser.addChoosableFileFilter(new FindTypeFilter("gif", "GIF Animated Image"));
		fileChooser.addChoosableFileFilter(new FindTypeFilter("webmp", "Web Image Type"));
		fileChooser.setAcceptAllFileFilterUsed(true);
		
		findFileButton.addActionListener(this);
		bgColorButton.addActionListener(this);
		convertButton.addActionListener(this);
		
		add(title);
		add(findLabel);
		add(fileInput);
		add(findFileButton);
		//add(fileChooser); //adding it makes it apart of the UI instead of a seperate popup
		//add(fileExtentionLabel);
		//add(fileExtentionInput);

		transparentCheckbox.setFocusable(true); //default value
		add(transparentCheckbox);
		add(bgColorButton);
		
		add(compressionLabel);
		add(compressionInput);
		add(convertButton);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			Object source = e.getSource();
		
			if(source == findFileButton)
			{
				if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
				{
	                fileInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
	                
	                currentFile = new File(fileInput.getText()).getAbsoluteFile();
					FileInputStream inputStream = new FileInputStream(currentFile);
					currentImg = ImageIO.read(inputStream);
					
					inputStream.close();   
				}
			}
			else if(source == bgColorButton)
			{
				//JColorChooser colorPicker = new JColorChooser();
				bgColor = JColorChooser.showDialog(null, "Pick a background color", bgColor); //bgColor is deafult (aka Black)
			}
			else if(source == convertButton)
			{
				//ask to find file
				fileChooser.setSelectedFile(currentFile);
				if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				{
					exportFile = fileChooser.getSelectedFile();
					JOptionPane.showMessageDialog(null, "Saved image to " + exportFile.getName() + ".");
				}
				
				String fileExtension = GetFileExtension(exportFile.getName()).get().toLowerCase();
				
				//export file
				FileOutputStream outputStream = new FileOutputStream(exportFile);
				
				//get rid of transparency
				int imgType = BufferedImage.TYPE_INT_RGB;
				
				//has a transparent background and isnt a JPG
				if(transparentCheckbox.isSelected() && !(fileExtension == "jpg" || fileExtension == "jpeg"))  //!(fileExtension == "jpg" || fileExtension == "jpeg")
				{
					imgType = BufferedImage.TYPE_INT_ARGB;
					bgColor = new Color(0, 0, 0, 0); //transparent

					ImageIO.write(currentImg, fileExtension, outputStream); //different export method for transparency

					outputStream.close();
					return;
				}
				
				BufferedImage convertedImg = new BufferedImage(currentImg.getWidth(), currentImg.getHeight(), BufferedImage.TYPE_INT_RGB);
				convertedImg.createGraphics().drawImage(currentImg, 0, 0, bgColor, null);
				
				//output
				Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(fileExtension);
				ImageWriter writer = (ImageWriter) writers.next();
				
				ImageOutputStream imageOutput = ImageIO.createImageOutputStream(outputStream);
				writer.setOutput(imageOutput);
				
				
				//change image parameters
				ImageWriteParam param = writer.getDefaultWriteParam();
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				float quality = Float.parseFloat(compressionInput.getText());
				param.setCompressionQuality(quality / 100); //convert from percent to decimal
				
				
				//write to the file
				writer.write(null, new IIOImage(convertedImg, null, null), param);
				
				imageOutput.close();
				outputStream.close();
			}
		}
		catch(IOException error)
		{
			error.printStackTrace();
		}
	}
	
	Optional<String> GetFileExtension(String _fileName)
	{
		return Optional.ofNullable(_fileName).filter(f -> f.contains(".")).map(f -> f.substring(_fileName.lastIndexOf('.') + 1));
	}
}
