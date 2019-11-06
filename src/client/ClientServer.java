package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import tags.Decode;
import tags.Tags;
import client.Client;
import client.ChatRoomGUI;
import client.ChatServer;

public class ClientServer {

	private String username = "";
	private ServerSocket serverClient;
	private int port, port_group;
	private boolean isStop = false;
	private InetAddress IPserver;

//	public void stopServerPeer() {
//		isStop = true;
//	}
//
//	public boolean getStop() {
//		return isStop;
//	}

	public ClientServer(String name, int portGroup, InetAddress IPServer) throws Exception {
		username = name;
		port = Client.getPort();
		serverClient = new ServerSocket(port);
		port_group = portGroup;
		IPserver = IPServer;
		(new WaitPeerConnect()).start();
	}
	
	public void exit() throws IOException {
		isStop = true;
		serverClient.close();
	}

	class WaitPeerConnect extends Thread {

		Socket connection;
		ObjectInputStream getRequest;

		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					connection = serverClient.accept();
					getRequest = new ObjectInputStream(connection.getInputStream());
					String msg = (String) getRequest.readObject();
					String name = Decode.getNameRequestChat(msg);
					String isRoom = name.substring(0, 1);
					name = name.substring(1);
					String[] totalName = name.split("-");
					name = totalName[0];
					
					int res = MainGui.request("Account: " + name + " want to connect with you !", true);
					ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
					if (res == 1) {
						send.writeObject(Tags.CHAT_DENY_TAG);

					} else if (res == 0) {
						send.writeObject(Tags.CHAT_ACCEPT_TAG);
						System.out.println("port_group = " + port_group);
						if(isRoom.equals("1")) {
							ArrayList<String> nameTemp = new ArrayList<String>();
							for (int i = 0; i < totalName.length - 2; i++) { //-1
								System.out.println(totalName[i]);
								nameTemp.add(totalName[i]);
							}
//							nameTemp.add("Group");
							System.out.println(IPserver + "   "  + username);
							
//							ChatRoomGUI.main(username, IPserver, nameTemp, port_group);
							ChatRoomGUI.main(username, InetAddress.getByName(totalName[totalName.length - 1]), nameTemp, port_group);
//							new ChatGui(username, name, connection, port);
						}
						else {
							new ChatGui(username, name, connection, port);
						}
					}
					send.flush();
				} catch (Exception e) {
					break;
				}
			}
			try {
				serverClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
