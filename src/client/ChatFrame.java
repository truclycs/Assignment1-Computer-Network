package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import data.BackgroundPanel;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.TextField;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ModuleLayer.Controller;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.ScrollPaneConstants;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.CardLayout;
import javax.swing.SwingConstants;

public class ChatFrame extends JFrame {
	private static JPanel contentPane;
	static JFrame messageFrame=new JFrame("alert");

	//
	private static TreeMap<String, JPanel> FriendPanel = new TreeMap<>();// Dictonary of Panels displaying name
	private static TreeMap<String, JTextArea> FriendNameTextArea = new TreeMap<>();// Dictonary of TextArea displaying
																					// name
	private static TreeMap<String, JPanel> FriendMessagePanel = new TreeMap<>();// Dictionary of Panels displaying mess
	static JPanel yourFriendPanel = new JPanel();// Contain names
	//
	private JTextField txtWritename; // enter a name for searching
	//

	static String focusFriendName;

	static JPanel messagePanel;
	static JScrollPane MessageScrollPane;
	static Controller controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFrame frame = new ChatFrame();
					frame.createFriend("aa");
					frame.createFriend("bb");
					frame.createFriend("cc");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatFrame() {
		
		setTitle("App");
		setSize(getMaximumSize());
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				revalidate();
				repaint();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
//		menuBar.setOpaque(true);

		menuBar.setBackground(new Color(100, 150, 0, 150));
		String myname = "NAmememmmemem";
		JMenu mnYourname = new JMenu(myname);
		mnYourname.setHorizontalAlignment(SwingConstants.CENTER);
		mnYourname.setFont(new Font("Serif", Font.PLAIN, 40));
//		mnYourname.setOpaque(true);
		mnYourname.setBackground(new Color(0, 255, 0, 120));
		menuBar.add(mnYourname);
		menuBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		JPanel LogoutPanel = new JPanel();
		mnYourname.add(LogoutPanel);
		LogoutPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnLogout = new JButton("Logout");
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//controller.chatFrameToFirst();
			}
		});
		LogoutPanel.add(btnLogout);

		JPanel ChangePasswordPanel = new JPanel();
		mnYourname.add(ChangePasswordPanel);
		ChangePasswordPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnChangePassword = new JButton("Change Password");
		ChangePasswordPanel.add(btnChangePassword);
		contentPane = new BackgroundPanel("C:\\Users\\DELL\\Desktop\\Assignment-1-Computer-Network\\src\\image\\background.png");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		Color panel_1_color = new Color(51, 153, 255, 0);

		messagePanel = new JPanel();
		messagePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		messagePanel.setOpaque(true);
		messagePanel.setBackground(new Color(0, 0, 0, 0));

		MessageScrollPane = new JScrollPane(messagePanel);
		MessageScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		CardLayout card = new CardLayout(0, 0);
		messagePanel.setLayout(card);

		MessageScrollPane.setOpaque(false);
		MessageScrollPane.setBackground(new Color(0, 0, 0, 0));
		contentPane.revalidate();
		contentPane.repaint();

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 5;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		txtWritename = new JTextField();
		txtWritename.setText("Find your friend");
		txtWritename.setFont(new Font("Serif", Font.PLAIN, 20));
		txtWritename.setOpaque(true);
		contentPane.revalidate();
		contentPane.repaint();
		panel.add(txtWritename);
		txtWritename.setColumns(10);

		yourFriendPanel.setOpaque(true);
		yourFriendPanel.setBackground(new Color(0, 255, 150, 90));

		JScrollPane yourFriendScrollPane = new JScrollPane(yourFriendPanel);
		yourFriendPanel.setLayout(new BoxLayout(yourFriendPanel, BoxLayout.Y_AXIS));

		yourFriendScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				yourFriendScrollPane.revalidate();
				yourFriendScrollPane.repaint();
			}
		});
		yourFriendScrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				yourFriendScrollPane.revalidate();
				yourFriendScrollPane.repaint();
			}
		});

		contentPane.revalidate();
		contentPane.repaint();

		GridBagConstraints gbc_YourFriendScrollPane = new GridBagConstraints();
		gbc_YourFriendScrollPane.gridwidth = 5;
		gbc_YourFriendScrollPane.gridheight = 10;
		gbc_YourFriendScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_YourFriendScrollPane.fill = GridBagConstraints.BOTH;
		gbc_YourFriendScrollPane.gridx = 0;
		gbc_YourFriendScrollPane.gridy = 1;
		contentPane.add(yourFriendScrollPane, gbc_YourFriendScrollPane);

		yourFriendScrollPane.setOpaque(false);

		GridBagConstraints gbc_MessageScrollPane = new GridBagConstraints();
		gbc_MessageScrollPane.gridwidth = 15;
		gbc_MessageScrollPane.gridheight = 10;
		gbc_MessageScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_MessageScrollPane.fill = GridBagConstraints.BOTH;
		gbc_MessageScrollPane.gridx = 5;
		gbc_MessageScrollPane.gridy = 0;
		contentPane.add(MessageScrollPane, gbc_MessageScrollPane);

		MessageScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				MessageScrollPane.revalidate();
				MessageScrollPane.repaint();
			}
		});

		JPanel panel_2 = new JPanel();
		panel_2.setOpaque(true);
		panel_2.setBackground(new Color(0, 255, 150, 90));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.gridwidth = 15;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 5;
		gbc_panel_2.gridy = 10;
		contentPane.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Serif", Font.PLAIN, 40));
		panel_2.add(textArea);
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel_2.add(scrollPane);

		JButton btnSend = new JButton("Send");
		btnSend.setFont(new Font("Serif", Font.PLAIN, 40));
		btnSend.setForeground(Color.WHITE);
		btnSend.setOpaque(true);
		btnSend.setBackground(new Color(0, 51, 255));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String tf = textArea.getText();
				if (!tf.equals("")) {
					textArea.setText("");
					createMessFromText(focusFriendName, tf, false);
					MessageScrollPane.getVerticalScrollBar()
							.setValue(MessageScrollPane.getVerticalScrollBar().getMaximum());
					MessageScrollPane.revalidate();
					MessageScrollPane.repaint();
					//Some code about sending message here
					String nameOfReceiver=focusFriendName;
					String message=tf;
					
					
					//
				}
			}
		});

		JButton btnFile = new JButton("File");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnFile.setFont(new Font("Serif", Font.PLAIN, 40));
		btnFile.setSize(100, 50);
		btnFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					createMessFromFile(focusFriendName, selectedFile, false);
					// them code gui file qua server
					
					
					
					
					
					
					//
					MessageScrollPane.getVerticalScrollBar()
							.setValue(MessageScrollPane.getVerticalScrollBar().getMaximum());
					MessageScrollPane.revalidate();
					MessageScrollPane.repaint();
				}

			}
		});
		btnFile.setForeground(new Color(255, 255, 255));
		btnFile.setBackground(new Color(0, 51, 255));
		panel_2.add(btnFile);
		panel_2.add(btnSend);
		/*
		 * for (Map.Entry<String, String> entry :
		 * controller.connection.FriendIP.entrySet()) { String key = entry.getKey();
		 * if(!Controller.myname.equals(key)) createFriend(key,true); }
		 * contentPane.revalidate(); contentPane.repaint();
		 */

	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public static void createMessFromText(String name, String txt, boolean align) {
		JPanel messagePanel=FriendMessagePanel.get(name);
		if(messagePanel==null) return;
		JPanel newMessPanel = new JPanel();
		newMessPanel.setOpaque(true);
		newMessPanel.setBackground(new Color(0, 0, 0, 0));
		FlowLayout flowLayout = (FlowLayout) newMessPanel.getLayout();
		flowLayout.setAlignment((align) ? FlowLayout.LEFT : FlowLayout.RIGHT);
		messagePanel.add(newMessPanel);

		JTextArea newMess = new JTextArea();
		newMess.setLineWrap(true);
		newMess.setSize(400, 400);
		newMess.setEditable(false);
		newMess.setWrapStyleWord(true);
		newMess.setOpaque(true);
		newMess.setBackground((!align) ? (new Color(0, 0, 255, 220)) : (new Color(255, 0, 0, 220)));
		newMess.setForeground(new Color(255, 255, 255, 255));

		newMess.setFont(new Font("Serif", Font.PLAIN, 40));
		newMess.setText(txt);

		newMessPanel.add(newMess);
	}

	public static void createMessFromFile(String name, File file, boolean align) {
		JPanel messagePanel=FriendMessagePanel.get(name);
		if(messagePanel==null) return;
		StyleContext context = new StyleContext();
		StyledDocument document = new DefaultStyledDocument(context);

		Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setUnderline(style, true);

		try {
			document.insertString(document.getLength(), file.getName(), style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JPanel newMessPanel = new JPanel();
		newMessPanel.setOpaque(true);
		newMessPanel.setBackground(new Color(0, 0, 0, 0));
		FlowLayout flowLayout = (FlowLayout) newMessPanel.getLayout();
		flowLayout.setAlignment((align) ? FlowLayout.LEFT : FlowLayout.RIGHT);
		messagePanel.add(newMessPanel);

		JTextPane newMess = new JTextPane(document);

		newMess.setPreferredSize(new Dimension(400, 50));
		newMess.setEditable(false);
		newMess.setOpaque(true);
		newMess.setBackground((!align) ? (new Color(0, 0, 255, 220)) : (new Color(255, 0, 0, 220)));
		newMess.setForeground(new Color(255, 255, 255, 255));

		newMess.setFont(new Font("Serif", Font.PLAIN, 40));

		newMessPanel.add(newMess);

		
		
	}

	public static void showAlert(String Mess) {
		JOptionPane.showMessageDialog(messageFrame, "thank you for using java");
	}
	
	public static void createFriend(String name) {

		if (FriendPanel.containsKey(name))
			return;
		JPanel newFriendNamePanel = new JPanel();
		newFriendNamePanel.setOpaque(true);
		newFriendNamePanel.setBackground(new Color(0, 0, 0, 0));
		yourFriendPanel.add(newFriendNamePanel);
		FriendPanel.put(name, newFriendNamePanel);

		JTextArea newFriendNameTextArea = new JTextArea();
		newFriendNameTextArea.setSize(200, 100);
		newFriendNameTextArea.setLineWrap(true);
		newFriendNameTextArea.setEditable(false);
		newFriendNameTextArea.setWrapStyleWord(true);
		newFriendNameTextArea.setOpaque(true);
		newFriendNameTextArea.setBackground(new Color(150, 150, 0, 220));
		newFriendNameTextArea.setForeground(new Color(255, 255, 255, 255));
		FriendNameTextArea.put(name, newFriendNameTextArea);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		panel.setOpaque(true);
		panel.setBackground(new Color(0, 0, 0, 0));
		messagePanel.add(panel, name);
		FriendMessagePanel.put(name, panel);

		if (FriendPanel.size() == 1) {
			focusFriendName = name;
			FriendPanel.get(name).setBackground(new Color(0, 255, 0, 150));
			FriendNameTextArea.get(name).setBackground(new Color(0, 255, 0, 220));
			;
			((CardLayout) messagePanel.getLayout()).show(messagePanel, name);

		}
		contentPane.revalidate();
		contentPane.repaint();
		newFriendNamePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (FriendPanel.size() > 1) {
					FriendPanel.get(focusFriendName).setBackground(new Color(0, 0, 0, 0));
					FriendNameTextArea.get(focusFriendName).setBackground(new Color(150, 150, 0, 220));
					focusFriendName = name;

					FriendPanel.get(focusFriendName).setBackground(new Color(0, 255, 0, 150));
					FriendNameTextArea.get(focusFriendName).setBackground(new Color(0, 255, 0, 220));
					((CardLayout) messagePanel.getLayout()).show(messagePanel, name);
					contentPane.revalidate();
					contentPane.repaint();
				}
			}
		});

		newFriendNameTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (FriendPanel.size() > 1) {
					FriendPanel.get(focusFriendName).setBackground(new Color(0, 0, 0, 0));
					FriendNameTextArea.get(focusFriendName).setBackground(new Color(150, 150, 0, 220));
					focusFriendName = name;

					FriendPanel.get(focusFriendName).setBackground(new Color(0, 255, 0, 150));
					FriendNameTextArea.get(focusFriendName).setBackground(new Color(0, 255, 0, 220));
					((CardLayout) messagePanel.getLayout()).show(messagePanel, name);
					contentPane.revalidate();
					contentPane.repaint();
				}
			}
		});

		newFriendNameTextArea.setFont(new Font("Serif", Font.PLAIN, 40));
		newFriendNameTextArea.setText(name);

		newFriendNamePanel.add(newFriendNameTextArea);
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	static public void removeFriend(String name) {
		showAlert(name+" has logout!");
		yourFriendPanel.remove(FriendPanel.get(name));
		FriendPanel.remove(name);
		messagePanel.remove(FriendMessagePanel.get(name));
		FriendMessagePanel.remove(name);
	}


}

