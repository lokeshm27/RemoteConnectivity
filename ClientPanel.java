import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClientPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	JLabel label;
	ImageIcon icon;
	
	public ClientPanel(Device device) {
		setSize(500, 200);
		label = new JLabel(device.DeviceID);
		switch(device.deviceType) {
			case Device.PHONE : icon = getSizedIcon("/images/phone.png");
			break;
			
			case Device.TV : icon = getSizedIcon("/images/tv.png");
			break;
			
			case Device.WEAR : icon = getSizedIcon("/images/wear.png");
			break;
			
			case Device.LAPTOP : icon = getSizedIcon("/images/laptop.png");
			break;
		}
		label.setIcon(icon);
		add(label);
	}
	
	public ImageIcon getSizedIcon(String path) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
		Image img = imageIcon.getImage();
		Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg);
		return imageIcon;
	}
}
