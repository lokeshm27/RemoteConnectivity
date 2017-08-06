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
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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
	boolean changed = false;

	public MainFrame() {
		log(Level.INFO, "mainFrame : Initiating frame");
		baseFrame = new JFrame("Remote Connectivity");
		baseFrame.setSize(400, 600);
		baseFrame.setResizable(false);
		baseFrame.setLocationRelativeTo(null);
		baseFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		baseFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onClose();
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
				onClose();
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
		log(Level.INFO, "openSettings: Settings Dialog initiating..");
		JDialog settingsDialog = new JDialog(baseFrame, "Settings - Remote Connectivity", true);
		settingsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		settingsDialog.setSize(400, 300);
		settingsDialog.setLocationRelativeTo(baseFrame);

		//<--------------------- Settings Portion 1 --------------------------->
		JPanel topPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.insets = new Insets(10, 20, 10, 5);
		gbc1.anchor = GridBagConstraints.WEST;

		JLabel portLabel = new JLabel("PORT : ");
		portLabel.setPreferredSize(new Dimension(125, 20));
		portLabel.setToolTipText("Specify port number to be used for the connection");
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		topPanel.add(portLabel, gbc1);

		JTextField port = new JTextField(5);
		port.setText(Integer.toString(VolatileBag.PORT));
		port.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int newPort = Integer.parseInt(port.getText());
					if(newPort > 1024 && newPort!=VolatileBag.PORT) {
						changed = true;
					}
				} catch (NumberFormatException e) {
					if(!port.getText().equals("")) {
						log(Level.SEVERE, "port Action Listner : Inlet filter allowed a non-numeric text.");
					}
				}
			}
		});
		PlainDocument doc = (PlainDocument) port.getDocument();
		doc.setDocumentFilter(new NumericFilter());
		
		gbc1.gridx = 1;
		gbc1.gridy = 0;
		topPanel.add(port, gbc1);
		
		JLabel deviceLabel = new JLabel("Device ID : ");
		deviceLabel.setPreferredSize(new Dimension(125, 20));
		deviceLabel.setToolTipText("Device ID is used to identify this device accross the network");
		
		gbc1.gridx = 0;
		gbc1.gridy = 1;
		topPanel.add(deviceLabel, gbc1);
		
		
		JTextField deviceID = new JTextField(15);
		deviceID.setText(VolatileBag.deviceID);
		deviceID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(deviceID.getText().trim().length() < 16 && !deviceID.getText().equals(VolatileBag.deviceID)) {
					changed = true;
				}
			}
		});
		
		gbc1.gridx = 1;
		gbc1.gridy = 1;
		topPanel.add(deviceID, gbc1);
		
		//<------------------------- Settings Portion 2 ------------------------------->
		JPanel middlePanel = new JPanel(new BorderLayout());
		//TODO middlePanel.setPreferredSize(new Dimension(380, 350));
		////TODO middlePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		//<--------------------- Settings Portion 2.1 -------------------------------->
		JPanel authPanel = new JPanel(new GridBagLayout());
		//authPanel.setPreferredSize(new Dimension(380, 100));
		GridBagConstraints gbc21 = new GridBagConstraints();
		gbc21.insets = new Insets(5, 20, 10, 5);
		gbc21.anchor = GridBagConstraints.WEST;
		gbc21.ipadx = 100;
		
		JCheckBox authenticate = new JCheckBox("Authenticate before accepting clients");
		authenticate.setToolTipText("If enabled, Only clients with specified password will be accepted.");
		
		gbc21.gridx = 0;
		gbc21.gridy = 0;
		authPanel.add(authenticate, gbc21);
		
		middlePanel.add(authPanel, BorderLayout.NORTH);
		
		//<------------------------ Settings Portion 2.2 ------------------------------>
		JPanel passwordPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc22 = new GridBagConstraints();
		gbc22.insets = new Insets(5, 50, 10, 5);
		gbc22.anchor = GridBagConstraints.WEST;
				
		JLabel passwdLabel = new JLabel("Password : ");
		JPasswordField passwd = new JPasswordField(10);
		passwd.setEchoChar('*');
		passwd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(passwd.getPassword().length  > 7) {
					changed = true;
				}
			}
		});
		
		JCheckBox showPasswd = new JCheckBox("Show password");
		showPasswd.setSelected(false);
		showPasswd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(showPasswd.isSelected()) {
					passwd.setEchoChar((char) 0);
				} else {
					passwd.setEchoChar('*');
				}
			}
		});
		
		gbc22.gridx = 0;
		gbc22.gridy = 0;
		passwordPanel.add(passwdLabel, gbc22);
		
		gbc22.gridx = 1;
		gbc22.gridy = 0;
		passwordPanel.add(passwd, gbc22);
		
		gbc22.gridx = 0;
		gbc22.gridy = 1;
		passwordPanel.add(showPasswd, gbc22);
		
		middlePanel.add(passwordPanel, BorderLayout.CENTER);
		//<---------------------------- End Portion 2.2 --------------------------------->
		
		
		if(VolatileBag.authenticate) {
			authenticate.setSelected(true);
			passwordPanel.setVisible(true);
			passwd.setText(VolatileBag.password);
		} else {
			authenticate.setSelected(false);
			settingsDialog.setSize(400, 310);
			passwordPanel.setVisible(false);
		}
		
		authenticate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(authenticate.isSelected()) {
					settingsDialog.setSize(400, 380);
					passwordPanel.setVisible(true);
					if(VolatileBag.authenticate) {
						passwd.setText(VolatileBag.password);
					} else {
						passwd.setText("");
					}
				} else {
					settingsDialog.setSize(400, 310);
					passwordPanel.setVisible(false);
				}
			}
		});
		
		//<----------------------------- Settings Portion 3 ------------------------------>
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.insets = new Insets(10, 20, 10, 5);
		gbc3.anchor = GridBagConstraints.WEST;
		
		JCheckBox allowReq = new JCheckBox("Allow Requests");
		allowReq.setToolTipText("If enabled, Clients will be able to send request to this server.");
		allowReq.setSelected(VolatileBag.allowRequests);
		
		JLabel edit = new JLabel("<HTML><U style=\"color:blue\">Edit type of requests allowed</U></HTML>");
		edit.setToolTipText("Click to allow only specified type of requests.");
		edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		edit.setEnabled(VolatileBag.allowRequests);
		allowReq.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(allowReq.isSelected()) {
					edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
					edit.setEnabled(true);
				} else {
					edit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					edit.setEnabled(false);
				}
			}
		});
		
		gbc3.gridx = 0;
		gbc3.gridy = 0;
		bottomPanel.add(allowReq, gbc3);
		
		gbc3.gridx = 0;
		gbc3.gridy = 1;
		bottomPanel.add(edit, gbc3);
		
		//<---------------------------------- Settings Portion 4 ------------------------------------>
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc4 = new GridBagConstraints();
		gbc4.insets = new Insets(10, 20, 10, 20);
		gbc4.anchor = GridBagConstraints.SOUTH;
		
		JButton save = new JButton("Save");
		
		JButton reset = new JButton("Reset to default");
		
		JButton cancel = new JButton("Cancel");
		
		gbc4.gridy = 0;
		
		gbc4.gridx = 0;
		buttonPanel.add(save, gbc4);
		
		gbc4.gridx = 1;
		buttonPanel.add(reset, gbc4);
		
		gbc4.gridx = 2;
		buttonPanel.add(cancel, gbc4);
		
		settingsDialog.setLayout(new FlowLayout(FlowLayout.LEFT));
		settingsDialog.add(topPanel);
		settingsDialog.add(middlePanel);
		settingsDialog.add(bottomPanel);
		settingsDialog.add(buttonPanel);
		
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
	
	public void onClose() {
		if(VolatileBag.alwaysConfirm) {
			if (JOptionPane.showConfirmDialog(baseFrame, "Do you want to stop server and exit application.?",
					"Sure to Exit?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}
		}
		baseFrame.dispose();
		if (switchLabel.getText().equals("  ON")) {
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
