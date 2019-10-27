package data;

public class User {	
	private String username = "";
	private String userhost = "";
	private int userport = 0;

	public void setPeer(String name, String host, int port) {
		username = name;
		userhost = host;
		userport = port;
	}

	public void setName(String name) {
		username = name;
	}

	public void setHost(String host) {
		userhost = host;
	}

	public void setPort(int port) {
		userport = port;
	}

	public String getName() {
		return username;
	}

	public String getHost() {
		return userhost;
	}

	public int getPort() {
		return userport;
	}
}

