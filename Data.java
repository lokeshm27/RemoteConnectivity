import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean startServer;
	public boolean authenticate;
	public String password;
	public String deviceID;
	public boolean allowRequests = true;
	
	public Data(boolean startServer, String deviceID, boolean allowRequests) {
		this.startServer = startServer;
		this.authenticate = false;
		this.password = "%empty%";
		this.deviceID = deviceID;
		this.allowRequests = allowRequests;
	}
	
	public Data(boolean startServer, String deviceID, boolean allowRequests, String password) {
		this.startServer = startServer;
		this.authenticate = true;
		this.password = password;
		this.deviceID = deviceID;
		this.allowRequests = allowRequests;
	}
	
}
