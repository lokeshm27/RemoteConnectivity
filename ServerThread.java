import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.logging.Level;
import javax.swing.JOptionPane;

public class ServerThread extends Thread{
	
	public ServerSocket server;
	ReaderThread thread;
	
	public ServerThread() {
		try {
			server = new ServerSocket(VolatileBag.PORT);
		} catch (IOException e) {
			log(Level.SEVERE, "serverThread: Unable to open Port : " + VolatileBag.PORT 
					+ "\nAsking to change port number.");
			JOptionPane.showMessageDialog(MainFrame.baseFrame, "Unable to Open server Port. Port may be used by other applications" 
					+ "\nTry changing Port number in the settings.", "Port Unavailable", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				thread = new ReaderThread(server.accept());
				thread.start();
			}
		} catch (SocketException e) {
			log(Level.WARNING, "run: SocketException occurred : " + e.getMessage() 
			+ "\nAssuming try to close ServerSocket. Cleaning up ServerThread.");
		} catch (IOException e) {
			log(Level.SEVERE, "run: Exception occured while connecting to Client : " + e.getMessage());
			JOptionPane.showMessageDialog(MainFrame.baseFrame, "Unable to establish connection with client", "Connection Failed.!", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				server.close();
			} catch (IOException e1) {
				log(Level.WARNING, "run : ServerThread Cleaning process interrupted : " + e1.getMessage() + "\nUnable to close RecieverThread.");
			}
		}
	}
	
	private void log(Level level, String msg) {
		if(Start.loggingEnabled) {
			Start.logger.log(level, msg);
		}
	}
	
}
