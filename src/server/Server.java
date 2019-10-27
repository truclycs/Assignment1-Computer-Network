package server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import data.User;
import tags.Decode;
import tags.Tags;

public class Server {
	private ArrayList<User> list_users = null;	
	private ServerSocket server;						
	private Socket connection;			
	private ObjectInputStream ob_input;	
	private ObjectOutputStream ob_output;				
	public boolean stop = false, exit = false;		
	
	//Intial server socket
	public Server(int port) throws Exception {
		server = new ServerSocket(port);		
		list_users = new ArrayList<User>();
		(new WaitForConnect()).start();			
	}
	
	//	show status of state
	private String sendSessionAccept() throws Exception {
		String mess = Tags.SESSION_ACCEPT_OPEN_TAG;
		int size = list_users.size();				
		for (int i = 0; i < size; i++) {		
			User user = list_users.get(i);	
			mess += Tags.USER_OPEN_TAG;			
			mess += Tags.USER_NAME_OPEN_TAG;
			mess += user.getName();
			mess += Tags.USER_NAME_CLOSE_TAG;
			mess += Tags.IP_OPEN_TAG;
			mess += user.getHost();
			mess += Tags.IP_CLOSE_TAG;
			mess += Tags.PORT_OPEN_TAG;
			mess += user.getPort();
			mess += Tags.PORT_CLOSE_TAG;
			mess += Tags.USER_CLOSE_TAG;			
		}
		mess += Tags.SESSION_ACCEPT_CLOSE_TAG;	
		return mess;
	}
	
	//	close server
	public void stopserver() throws Exception {
		stop = true;
		server.close();							
		connection.close();						
	}
	
	//client connect to server
	private boolean connect() throws Exception {
		connection = server.accept();			
		ob_input = new ObjectInputStream(connection.getInputStream());		
		String mess = (String) ob_input.readObject();						
		ArrayList<String> get_data = Decode.getUser(mess);					
		ServerGui.updateMessage(mess);											
		if (get_data != null) {
			if (!isExsistName(get_data.get(0))) {						
				addNewUser(get_data.get(0), connection.getInetAddress()			
						.toString(), Integer.parseInt(get_data.get(1)));			
				ServerGui.updateMessage(get_data.get(0));	
				ServerGui.updateNumberClient();
			} 
			else {
				return false;
			}
		} 
		else {
			int size = list_users.size();			
			
			Decode.updateUserOnline(list_users, mess);	
			if (size != list_users.size()) {					
				exit = true;								
				ServerGui.decreaseNumberClient();
			}
		}
		return true;
	}
	
	private void addNewUser(String user, String ip, int port) throws Exception {
		User newPeer = new User();		
		if (list_users.size() == 0)				
			list_users = new ArrayList<User>();
		newPeer.setPeer(user, ip, port);		
		list_users.add(newPeer);					
	}
	
	private boolean isExsistName(String name) throws Exception {
		if (list_users == null)
			return false;
		int size = list_users.size();
		for (int i = 0; i < size; i++) {
			User user = list_users.get(i);
			if (user.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public class WaitForConnect extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				while (!stop) {
					if (connect()) {
						if (exit) {
							exit = false;
						} 
						else {
							ob_output = new ObjectOutputStream(connection.getOutputStream());
							ob_output.writeObject(sendSessionAccept());
							ob_output.flush();
							ob_output.close();
						}
					} 
					else {
						ob_output = new ObjectOutputStream(connection.getOutputStream());
						ob_output.writeObject(Tags.SESSION_DENY_TAG);
						ob_output.flush();
						ob_output.close();
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

