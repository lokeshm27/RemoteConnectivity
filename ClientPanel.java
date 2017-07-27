import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class ClientPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	JLabel label;
	ImageIcon icon;
	
	public ClientPanel(Device device) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(376, 30));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		label = new JLabel("   " + device.deviceID);
		switch(device.deviceType) {
			case Device.PHONE : icon = getSizedIcon("/images/phone.png", 20, 20);
			break;
			
			case Device.TV : icon = getSizedIcon("/images/tv.png", 20, 20);
			break;
			
			case Device.WEAR : icon = getSizedIcon("/images/wear.png", 20, 20);
			break;
			
			case Device.LAPTOP : icon = getSizedIcon("/images/laptop.png", 20, 20);
			break;
		}
		label.setIcon(icon);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Clicked On " + device.deviceID);
			}
		});
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(label, BorderLayout.WEST);
	}
	
	public ClientPanel() {
		//Do Nothing
	}

	public ImageIcon getSizedIcon(String path, int width, int height) {
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(path));
		Image img = imageIcon.getImage();
		Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg);
		return imageIcon;
	}
}
