import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean startServer;
	public boolean authenticate;
	public String password;
	public String deviceID;
	public boolean allowRequests;
	public int PORT;
	
	public Data(boolean startServer, String deviceID, boolean allowRequests, int PORT) {
		this.startServer = startServer;
		this.authenticate = false;
		this.password = "%empty%";
		this.deviceID = deviceID;
		this.allowRequests = allowRequests;
		this.PORT = PORT;
	}
	
	public Data(boolean startServer, String deviceID, boolean allowRequests, String password, int PORT) {
		this.startServer = startServer;
		this.authenticate = true;
		this.password = password;
		this.deviceID = deviceID;
		this.allowRequests = allowRequests;
		this.PORT = PORT;
	}
	
}
