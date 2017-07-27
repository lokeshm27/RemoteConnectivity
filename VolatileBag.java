import java.net.Socket;
import java.util.HashMap;

public class VolatileBag {
	volatile static public int numDevices = 0;
	volatile static public HashMap<String, Device> deviceList = new HashMap<String, Device>();
	volatile static public HashMap<String, Socket> socketList = new HashMap<String, Socket>();
	volatile static public boolean startServer = false;
	volatile static public boolean authenticate = false;
	volatile static public String password = "%empty%";
	volatile static public boolean allowRequests = true;
	volatile static public String deviceID = "LOKESH-PC";
}
