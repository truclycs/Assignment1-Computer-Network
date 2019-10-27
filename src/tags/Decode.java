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

	public static ArrayList<String> getUser(String msg) {
		ArrayList<String> user = new ArrayList<String>();
		if (createAccount.matcher(msg).matches()) {
			Pattern findName = Pattern.compile(Tags.USER_NAME_OPEN_TAG + ".*"
					+ Tags.USER_NAME_CLOSE_TAG);
			Pattern findPort = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
					+ Tags.PORT_CLOSE_TAG);
			Matcher find = findName.matcher(msg);
			if (find.find()) {
				String name = find.group(0);
				user.add(name.substring(11, name.length() - 12));
				find = findPort.matcher(msg);
				if (find.find()) {
					String port = find.group(0);
					user.add(port.substring(6, port.length() - 7));
				} else
					return null;
			} else
				return null;
		} else
			return null;
		return user;
	}

	public static ArrayList<User> getAllUser(String msg) {
		ArrayList<User> user = new ArrayList<User>();
		Pattern findUser = Pattern.compile(Tags.USER_OPEN_TAG
				+ Tags.USER_NAME_OPEN_TAG + "[^<>]*" + Tags.USER_NAME_CLOSE_TAG
				+ Tags.IP_OPEN_TAG + "[^<>]*" + Tags.IP_CLOSE_TAG
				+ Tags.PORT_OPEN_TAG + "[0-9]*" + Tags.PORT_CLOSE_TAG
				+ Tags.USER_CLOSE_TAG);
		Pattern findName = Pattern.compile(Tags.USER_NAME_OPEN_TAG + ".*"
				+ Tags.USER_NAME_CLOSE_TAG);
		Pattern findPort = Pattern.compile(Tags.PORT_OPEN_TAG + "[0-9]*"
				+ Tags.PORT_CLOSE_TAG);
		Pattern findIP = Pattern.compile(Tags.IP_OPEN_TAG + ".+"
				+ Tags.IP_CLOSE_TAG);
		if (users.matcher(msg).matches()) {
			Matcher find = findUser.matcher(msg);
			while (find.find()) {
				String peer = find.group(0);
				String data = "";
				User dataUser = new User();
				Matcher findInfo = findName.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataUser.setName(data.substring(11, data.length() - 12));
				}
				findInfo = findIP.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataUser.setHost(findInfo.group(0).substring(5,
							data.length() - 5));
				}
				findInfo = findPort.matcher(peer);
				if (findInfo.find()) {
					data = findInfo.group(0);
					dataUser.setPort(Integer.parseInt(data.substring(6,
							data.length() - 7)));
				}
				user.add(dataUser);
			}
		} else
			return null;
		return user;
	}

	public static ArrayList<User> updateUserOnline(
			ArrayList<User> peerList, String msg) {
		Pattern alive = Pattern.compile(Tags.STATUS_OPEN_TAG
				+ Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG);
		Pattern killUser = Pattern.compile(Tags.USER_NAME_OPEN_TAG + "[^<>]*"
				+ Tags.USER_NAME_CLOSE_TAG);
		if (request.matcher(msg).matches()) {
			Matcher findState = alive.matcher(msg);
			if (findState.find())
				return peerList;
			findState = killUser.matcher(msg);
			if (findState.find()) {
				String findUser = findState.group(0);
				int size = peerList.size();
				String name = findUser.substring(11, findUser.length() - 12);
				for (int i = 0; i < size; i++)
					if (name.equals(peerList.get(i).getName())) {
						peerList.remove(i);
						break;
					}
			}
		}
		return peerList;
	}

	public static String getMessage(String msg) {
//		System.out.print("Ham getMessage o decode.java duoc goi");
//		System.out.print(msg);
		if (message.matcher(msg).matches()) {
			int begin = Tags.CHAT_MSG_OPEN_TAG.length();
			int end = msg.length() - Tags.CHAT_MSG_CLOSE_TAG.length();
			System.out.println(begin + " "+ end);
			String message = msg.substring(begin, end);
			return message;
		}
		return null;
	}

	public static String getNameRequestChat(String msg) {
		Pattern checkRequest = Pattern.compile(Tags.CHAT_REQ_OPEN_TAG
				+ Tags.USER_NAME_OPEN_TAG + "[^<>]*" + Tags.USER_NAME_CLOSE_TAG
				+ Tags.CHAT_REQ_CLOSE_TAG);
		if (checkRequest.matcher(msg).matches()) {
			int lenght = msg.length();
			String name = msg
					.substring(
							(Tags.CHAT_REQ_OPEN_TAG + Tags.USER_NAME_OPEN_TAG)
									.length(),
							lenght
									- (Tags.USER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG)
											.length());
			return name;
		}
		return null;
	}

	public static boolean checkFile(String name) {
		if (checkNameFile.matcher(name).matches())
			return true;
		return false;
	}

	public static boolean checkFeedBack(String msg) {
		if (feedBack.matcher(msg).matches())
			return true;
		return false;
	}
}

