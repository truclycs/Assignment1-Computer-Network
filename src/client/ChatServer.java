package client;

//Chat Server runs at port no. 9999
import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;
public class  ChatServer {
	private ArrayList<String> users = new ArrayList<String>();
	private ArrayList<HandleClient> clients = new ArrayList<HandleClient>();
	private InetAddress IPserver;
	public ChatServer(ArrayList<String> Users, InetAddress _IPserver){
		users = Users;
		IPserver = _IPserver;
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					process();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	public void process() throws Exception  {
		 ServerSocket server = new ServerSocket(9960, 10, IPserver);
		 out.println("Server Started...");
		 while( true) {
				 Socket client = server.accept();
				 HandleClient c = new HandleClient(client);
				 clients.add(c);
		}  // end of while
	}
//	public static void main(String ... args) throws Exception {
//		 new ChatServer().process();
//		} // end of main
	public void broadcast(String user, String message)  {
		// send message to all connected users
		for ( HandleClient c : clients )
			if ( ! c.getUserName().equals(user) )	
				c.sendMessage(user,message);
	}
	class  HandleClient extends Thread {
		String name = "";
		BufferedReader input;
		PrintWriter output;
		public HandleClient(Socket  client) throws Exception {
			// get input and output streams
			 input = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
			 output = new PrintWriter ( client.getOutputStream(),true);
			 // read name
			 name  = input.readLine();
			 users.add(name); // add to vector
			 start();
			 }
		public void sendMessage(String uname,String  msg)  {
			output.println( uname + ":" + msg);
			}
				
		   public String getUserName() {  
		       return name; 
		   }
		   public void run()  {
			     String line;
			     try    {
		           while(true)   {
				 line = input.readLine();
				 if ( line.equals("end") ) {
				   clients.remove(this);
				   users.remove(name);
				   break;
				   }
				 broadcast(name,line); // method  of outer class - send messages to all
			       } // end of while
			     } // try
			     catch(Exception ex) {
			       System.out.println(ex.getMessage());
			     }
		   } // end of run()
	} // end of inner class
} // end of Server