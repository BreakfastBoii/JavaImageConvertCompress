import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame();
		frame.setSize(512, 512);
		frame.add(new ImageConvertPanel());
		frame.setVisible(true);
	}

}
