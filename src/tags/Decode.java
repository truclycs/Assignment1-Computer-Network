package tags;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import data.User;

public class Decode {
	private static Pattern createAccount = Pattern
			.compile(Tags.SESSION_OPEN_TAG + Tags.USER_NAME_OPEN_TAG + ".*"
					+ Tags.USER_NAME_CLOSE_TAG + Tags.PORT_OPEN_TAG + ".*"
					+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG);

	private static Pattern users = Pattern.compile(Tags.SESSION_ACCEPT_OPEN_TAG
			+ "(" + Tags.USER_OPEN_TAG + Tags.USER_NAME_OPEN_TAG + ".+"
			+ Tags.USER_NAME_CLOSE_TAG + Tags.IP_OPEN_TAG + ".+"
			+ Tags.IP_CLOSE_TAG + Tags.PORT_OPEN_TAG + "[0-9]+"
			+ Tags.PORT_CLOSE_TAG + Tags.USER_CLOSE_TAG + ")*"
			+ Tags.SESSION_ACCEPT_CLOSE_TAG);

	private static Pattern request = Pattern
			.compile(Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.USER_NAME_OPEN_TAG
					+ "[^<>]+" + Tags.USER_NAME_CLOSE_TAG
					+ Tags.STATUS_OPEN_TAG + "(" + Tags.SERVER_ONLINE + "|"
					+ Tags.SERVER_OFFLINE + ")" + Tags.STATUS_CLOSE_TAG
					+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG);

	private static Pattern message = Pattern.compile(Tags.CHAT_MSG_OPEN_TAG
			+ ".*" + Tags.CHAT_MSG_CLOSE_TAG);

	private static Pattern checkNameFile = Pattern
			.compile(Tags.FILE_REQ_OPEN_TAG + ".*" + Tags.FILE_REQ_CLOSE_TAG);

	private static Pattern feedBack = Pattern
			.compile(Tags.FILE_REQ_ACK_OPEN_TAG + ".*"
					+ Tags.FILE_REQ_ACK_CLOSE_TAG);

	public static ArrayList<String> getUser(String mess) {
		ArrayList<String> user = new ArrayList<String>();
		if (createAccount.matcher(mess).matches()) {
			Pattern find_name = Pattern.compile(Tags.USER_NAME_OPEN_TAG + ".*"
					+ Tags.USER_NAME_CLOSE_TAG);
			Pattern find_port = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
					+ Tags.PORT_CLOSE_TAG);
			Matcher find = find_name.matcher(mess);
			if (find.find()) {
				String name = find.group(0);
				user.add(name.substring(11, name.length() - 12));
				find = find_port.matcher(mess);
				if (find.find()) {
					String port = find.group(0);
					user.add(port.substring(6, port.length() - 7));
				} 
				else {
					return null;
				}
			} 
			else {
				return null;
			}
		} 
		else {
			return null;
		}
		return user;
	}

	public static ArrayList<User> getAllUser(String mess) {
		ArrayList<User> user = new ArrayList<User>();
		Pattern find_user = Pattern.compile(Tags.USER_OPEN_TAG
				+ Tags.USER_NAME_OPEN_TAG + "[^<>]*" + Tags.USER_NAME_CLOSE_TAG
				+ Tags.IP_OPEN_TAG + "[^<>]*" + Tags.IP_CLOSE_TAG
				+ Tags.PORT_OPEN_TAG + "[0-9]*" + Tags.PORT_CLOSE_TAG
				+ Tags.USER_CLOSE_TAG);
		Pattern find_name = Pattern.compile(Tags.USER_NAME_OPEN_TAG + ".*"
				+ Tags.USER_NAME_CLOSE_TAG);
		Pattern find_port = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
				+ Tags.PORT_CLOSE_TAG);
		Pattern find_IP = Pattern.compile(Tags.IP_OPEN_TAG + ".+"
				+ Tags.IP_CLOSE_TAG);
		if (users.matcher(mess).matches()) {
			Matcher find = find_user.matcher(mess);
			while (find.find()) {
				String peer = find.group(0);
				String data = "";
				User data_user = new User();
				Matcher find_info = find_name.matcher(peer);
				if (find_info.find()) {
					data = find_info.group(0);
					data_user.setName(data.substring(11, data.length() - 12));
				}
				find_info = find_IP.matcher(peer);
				if (find_info.find()) {
					data = find_info.group(0);
					data_user.setHost(find_info.group(0).substring(5,
							data.length() - 5));
				}
				find_info = find_port.matcher(peer);
				if (find_info.find()) {
					data = find_info.group(0);
					data_user.setPort(Integer.parseInt(data.substring(6,
							data.length() - 7)));
				}
				user.add(data_user);
			}
		} else
			return null;
		return user;
	}

	public static ArrayList<User> updateUserOnline(ArrayList<User> list_users, String mess) {
		Pattern alive = Pattern.compile(Tags.STATUS_OPEN_TAG + Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG);
		Pattern kill_user = Pattern.compile(Tags.USER_NAME_OPEN_TAG + "[^<>]*" + Tags.USER_NAME_CLOSE_TAG);
		if (request.matcher(mess).matches()) {
			Matcher find_state = alive.matcher(mess);
			if (find_state.find())
				return list_users;
			find_state = kill_user.matcher(mess);
			if (find_state.find()) {
				String find_user = find_state.group(0);
				int size = list_users.size();
				String name = find_user.substring(11, find_user.length() - 12);
				for (int i = 0; i < size; i++)
					if (name.equals(list_users.get(i).getName())) {
						list_users.remove(i);
						break;
					}
			}
		}
		return list_users;
	}

	public static String getMessage(String mess) {
//		System.out.print("Ham getMessage o decode.java duoc goi");
//		System.out.print(mess);
		if (message.matcher(mess).matches()) {
			int begin = Tags.CHAT_MSG_OPEN_TAG.length();
			int end = mess.length() - Tags.CHAT_MSG_CLOSE_TAG.length();
			System.out.println(begin + " "+ end);
			String message = mess.substring(begin, end);
			return message;
		}
		return null;
	}

	public static String getNameRequestChat(String mess) {
		Pattern check_request = Pattern.compile(Tags.CHAT_REQ_OPEN_TAG
				+ Tags.USER_NAME_OPEN_TAG + "[^<>]*" + Tags.USER_NAME_CLOSE_TAG
				+ Tags.CHAT_REQ_CLOSE_TAG);
		if (check_request.matcher(mess).matches()) {
			int lenght = mess.length();
			String name = mess.substring((Tags.CHAT_REQ_OPEN_TAG + Tags.USER_NAME_OPEN_TAG).length(),
							lenght - (Tags.USER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG).length());
			return name;
		}
		return null;
	}

	public static boolean checkFile(String name) {
		if (checkNameFile.matcher(name).matches())
			return true;
		return false;
	}

	public static boolean checkFeedBack(String mess) {
		if (feedBack.matcher(mess).matches())
			return true;
		return false;
	}
}

