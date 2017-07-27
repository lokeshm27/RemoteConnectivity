public class Device {
	public static final int PHONE = 0;
	public static final int TV = 1;
	public static final int WEAR = 2;
	public static final int LAPTOP = 3;
	
	public String deviceID;
	public int deviceType;
	
	public Device(String ID, int type) { 
		deviceID = ID;
		deviceType = type;
	}
}
