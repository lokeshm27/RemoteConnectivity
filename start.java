import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

public class start {
	
	public static Logger logger;
	public static boolean loggingEnabled = false;
	
	public static void main(String args[]) {
		if(init()) {
			new mainFrame();
		}
	}
	
	public static boolean init() {
		try {
			logger = Logger.getLogger("MyLog");
			FileHandler fh = new FileHandler("C:/ProgramData/RemoteConnectivity/Log.log");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        loggingEnabled = true;
	        log(Level.INFO, "Init : Logger initiated.!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to open log file. Proceeding without logging.!", "Logging Error.!", JOptionPane.ERROR_MESSAGE);
		}
		
		File data = new File("C:/ProgramData/RemoteConnectivity/Settings.dat");
		if(!data.exists()) {
			System.out.println("Creating new Data file");
			new File("C:/ProgramData/RemoteConnectivity").mkdir();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(data));
				oos.writeObject(new Data(false, "LOKESH-PC", true));
				oos.close();
				return true;
			} catch (IOException e) {
				log(Level.SEVERE, "Init : IOException while writing Data file : " + e.getMessage());
				return false;
			}
		} else {
			System.out.println("Reading new Data file");
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data));
				Data temp = (Data) ois.readObject();
				ois.close();
				
				VolatileBag.startServer = temp.startServer;
				VolatileBag.deviceID = temp.deviceID;
				VolatileBag.allowRequests = temp.allowRequests;
				VolatileBag.authenticate = temp.authenticate;
				VolatileBag.password = temp.password;
				return true;
			} catch (Exception e) {
				log(Level.WARNING, "Init : Exception while reading data file : " + e.getMessage());
				log(Level.INFO, "Init: Unable to open data file. Deleing it and loading default settings..");
				data.delete();
				return false;
			}
		}
	}
	
	private static void log(Level level, String message) {
		if(loggingEnabled) {
			logger.log(level, message);
		}
	}
}
