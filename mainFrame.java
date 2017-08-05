import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.text.PlainDocument;

public class MainFrame {
	public static JFrame baseFrame;
	static JLabel switchLabel;
	static JLabel statusLabel;
	static JPanel container;
	static JScrollPane scroll;
	static Font heading;
	static JPanel centerPanel;
	static JPanel bottomPanel;
	ServerThread serverThread;

	public MainFrame() {
		log(Level.INFO, "mainFrame : Initiating frame");
		baseFrame = new JFrame("Remote Connectivity - " + VolatileBag.deviceID);
		baseFrame.setSize(400, 600);
		baseFrame.setResizable(false);
		baseFrame.setLocationRelativeTo(null);
		baseFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		baseFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(baseFrame, "Do you want to stop server and exit application.?",
						"Sure to Exit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					baseFrame.dispose();
					if (switchLabel.getText().equals("  ON")) {
						finishUp();
					}
				}
			}
		});

		// <------------------ Frame Portion 1 --------------------->
		JPanel topPanel = new JPanel(new GridBagLayout());
		heading = new Font("Serif - PLAIN - 12", Font.BOLD, 18);
		JLabel serverLabel = new JLabel("Server : ");
		serverLabel.setFont(heading);
		switchLabel = new JLabel();
		switchLabel.setFont(heading);
		switchLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		switchLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				switchState();
			}
		});
		topPanel.setPreferredSize(new Dimension(300, 70));
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.insets = new Insets(0, 5, 0, 10);

		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.anchor = GridBagConstraints.WEST;
		serverLabel.setPreferredSize(new Dimension(175, 70));
		topPanel.add(serverLabel, gbc1);

		gbc1.gridx = 1;
		gbc1.gridy = 0;
		gbc1.anchor = GridBagConstraints.EAST;
		switchLabel.setPreferredSize(new Dimension(175, 70));
		topPanel.add(switchLabel, gbc1);

		// <-------------------- Frame Portion 2 ---------------------->
		centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 5, 0, 5);

		// <-------------------- Frame Portion 2.1 ----------------------->
		JLabel clientList = new JLabel("Client List : ");
		clientList.setFont(heading);

		statusLabel = new JLabel("Loading.....");
		statusLabel.setIcon(new ClientPanel().getSizedIcon("/images/refresh.png", 15, 15));
		statusLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		statusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateList(true);
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		centerPanel.add(clientList, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		centerPanel.add(statusLabel, gbc);

		// <-------------------- Frame Portion 2.2 ----------------------->
		scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(380, 300));
		scroll.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.gray));

		container = new JPanel(new FlowLayout(FlowLayout.CENTER));
		container.setPreferredSize(new Dimension(345, 350));

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		centerPanel.add(scroll, gbc);

		// <-------------------- Frame Portion 2.3 ----------------------->
		JLabel message = new JLabel("Click on the client to send message");

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 2;
		centerPanel.add(message, gbc);

		if (VolatileBag.startServer) {
			switchLabel.setText("  ON");
			switchLabel.setIcon(new ClientPanel().getSizedIcon("/images/on.png", 30, 30));
			updateList(false);
		} else {
			centerPanel.setVisible(false);
			switchLabel.setText(" OFF");
			switchLabel.setIcon(new ClientPanel().getSizedIcon("/images/off.png", 30, 30));
		}

		// <-------------------- Frame Portion 3 ------------------------>
		bottomPanel = new JPanel(new GridBagLayout());
		bottomPanel.setPreferredSize(new Dimension(400, 100));
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.insets = new Insets(10, 10, 10, 10);
		gbc2.ipadx = 50;

		JButton settings = new JButton("Settings", new ClientPanel().getSizedIcon("/images/settings.png", 20, 20));
		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openSettings();
			}
		});

		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.anchor = GridBagConstraints.WEST;
		bottomPanel.add(settings, gbc2);

		JButton exit = new JButton("Exit", new ClientPanel().getSizedIcon("/images/exit.png", 20, 20));
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(baseFrame, "Do you want to stop server and exit application.?",
						"Sure to Exit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					baseFrame.dispose();
					if (switchLabel.getText().equals("  ON")) {
						finishUp();
					}
				}
			}
		});

		gbc1.gridx = 1;
		gbc1.gridy = 0;
		gbc1.anchor = GridBagConstraints.EAST;
		bottomPanel.add(exit, gbc2);

		baseFrame.add(topPanel, BorderLayout.NORTH);
		baseFrame.add(centerPanel, BorderLayout.CENTER);
		baseFrame.add(bottomPanel, BorderLayout.SOUTH);
		baseFrame.setVisible(true);
	}

	private void openSettings() {
		boolean changed = false;
		JDialog settingsDialog = new JDialog(baseFrame, "Settings - Remote Connectivity", true);
		settingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		settingsDialog.setSize(400, 600);
		settingsDialog.setLocationRelativeTo(baseFrame);

		JPanel container = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 20, 20);

		JLabel portLabel = new JLabel("PORT : ");

		gbc.gridx = 0;
		gbc.gridy = 0;
		container.add(portLabel, gbc);

		JTextField port = new JTextField(5);
		PlainDocument doc = (PlainDocument) port.getDocument();
		doc.setDocumentFilter(new NumericFilter());
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		container.add(port, gbc);
		
		settingsDialog.add(container);
		settingsDialog.setVisible(true);
	}

	public static void updateList(boolean showMsg) {
		container = new JPanel(new FlowLayout(FlowLayout.CENTER));
		container.setPreferredSize(new Dimension(380, 350));
		if (!statusLabel.getText().equals("Refreshing...")) {
			System.out.println("Updating list");
			ClientPanel temp;
			statusLabel.setText("Refreshing...");
			for (String key : VolatileBag.deviceList.keySet()) {
				temp = new ClientPanel(VolatileBag.deviceList.get(key));
				container.add(temp);
			}
			scroll.getViewport().add(container);
			if (showMsg)
				JOptionPane.showMessageDialog(baseFrame, "Device list has been succesfully updated.!",
						"Device list refresh complete.", JOptionPane.INFORMATION_MESSAGE);
			statusLabel.setText("Refresh");
		}
	}

	private void switchState() {
		System.out.println("Server state switch.!");
		if (switchLabel.getText().equals(" OFF")) {
			centerPanel.setVisible(true);
			switchLabel.setText("  ON");
			switchLabel.setIcon(new ClientPanel().getSizedIcon("/images/on.png", 30, 30));
			serverThread = new ServerThread();
			serverThread.start();
			updateList(false);
		} else {
			centerPanel.setVisible(false);
			switchLabel.setText(" OFF");
			switchLabel.setIcon(new ClientPanel().getSizedIcon("/images/off.png", 30, 30));
			finishUp();
		}
	}

	public void finishUp() {
		log(Level.INFO, "Finish-Up initiated.!");
		try {
			serverThread.server.close();
			for (Socket socket : VolatileBag.socketList.values()) {
				socket.close();
			}
			VolatileBag.deviceList = new HashMap<String, Device>();
			VolatileBag.socketList = new HashMap<String, Socket>();
		} catch (IOException e) {
			log(Level.SEVERE, "finishUp: Exception while clean-up process " + e.getMessage());
		}
	}

	private void log(Level level, String message) {
		if (Start.loggingEnabled) {
			Start.logger.log(level, message);
		}
	}

}
