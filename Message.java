import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int HS_REQ = 1;
	public static final int HS_REP = 2;
	public static final int SERV_REQ = 3;
	public static final int SERV_REP = 4;
	public static final int REQ = 5;
	public static final int REQ_REP = 6;
	public static final int SUCCESS = 0;
	public static final int UNKNOWN = -1;
	public static final int NOT_FOUND = -2;
	public static final int REJECTED = -3;
	public static final int ADD_DEV = 11;
	public static final int REM_DEV = 22;
	
	public int type = 0;
	public String fromID = null;
	public String toID = null;
	public boolean authRequired = false;
	public boolean authMessage = false;
	public String password = null;
	public Device device;
	public int serviceType = 0;
	public int reqType = 0;
	public String reqCommand = null;
	public int repStatus = 0;
	public String repMessage = null;
	
	public Message HsReqMessage(String toID, boolean authRequired) {
		this.type = HS_REQ;
		this.fromID = VolatileBag.deviceID;
		this.toID = toID;
		this.authRequired = authRequired;
		return this;
	}
	
	public Message HsRepMessage(String toID) {
		//Handshake Replay message with no password
		//Not Used 
		return null;
	}
	
	public Message HsRepMessage(String toID, String password) {
		//Handshake reply message with password
		//Not Used
		return null;
	}
	
	public Message ServReqMessage(String toID) {
		//Service request message
		//Not used
		return null;
	}
	
	public Message ServRepMessage(String toID, int serviceType, Device device) {
		this.type = SERV_REP;
		this.fromID = VolatileBag.deviceID;
		this.toID = toID;
		this.serviceType = serviceType;
		this.device = device;
		
		return this;
	}
	
	public Message ReqMessage(String toID, int reqType, String reqCommand) {
		this.type = REQ;
		this.fromID = VolatileBag.deviceID;
		this.toID = toID;
		this.reqType = reqType;
		this.reqCommand = reqCommand;
		
		return this;
	}
	
	public Message reqRepMessage(String toID, int repStatus, String repMessage) {
		this.type = REQ_REP;
		this.fromID = VolatileBag.deviceID;
		this.toID = toID;
		this.repStatus = repStatus;
		this.repMessage = repMessage;
		
		return this;
	}
}
