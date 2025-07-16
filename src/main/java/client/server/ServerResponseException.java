package client.server;

public class ServerResponseException extends Exception {

//	public ServerResponseException() {
//		  super("Server Response Exception!");
//	}
	
	public ServerResponseException(String message) {
		  super("Server Response Exception: " + message);
	}
}