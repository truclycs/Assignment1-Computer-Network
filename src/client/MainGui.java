package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import data.User;
import login.Login;

import javax.swing.JLabel;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import tags.Tags;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import java.awt.Color;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainGui {

	private Client clientNode;
	private static String IPClient = "", nameUser = "", dataUser = "", nameGroup = "";
	private static int portClient = 0;
	private JFrame frameMainGui;
	private JTextField txtNameFriend, txtNameGroup;
	private JButton btnChat, btnLogout, btnChatRoom, btnAdd, btnClear;
	private JLabel lblLogo;
	private JLabel lblActiveNow;
	private static JList<String> listActive;
	private static ArrayList<User> clients = new ArrayList<User>();
	private ArrayList<String> nameMember = new ArrayList<String>();
	
	static DefaultListModel<String> model = new DefaultListModel<>();
	private JLabel lblUsername;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frameMainGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainGui(String arg, int arg1, String name, String msg) throws Exception {
		IPClient = arg;
		portClient = arg1;
		nameUser = name;
		dataUser = msg;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frameMainGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainGui() throws Exception {
		initialize();
		clientNode = new Client(IPClient, portClient, nameUser, dataUser);
	}

	public static void updateFriendMainGui(String msg) {
		model.addElement(msg);
	}

	public static void resetList() {
		model.clear();
	} 
	
	private void initialize() {
		frameMainGui = new JFrame();
		frameMainGui.setTitle("Menu Chat");
		frameMainGui.setResizable(false);
		frameMainGui.setBounds(100, 100, 560, 635);
//		frameMainGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMainGui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameMainGui.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		frameMainGui.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("About");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmMe = new JMenuItem("Author");
		mnNewMenu.add(mntmMe);
		mntmMe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(mntmMe, "Nguyen Dang Ha Nam: 1710195\nNguyen Thi Truc Ly:      1710187\nTran Ho Minh Thong:     1710314", "About Us", 1);
			}
		});
		
		JMenuItem mntmVersion = new JMenuItem("Version");
		mnNewMenu.add(mntmVersion);
		mntmVersion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(mntmMe, "version 1.1 update 4/11/2019", "Version", 1);
				}
		});
		
		JLabel lblHello = new JLabel("Welcome");
		lblHello.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblHello.setBounds(12, 82, 70, 16);
		frameMainGui.getContentPane().add(lblHello);


		JLabel lblFriendsName = new JLabel("Name Friend: ");
		lblFriendsName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblFriendsName.setBounds(12, 425, 110, 16);
		frameMainGui.getContentPane().add(lblFriendsName);
		
		JLabel lblGroupName = new JLabel("Group Member: ");
		lblGroupName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblGroupName.setBounds(12, 460, 110, 16);
		frameMainGui.getContentPane().add(lblGroupName);
		
		txtNameGroup = new JTextField("");
		txtNameGroup.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtNameGroup.setColumns(10);
		txtNameGroup.setBounds(115, 455, 380, 28);
		txtNameGroup.setEditable(false);
		frameMainGui.getContentPane().add(txtNameGroup);
		
		txtNameFriend = new JTextField("");
		txtNameFriend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtNameFriend.setColumns(10);
		txtNameFriend.setBounds(115, 419, 416, 28);
		frameMainGui.getContentPane().add(txtNameFriend);

		btnChat = new JButton("Chat");
		btnChat.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		btnChat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String name = txtNameFriend.getText();
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Invaild username", false);
					return;
				}
				if (name.equals(nameUser)) {
					Tags.show(frameMainGui, "This software doesn't support chat yourself function", false);
					return;
				}
				int size = Client.clientarray.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Client.clientarray.get(i).getName())) {
						try {
							clientNode.intialNewChat(Client.clientarray.get(i).getHost(),Client.clientarray.get(i).getPort(), name);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			}
		});
		btnChat.setBounds(20, 505, 129, 44);
		frameMainGui.getContentPane().add(btnChat);
		btnChat.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/chatting.png")));
		
		
		btnClear = new JButton();
		btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnClear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				clients.clear();
				nameMember.clear();
				nameGroup = "";
				txtNameGroup.setText(nameGroup);
				return;
			}
		});
		
		btnClear.setBounds(500, 455, 30, 28);
		frameMainGui.getContentPane().add(btnClear);
		btnClear.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/remove.png")));

		// update 1/11/2019
		//
		///
		///
		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		
		btnAdd.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String name = txtNameFriend.getText();
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Invaild username", false);
					return;
				}
				if (name.equals(nameUser)) {
					Tags.show(frameMainGui, "This software doesn't support chat yourself function", false);
					return;
				}
				int size = Client.clientarray.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Client.clientarray.get(i).getName())) {
						try {
							if(!nameMember.contains(name)) {
								nameMember.add(name);
								User newPeer = new User();
								newPeer.setPeer(name, Client.clientarray.get(i).getHost(),Client.clientarray.get(i).getPort());
								clients.add(newPeer);
								nameGroup = nameGroup + " " + name;
								txtNameGroup.setText(nameGroup);
							}
							
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			}
		});
		
		btnAdd.setBounds(305, 505, 80, 44);
		frameMainGui.getContentPane().add(btnAdd);
		btnAdd.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/adduser.png")));
		
		btnChatRoom = new JButton("Group");
		btnChatRoom.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		btnChatRoom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String name = txtNameGroup.getText();
				if (name.equals("") || Client.clientarray == null) {
					Tags.show(frameMainGui, "Group invaild username", false);
					return;
				}
				if (name.equals(nameUser)) {
					Tags.show(frameMainGui, "This software doesn't support chat yourself function", false);
					return;
				}
				User newPeer = new User();
				newPeer.setPeer(nameUser, IPClient, portClient);
				clients.add(newPeer);
				try {
					nameGroup = "";
					txtNameGroup.setText(nameGroup);
					nameMember.clear();
					clientNode.intialNewChatRoom(clients);
					clients = new ArrayList<User>();
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
				Tags.show(frameMainGui, "Friend is not found. Please wait to update your list friend", false);
			}
		});
		btnChatRoom.setBounds(160, 505, 129, 44);
		frameMainGui.getContentPane().add(btnChatRoom);
		btnChatRoom.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/teamwork.png")));
		
		
		
		btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = Tags.show(frameMainGui, "Are you sure want to logout ?", true);
				if (result == 0) {
					try {
						Login.main(null);
						clientNode.exit();
						frameMainGui.dispose();
					} catch (Exception e) {
						frameMainGui.dispose();
					}
				}
			}
		});
		btnLogout.setBounds(403, 505, 129, 44);
		btnLogout.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/logout.png")));
		frameMainGui.getContentPane().add(btnLogout);
		
		lblLogo = new JLabel("CHAT APPLICATION");
		lblLogo.setForeground(new Color(0, 0, 205));
		lblLogo.setIcon(new javax.swing.ImageIcon(MainGui.class.getResource("/image/customer.png")));
		lblLogo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLogo.setBounds(175, 13, 413, 60);
		frameMainGui.getContentPane().add(lblLogo);
		
		lblActiveNow = new JLabel("List Account Active Now");
		lblActiveNow.setForeground(new Color(100, 149, 237));
		lblActiveNow.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblActiveNow.setBounds(10, 123, 156, 16);
		frameMainGui.getContentPane().add(lblActiveNow);
		
		listActive = new JList<>(model);
		listActive.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		listActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String value = (String)listActive.getModel().getElementAt(listActive.locationToIndex(arg0.getPoint()));
				txtNameFriend.setText(value);
			}
		});
		listActive.setBounds(12, 152, 518, 251);
		frameMainGui.getContentPane().add(listActive);
		
		lblUsername = new JLabel(nameUser);
		lblUsername.setForeground(Color.RED);
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblUsername.setBounds(75, 76, 156, 28);
		frameMainGui.getContentPane().add(lblUsername);
	}

	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		return Tags.show(frameMessage, msg, type);
	}
}
