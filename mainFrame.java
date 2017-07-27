import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class mainFrame {
	public mainFrame() {
		log(Level.INFO, "mainFrame : Initiating frame");
		JFrame frame = new JFrame("Test");
		JLabel label = new JLabel("OFF");
		label.setIcon(new ImageIcon(getClass().getResource("/images/on.png")));
		frame.add(label, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	private void log(Level level, String message) {
		if(start.loggingEnabled) {
			start.logger.log(level, message);
		}
	}
}
