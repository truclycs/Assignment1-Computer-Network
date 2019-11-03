package client;

import javax.swing.*;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.System.out;
import java.awt.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import tags.Encode;



public class ChatRoomGUI extends JFrame implements ActionListener {
	
	String nameUser = "", nameFile = "", srcImgGuest;
	ArrayList<String> nameGuest = new ArrayList<String>();
	String strGuest = "";
	JFrame frameChatRoomGUI;
	JTextField textName;
	JPanel panelMessage;
	JTextPane txtDisplayChat;
	Label textState, lblReceive;
	JButton btnDisConnect, btnSend, btnChoose, btnExit;
	boolean isStop = false, isSendFile = false, isReceiveFile = false;
	JProgressBar progressSendFile;
	JTextField txtMessage;
	JScrollPane scrollPane;
	private JTextField txtPath;
	
    PrintWriter pw;
    BufferedReader br;
    public static void main(String nameUser,InetAddress serverName, ArrayList<String> nameGuest) {
    	System.out.print("Nam Khung");
        try {
//            new ChatGroupClients(Username, ipserver);
        	
        	String sn = "localhost";
        	out.println(nameUser);
        	new ChatRoomGUI(nameUser, serverName, nameGuest);
        } catch(Exception ex) {
            out.println( "Error --> " + ex.getMessage());
        }
        
    } // end of main
    Socket socketChat;
    public ChatRoomGUI(String nameUser,InetAddress serverName, ArrayList<String> guest) throws Exception {
        super(nameUser);
        this.nameUser = nameUser;
        
        
        
        this.nameGuest = guest;
		for (int i = 0; i < guest.size() - 1; i++) {
			if(!guest.get(i).equals(nameUser))
				strGuest = strGuest + guest.get(i) + ", ";
		}
		strGuest = strGuest + guest.get(guest.size() - 1);
		try {
        	socketChat  = new Socket(serverName, 10007);
        }catch(Exception ex) {
            out.println( "ErrorChatRoomGUI --> " + ex.getMessage());
            ex.printStackTrace();
        }
        br = new BufferedReader( new InputStreamReader( socketChat.getInputStream()) ) ;
        pw = new PrintWriter(socketChat.getOutputStream(),true);
        pw.println(nameUser);  // send name to server
        System.out.println("hello");
        buildInterface();
        frameChatRoomGUI.setVisible(true);
        new MessagesThread().start();
    }
    

    public void buildInterface() {
    	frameChatRoomGUI = new JFrame();
    	frameChatRoomGUI.setTitle("Welcome: " + nameUser);
    	frameChatRoomGUI.setResizable(false);
    	frameChatRoomGUI.setBounds(200, 200, 673, 645);
    	frameChatRoomGUI.getContentPane().setLayout(null);
    	frameChatRoomGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	frameChatRoomGUI.setLocationRelativeTo(null);
    	
    	out.println("interface");
    	
    	JLabel lblClientIP =  new JLabel("");
    	lblClientIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    	lblClientIP.setBounds(30, 6, 41, 40);
//    	lblClientIP.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/girl.png")));
    	frameChatRoomGUI.getContentPane().add(lblClientIP);
    	
    	textName = new JTextField(nameUser);
		textName.setForeground(Color.RED);
		textName.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		textName.setEditable(false);
		textName.setBounds(70, 6, 448, 40);
		frameChatRoomGUI.getContentPane().add(textName);
		textName.setText(strGuest);
		textName.setColumns(10);
		
		panelMessage = new JPanel();
		panelMessage.setBounds(6, 363, 649, 201);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		frameChatRoomGUI.getContentPane().add(panelMessage);
		panelMessage.setLayout(null);

		txtMessage = new JTextField("");
		txtMessage.setBounds(10, 21, 479, 62);
		panelMessage.add(txtMessage);
		txtMessage.setColumns(10);
		
		out.println("end  - 1 - interface");
		
		btnSend = new JButton("");
		btnSend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnSend.setBounds(551, 33, 65, 39);
		btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSend.setContentAreaFilled(false);
		panelMessage.add(btnSend);
		btnSend.setIcon(new javax.swing.ImageIcon(ChatRoomGUI.class.getResource("/image/send.png")));
			
		out.println("end  - 1.1 - interface");
		
		
		btnChoose = new JButton("");
		btnChoose.setBounds(551, 152, 50, 36);
		panelMessage.add(btnChoose);
		btnChoose.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnChoose.setIcon(new javax.swing.ImageIcon(ChatGui.class.getResource("/image/attachment.png")));
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(frameChatRoomGUI);
				if (result == JFileChooser.APPROVE_OPTION) {
					isSendFile = true;
					String path_send = (fileChooser.getSelectedFile()
							.getAbsolutePath()) ;
					System.out.println(path_send);
					nameFile = fileChooser.getSelectedFile().getName();
					txtPath.setText(path_send);
				}
			}
		});
		btnChoose.setBorder(BorderFactory.createEmptyBorder());
		btnChoose.setContentAreaFilled(false);
		
		txtPath = new JTextField("");
		txtPath.setBounds(76, 163, 433, 25);
		panelMessage.add(txtPath);
		txtPath.setEditable(false);
		txtPath.setColumns(10);
				
				
		Label label = new Label("Path");
		label.setBounds(10, 166, 39, 22);
		panelMessage.add(label);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		
		
		
		out.println("end  - 1.2 - interface");
		
		//action when press button Send
		btnSend.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				if(isSendFile) {
					sendMessage(nameFile);
					updateChat_notify("sendfile complete");
				}
				String msg = txtMessage.getText();
				if (msg.equals(""))
					return;
				try {
					updateChat_send(msg);
					txtMessage.setText("");
					sendMessage(msg);
				} catch (Exception e) {
			        System.out.println("hello141");
					e.printStackTrace();
				}
			}
		});
		
		out.println("end  - 2 - interface");

		txtMessage.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = txtMessage.getText().toString();
					if (isStop) {
						updateChat_send(txtMessage.getText().toString());
						txtMessage.setText("");
						return;
					}
					if (msg.equals("")) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
						return;
					}
					try {
						updateChat_send(msg);
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
						sendMessage(msg);
					} catch (Exception e) {
						txtMessage.setText("");
						txtMessage.setCaretPosition(0);
					}
				}
			}
		});

		btnDisConnect = new JButton("LEAVE CHAT");
		btnDisConnect.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnDisConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					isStop = true;
					pw.println("end");  // send end to server so that server knows about the termination
					frameChatRoomGUI.dispose();
					System.exit(1);
