package server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.TextArea;
import java.awt.Font;

import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import tags.Tags;
import client.*;

public class ServerGui {
	public static int port = 9600;
	private JFrame frmServer;
	private JTextField txtIP, txtPort;
	private JLabel lblStatus;
	private static TextArea txtMessage;
	public static JLabel lblUserOnline;
	Server server;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGui window = new ServerGui();
					window.frmServer.setVisible(true);
					window.frmServer.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerGui() {
		initialize();
	}
	
	public static String getLabelUserOnline() {
		return lblUserOnline.getText();
	}

	public static void updateMessage(String msg) {
		txtMessage.append(msg + "\n");
	}

	public static void updateNumberClient() {
		int number = Integer.parseInt(lblUserOnline.getText());
		lblUserOnline.setText(Integer.toString(number + 1));
	}
	
	public static void decreaseNumberClient() {
		int number = Integer.parseInt(lblUserOnline.getText());
		lblUserOnline.setText(Integer.toString(number - 1));

	}

	private void initialize() {
		frmServer = new JFrame();
		frmServer.setForeground(UIManager.getColor("RadioButtonMenuItem.foreground"));
		frmServer.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 13));
		frmServer.getContentPane().setForeground(UIManager.getColor("RadioButtonMenuItem.acceleratorSelectionForeground"));
		frmServer.setTitle("Server Mangement");
		frmServer.setResizable(false);
		frmServer.setBounds(200, 200, 370, 315);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServer.getContentPane().setLayout(null);
		frmServer.setBackground(Color.ORANGE);

		JLabel lblIP = new JLabel("IP");
		lblIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblIP.setBounds(46, 120, 15, 16);					////// Vi tri lbl IP
		frmServer.getContentPane().add(lblIP);

		txtIP = new JTextField();
		txtIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtIP.setEditable(false);
		txtIP.setBounds(60, 114, 105, 28);				////// Vi tri text Ip
		frmServer.getContentPane().add(txtIP);
		txtIP.setColumns(10);
		
//		try {
//			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
//			
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		
//		Cach de lay dia chi ip thuong xuyen sai
		DatagramSocket socket;
		try {
			socket = new DatagramSocket();
			try {
				socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String ip = socket.getLocalAddress().getHostAddress();
			txtIP.setText(ip);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		

		JLabel lbl_new_label = new JLabel("PORT");
		lbl_new_label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lbl_new_label.setBounds(200, 120, 35, 16);			////// Vi tri lbl Port
		frmServer.getContentPane().add(lbl_new_label);

		txtPort = new JTextField();
		txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setBounds(240, 114, 34, 28);
		frmServer.getContentPane().add(txtPort);			///// Vi tri cua text Port
		txtPort.setText(Integer.toString(port));

		JButton btn_start = new JButton("START");
		btn_start.setBackground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));
		btn_start.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btn_start.setEnabled(true);
		btn_start.setBounds(46, 200, 100, 35);			/////// Vi tri button START
		frmServer.getContentPane().add(btn_start);
		btn_start.setIcon(new javax.swing.ImageIcon(ServerGui.class.getResource("/image/power.png")));
		
//		BufferedImage img = null;
//		try {
//		    img = ImageIO.read(new File(ServerGui.class.getResource("/image/serverManager.png").getFile()));
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//		Image dimg = img.getScaledInstance(64, 64,
//		        Image.SCALE_SMOOTH);
//		ImageIcon imageIcon = new ImageIcon(dimg);

		
		JLabel lbl_group = new JLabel("SERVER");
		lbl_group.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		lbl_group.setForeground(Color.getHSBColor(24, 49, 24));
		lbl_group.setBounds(105, 13, 408, 76);
		lbl_group.setIcon(new javax.swing.ImageIcon(ServerGui.class.getResource("/image/group.png")));
		frmServer.getContentPane().add(lbl_group);

		txtMessage = new TextArea();					
		txtMessage.setBackground(Color.BLACK);
		txtMessage.setForeground(Color.GREEN);
		txtMessage.setFont(new Font("Consolas", Font.PLAIN, 14));
		txtMessage.setEditable(false);
		txtMessage.setBounds(0, 300, 714, 358);		////// Vi tri textArea
		frmServer.getContentPane().add(txtMessage);

		JButton btnStop = new JButton("STOP");
		btnStop.setEnabled(false);
		btnStop.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblUserOnline.setText("0");
				try {
					btn_start.setEnabled(true);
					btnStop.setEnabled(false);
					server.stopserver();
					ServerGui.updateMessage("STOP SERVER");
					lblStatus.setText("<html><font color='red'>OFF</font></html>");
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					ServerGui.updateMessage("STOP SERVER");
					lblStatus.setText("<html><font color='red'>OFF</font></html>");
				}
			}
		});
		btnStop.setBounds(200, 200, 100, 35);						//// Vi tri button Stop
		frmServer.getContentPane().add(btnStop);
		btnStop.setIcon(new javax.swing.ImageIcon(ServerGui.class.getResource("/image/stop16.png")));
		
		JLabel lblnew111 = new JLabel("STATUS");
		lblnew111.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblnew111.setBounds(46, 168, 50, 16);
		frmServer.getContentPane().add(lblnew111);
		
		lblStatus = new JLabel("New label");
		lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblStatus.setBounds(100, 168, 100, 16);


		frmServer.getContentPane().add(lblStatus);
		lblStatus.setText("<html><font color='red'>OFF</font></html>");
		
//		JLabel lblRecord = new JLabel("LOG");
//		lblRecord.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//		lblRecord.setBounds(26, 245, 89, 16);
//		frmServer.getContentPane().add(lblRecord);
		
		JLabel lbllabelUserOnline = new JLabel("USER ONLINE");
		lbllabelUserOnline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lbllabelUserOnline.setBounds(200, 168, 89, 16);
		frmServer.getContentPane().add(lbllabelUserOnline);
		
		lblUserOnline = new JLabel("0");
		lblUserOnline.setForeground(Color.BLUE);
		lblUserOnline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblUserOnline.setBounds(295, 168, 56, 16);
		frmServer.getContentPane().add(lblUserOnline);
		
		JMenuBar menuBar = new JMenuBar();
		frmServer.setJMenuBar(menuBar);
		
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
		btn_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server = new Server(port);
					ServerGui.updateMessage("START SERVER");
					lblStatus.setText("<html><font color='green'>RUNNING</font></html>");
					btn_start.setEnabled(false);
					btnStop.setEnabled(true);
				} catch (Exception e) {
					ServerGui.updateMessage("START ERROR");
					e.printStackTrace();
				}
			}
		});
	}
}

