public class ProcessThread extends Thread{
	Message msg;
	
	public ProcessThread(Message msg) {
		this.msg = msg;
	}
	
	@Override
	public void run() {
		System.out.println("Processing message from " + msg.fromID);
		switch(msg.type) {
		case Message.SERV_REQ :
			System.out.println("Message type : Service request");
			break;
			
		case Message.SERV_REP :
			System.out.println("Message type : Service reply");
			System.out.println("Service Type : " + msg.serviceType);
			System.out.println("Device : " + msg.device.deviceID);
			break;
			
		case Message.REQ :
			System.out.println("Message type : Requset");
			System.out.println("Request type : " + msg.reqType);
			System.out.println("Request command : " + msg.reqCommand);
			break;
			
		case Message.REQ_REP :
			System.out.println("Message type : Requset Reply");
			System.out.println("Reply status : " + msg.repStatus);
			System.out.println("Reply message : " + msg.repMessage);
			break;
			
		default : System.out.println("Message type : " + msg.type);
		}
	}
}