//					System.gc();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		out.println("end  - 3 - interface");
		
		btnDisConnect.setBounds(540, 6, 113, 40);
		btnDisConnect.addActionListener(this);
		frameChatRoomGUI.getContentPane().add(btnDisConnect);
		textState = new Label("");
		textState.setBounds(6, 570, 158, 22);
		textState.setVisible(false);
		frameChatRoomGUI.getContentPane().add(textState);

		lblReceive = new Label("Receiving ...");
		lblReceive.setBounds(564, 577, 83, 14);
		lblReceive.setVisible(false);
		frameChatRoomGUI.getContentPane().add(lblReceive);
		
		txtDisplayChat = new JTextPane();
		txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDisplayChat.setEditable(false);
		txtDisplayChat.setContentType( "text/html" );
		txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
		txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		txtDisplayChat.setBounds(6, 59, 670, 291);
		appendToPane(txtDisplayChat, "<div class='clear' style='background-color:white'></div>"); //set default background
			    
		frameChatRoomGUI.getContentPane().add(txtDisplayChat);
	
		scrollPane = new JScrollPane(txtDisplayChat);
		scrollPane.setBounds(6, 59, 649, 291);
		frameChatRoomGUI.getContentPane().add(scrollPane);
		
		out.println("end - - interface");
    }
    
    
    
    public void sendMessage(String msg) {
    	pw.println(msg);
    }
    
    public void updateChat_receive(String msg) {
		appendToPane(txtDisplayChat, "<div class='left' style='width: 40%; background-color: #f1f0f0; '>"+ msg +"</div>");
	}

	public void updateChat_send(String msg) {
		appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%;'></td>"
				+ "<td style='width: 40%;  background-color: #0084ff; '>" + msg
				+"</td> </tr>"
				+ "</table>");
	}
	
	public void updateChat_notify(String msg) {
		appendToPane(txtDisplayChat, "<table class='bang' style='color: white; clear:both; width: 100%;'>"
				+ "<tr align='right'>"
				+ "<td style='width: 59%; '></td>"
				+ "<td style='width: 40%; background-color: #f1c40f;'>" + msg 
				+"</td> </tr>"
				+ "</table>");
	}

	private void appendToPane(JTextPane tp, String msg){
	    HTMLDocument doc = (HTMLDocument)tp.getDocument();
	    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
	    try {
//		    tp.insertIcon(new javax.swing.ImageIcon(ChatGui.class.getResource(srcImgGuest)));
	    	editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
	    	tp.setCaretPosition(doc.getLength());
	      
	    } 
	    catch(Exception e){
	    	e.printStackTrace();
	    }    
	}
    
    
    
    // inner class for Messages Thread
    class  MessagesThread extends Thread {
        public void run() {
            String line = "";
            try {
                while(true) {
                    line = br.readLine();
                    updateChat_receive(line + "\n");
                } // end of while
            } catch(Exception ex) {
            	ex.printStackTrace();
            }
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		pw.println(txtMessage.getText());
		if ( e.getSource() == btnDisConnect ) {
            pw.println("end");  // send end to server so that server knows about the termination
            System.exit(0);
        } else {
            // send message to server
            pw.println(txtMessage.getText());
        }
	}
	
}
