package tags;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Encode {
	private static Pattern check_mess = Pattern.compile("[^<>]*[<>]");
	public static String getCreateAccount(String name, String port) {
		return Tags.SESSION_OPEN_TAG + Tags.USER_NAME_OPEN_TAG + name 
				+ Tags.USER_NAME_CLOSE_TAG + Tags.PORT_OPEN_TAG + port
				+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG;
	}

	public static String sendRequest(String name) {
		return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.USER_NAME_OPEN_TAG
				+ name + Tags.USER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
				+ Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG
				+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
	}

	public static String sendMessage(String message) {
//		System.out.println("(encode)Dau vao message: " + message);
		Matcher find_mess = check_mess.matcher(message);
		String result = "";
		while (find_mess.find()) {
			String sub_mess = find_mess.group(0);
			System.out.println("sub_mess: " + sub_mess);
			int begin = sub_mess.length();			//do dai chuoi con
			char next_char = message.charAt(sub_mess.length() - 1); //ky tu cuoi cung cua message
			System.out.println("next_char: " + next_char);
			result += sub_mess; // + next_char
			sub_mess = message.substring(begin, message.length());
			message = sub_mess;
			find_mess = check_mess.matcher(message);
		}
		result += message;
//		System.out.println("(encode)Dau ra message: " + Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG);
		return Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG;
	}

	public static String sendRequestChat(String name) {
		return Tags.CHAT_REQ_OPEN_TAG + Tags.USER_NAME_OPEN_TAG + name
				+ Tags.USER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG;
	}

	public static String sendFile(String name) {
		return Tags.FILE_REQ_OPEN_TAG + name + Tags.FILE_REQ_CLOSE_TAG;
	}

	public static String exit(String name) {
		return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.USER_NAME_OPEN_TAG
				+ name + Tags.USER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
				+ Tags.SERVER_OFFLINE + Tags.STATUS_CLOSE_TAG
				+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
	}
}

