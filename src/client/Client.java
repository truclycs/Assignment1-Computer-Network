package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import data.User;
import tags.Decode;
import tags.Encode;
import tags.Tags;

public class Client {

	public static ArrayList<User> clientarray = null;
	private ClientServer server;
	private static InetAddress IPserver;
	private int port_server = 9600;
	private String username = "";
	private boolean stop = false;
//	private Random rd =  new Random();
	private static int port_client = 10000;
	private static int port_group = 11000;
	private int timeout = 10000;  //time to each request is 10 seconds.
	private Socket socket_client;
	private ObjectInputStream server_input;
	private ObjectOutputStream server_output;
	
	public Client(String arg, int arg1, String name, String data_user) throws Exception {
		IPserver = InetAddress.getByName(arg);
		username = name;
		port_client = arg1;
		clientarray = Decode.getAllUser(data_user);
//		Random rd =  new Random();
//		port_group = port_group + rd.nextInt(999);
		new Thread(new Runnable(){
			@Override
			public void run() {
				updateFriend();
			}
		}).start();
		server = new ClientServer(username, port_group, IPserver);
		(new Request()).start();
	}

	public static int getPort() {
		return port_client;
	}
	
	public static InetAddress getIPserver() {
		return IPserver;
	}

	public void request() throws Exception {
		socket_client = new Socket();
		SocketAddress address_server = new InetSocketAddress(IPserver, 9600);
		socket_client.connect(address_server);
		String mess = Encode.sendRequest(username);
		server_output = new ObjectOutputStream(socket_client.getOutputStream());
		server_output.writeObject(mess);
		server_output.flush();
		server_input = new ObjectInputStream(socket_client.getInputStream());
		mess = (String) server_input.readObject();
		server_input.close();
		//		just for test
		System.out.println("Return to server? " + mess); //test server return to user
		clientarray = Decode.getAllUser(mess);
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateFriend();
			}
		}).start();
	}

	public class Request extends Thread {
		@Override
		public void run() {
			super.run();
			while (!stop) {
				try {
					Thread.sleep(timeout);
					request();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void intialNewChat(String IP, int host, String guest) throws Exception {
		final Socket connclient = new Socket(InetAddress.getByName(IP), host);
		ObjectOutputStream send_request = new ObjectOutputStream(connclient.getOutputStream());
		send_request.writeObject(Encode.sendRequestChat("0" + username));
		send_request.flush();
		ObjectInputStream recieved = new ObjectInputStream(connclient.getInputStream());
		String mess = (String) recieved.readObject();
		if (mess.equals(Tags.CHAT_DENY_TAG)) {
			MainGui.request("Your friend denied connect with you!", false);
			connclient.close();
			return;
		}
		new ChatGui(username, guest, connclient, port_client);
	}
	
	// update 1/11/19
	public void intialNewChatRoom(ArrayList<User> clients) throws Exception {
		int size = clients.size();
		ArrayList<String> nameGuest = new ArrayList<String>();
		String strNameGuest = "";
		for (int i = 0; i < size - 1; i++) {
			nameGuest.add(clients.get(i).getName());
			strNameGuest = strNameGuest + "-" + clients.get(i).getName();
		}
		strNameGuest = strNameGuest + "-" + clients.get(clients.size() - 1).getName();
		new ChatServer(nameGuest, IPserver, port_group);
//		strNameGuest = strNameGuest + "-" + clients.get(clients.size() - 1).getHost();
		
		ArrayList<String> nameTemp = new ArrayList<String>(nameGuest);
		Socket connclient;
		for (int i = 0; i < size - 1; i++) {
			connclient = new Socket(InetAddress.getByName(clients.get(i).getHost()), clients.get(i).getPort());
			ObjectOutputStream send_request = new ObjectOutputStream(connclient.getOutputStream());
			send_request.writeObject(Encode.sendRequestChat("1" + username + strNameGuest));
			send_request.flush();
			ObjectInputStream recieved = new ObjectInputStream(connclient.getInputStream());
			String mess = (String) recieved.readObject();
			if (mess.equals(Tags.CHAT_DENY_TAG)) {
				MainGui.request("Your friend denied connect with you!", false);
				connclient.close();
				return;
			}
		}
		ChatRoomGUI.main(clients.get(clients.size() - 1).getName(), IPserver, nameTemp, port_group);

		

//		ArrayList<String> nameTemp = new ArrayList<String>(nameGuest);
//		for (int i = 0; i < size; i++) {
////			ChatGroupClients.main(clients.get(i).getName(), IPserver);
//			ChatRoomGUI.main(clients.get(i).getName(), IPserver, nameTemp, port_group);
//			System.out.println("nameGuest :" + nameTemp.size());
//		}
		
		
//		int size = clients.size();
//		Random rd =  new Random();
//		port_group = port_group + rd.nextInt(999);
//		ArrayList<String> nameGuest = new ArrayList<String>();
//		for (int i = 0; i < size; i++) {
//			nameGuest.add(clients.get(i).getName());
//		}
//		new ChatServer(nameGuest, IPserver, port_group);
//		ArrayList<String> nameTemp = new ArrayList<String>(nameGuest);
//		for (int i = 0; i < size; i++) {
////			ChatGroupClients.main(clients.get(i).getName(), IPserver);
//			ChatRoomGUI.main(clients.get(i).getName(), IPserver, nameTemp, port_group);
//			System.out.println("nameGuest :" + nameTemp.size());
//		}
	}

	public void exit() throws IOException, ClassNotFoundException {
		stop = true;
		socket_client = new Socket();
		SocketAddress address_server = new InetSocketAddress(IPserver, port_server);
		socket_client.connect(address_server);
		String mess = Encode.exit(username);
		server_output = new ObjectOutputStream(socket_client.getOutputStream());
		server_output.writeObject(mess);
		server_output.flush();
		server_output.close();
		server.exit();
	}

	public void updateFriend(){
		int size = clientarray.size();
		MainGui.resetList();
		for (int i = 0; i < size; i++) {
			if (!clientarray.get(i).getName().equals(username))
				MainGui.updateFriendMainGui(clientarray.get(i).getName());
		}
	}
}