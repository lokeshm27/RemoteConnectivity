import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = 1L;

	public boolean startServer;
	public boolean authenticate;
	public String password;
	public String deviceID;
	public boolean allowRequests;
	public int PORT;
	public boolean alwaysConfirm;
	
	public Data(boolean startServer, String deviceID, boolean allowRequests, int PORT, boolean alwaysConfirm) {
		this.startServer = startServer;
		this.authenticate = false;
		this.password = "%empty%";
		this.deviceID = deviceID;
		this.allowRequests = allowRequests;
		this.PORT = PORT;
		this.alwaysConfirm = alwaysConfirm;
	}
	
	public Data(boolean startServer, String deviceID, boolean allowRequests, String password, int PORT, boolean alwaysConfirm) {
		this.startServer = startServer;
		this.authenticate = true;
		this.password = password;
		this.deviceID = deviceID;
		this.allowRequests = allowRequests;
		this.PORT = PORT;
		this.alwaysConfirm = alwaysConfirm;
	}
	
}
