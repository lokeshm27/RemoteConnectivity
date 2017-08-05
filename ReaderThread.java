import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;

public class ReaderThread extends Thread{
	Socket socket;
	Message sendMsg;
	Message recMsg;
	ObjectOutputStream out;
	ObjectInputStream in;
	ProcessThread process;
	Device dev;
	
	public ReaderThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			//Sending handshake request message
			log(Level.INFO, "run: New Client connected. Handshake is in Progress");
			sendMsg = new Message().HsReqMessage("%UNKNOWN%", VolatileBag.authenticate);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.writeObject(sendMsg);
			out.flush();
			
			//Waiting for reply
			socket.setSoTimeout(5000);
			recMsg = (Message) in.readObject();
			log(Level.INFO, "run: Handshake reply recieved. Processing.");
			if(VolatileBag.authenticate) {
				log(Level.INFO, "run: Authenticating device.");
				if(recMsg.authMessage) {
					log(Level.INFO, "run: Device sent a authenticating message, Verifying Password.");
					if(recMsg.password.equals(VolatileBag.password)) {
						log(Level.INFO, "run: Password verification success. Adding device to list.");
					} else {
						log(Level.INFO, "run: Incorrect password. Closing connection");
						socket.close();
						return;
					}
				} else {
					log(Level.INFO, "run: Device did not sent a authenticating message. Closing connection");
					socket.close();
					return;
				}
			} else {
				log(Level.INFO, "run: Authentication not required. Adding device to the list.");
			}
			dev = recMsg.device;
			VolatileBag.deviceList.put(dev.deviceID, dev);
			VolatileBag.socketList.put(dev.deviceID, socket);
		} catch (SocketException e ) {
			log(Level.WARNING, "run: SocketException occurred while validating client : " + e.getMessage());
			cleanUp();
			return;
		} catch (IOException e) {
			log(Level.WARNING, "run: IOException occurred while validating client : " + e.getMessage());
			cleanUp();
			return;
		} catch (ClassNotFoundException e) {
			log(Level.SEVERE, "run: Client sent a message that can not be converted to Message object. Invalid Client");
			cleanUp();
			return;
		}
		
		//Device added. Listening for message
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e) {
			log(Level.SEVERE, "run: SetTimout method threw exception");
			cleanUp();
			deleteDev();
		}
		
		
		try {
			while(true) {
				recMsg = (Message) in.readObject();
				log(Level.INFO, "run: Recieved Message. Processing");
				process = new ProcessThread(recMsg);
			}
		} catch (SocketException e) {
			log(Level.INFO, "run: SocketException");
			if(Thread.currentThread().isInterrupted()) {
				log(Level.INFO, "run: Thread interrupted.");
				cleanUp();
			} else {
				log(Level.INFO, "run: Client closed connection");
				cleanUp();
				deleteDev();
			}
		} catch (IOException e) {
			log(Level.WARNING, "run: IOException occurred while listening : " + e.getMessage());
			cleanUp();
		} catch (ClassNotFoundException e) {
			log(Level.SEVERE, "run: Client sent a message that can not be converted to Message object. Invalid Client");
			cleanUp();
		}
	}
	
	private void cleanUp() {
		try {
			socket.close();
		} catch (IOException e) {
			log(Level.SEVERE, "run: Problem with clean up process : " + e.getMessage());
		}
	}
	
	private void deleteDev() {
		VolatileBag.deviceList.remove(dev.deviceID);
		VolatileBag.socketList.remove(dev.deviceID);
		MainFrame.updateList(false);
		
		// Sending remove message to all clients
		Message rmMsg;
		ObjectOutputStream oos;
		for (String deviceID : VolatileBag.socketList.keySet()) {
			try {
			rmMsg = new Message().ServRepMessage(deviceID, Message.REM_DEV, VolatileBag.deviceList.get(dev.deviceID));
			oos = new ObjectOutputStream(VolatileBag.socketList.get(deviceID).getOutputStream());
			oos.writeObject(rmMsg);
			oos.flush();
			} catch (IOException e ) {
				log(Level.SEVERE, "deleteDev: Exception while sending remove message to " + deviceID);
			}
		}
	}
	
	private void log(Level level, String msg) {
		if(Start.loggingEnabled) {
			Start.logger.log(level, msg);
		}
	}
	
}
