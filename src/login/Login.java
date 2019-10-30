package login;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;
import client.MainGui;
import tags.Encode;
import tags.Tags;
import data.BackgroundPanel;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.Font;
import javax.swing.UIManager;

public class Login {
    private static String USER_FAILED = "NAME DOESN'T EXIST";
    private static String PASS_FAILED = "WRONG PASSWORD";
    private static String NAME_EXSIST = "THIS NAME IS ALREADY USED. PLEASE TRY AGAIN";
    private static String SERVER_NOT_START = "TURN ON SERVER BEFORE START";

    private Pattern check_name = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");

    private JFrame frameLoginForm;
    private JTextField txtPort;
    private JLabel lblError;
    private String name = "", pass = "", IP = "", prtServer = "9600";
    private JTextField txtIP;	
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSignup;
    
    private Boolean sex = true;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login window = new Login();
                    window.frameLoginForm.setVisible(true);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        initialize();
    }

    private void initialize() {
        frameLoginForm = new JFrame();
        frameLoginForm.setTitle("Login");
        frameLoginForm.setResizable(false);
        frameLoginForm.setBounds(200, 200, 550, 350);
        frameLoginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLoginForm.setContentPane(new BackgroundPanel("/image/background.png"));
        frameLoginForm.getContentPane().setLayout(null);
        frameLoginForm.setLocationRelativeTo(null);
       

        JLabel lblWelcome = new JLabel("Connect to Server\r\n");
        lblWelcome.setForeground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblWelcome.setBounds(27, 13, 312, 48);
        frameLoginForm.getContentPane().add(lblWelcome);

        JLabel lblHostServer = new JLabel("IP Server");
        lblHostServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblHostServer.setBounds(47, 74, 86, 20);
        frameLoginForm.getContentPane().add(lblHostServer);

        JLabel lblPortServer = new JLabel("Port Server");
        lblPortServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPortServer.setBounds(349, 77, 79, 14);
        frameLoginForm.getContentPane().add(lblPortServer);

        txtPort = new JTextField();
        txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPort.setText("9600");
        txtPort.setEditable(false);
        txtPort.setColumns(10);
        txtPort.setBounds(429, 70, 65, 28);
        frameLoginForm.getContentPane().add(txtPort);

        JLabel lblUserName = new JLabel("Username");
        lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUserName.setBounds(10, 134, 106, 38);
        frameLoginForm.getContentPane().add(lblUserName);
        lblUserName.setIcon(new javax.swing.ImageIcon(Login.class.getResource("/image/private-access.png")));
        
        JLabel lblPassword = new JLabel("Passwords");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setBounds(46, 170, 106, 38);
        frameLoginForm.getContentPane().add(lblPassword);
        
        
        
        lblError = new JLabel("");
        lblError.setBounds(66, 287, 399, 20);
        frameLoginForm.getContentPane().add(lblError);

        txtIP = new JTextField();
        txtIP.setBounds(128, 70, 185, 28);
        frameLoginForm.getContentPane().add(txtIP);
        txtIP.setColumns(10);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setColumns(10);
        txtUsername.setBounds(128, 138, 366, 30);
        frameLoginForm.getContentPane().add(txtUsername);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setColumns(10);
        txtPassword.setBounds(128, 175, 366, 30);
        frameLoginForm.getContentPane().add(txtPassword);
        
        btnSignup = new JButton("Sign up");
        btnSignup.setFont(new Font("TimesRoman", Font.PLAIN, 13));
        btnSignup.setBackground(Color.WHITE);
        btnSignup.setIcon(new javax.swing.ImageIcon(Login.class.getResource("/image/signup.png")));
        
        btnSignup.addActionListener(new ActionListener() {
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frameLoginForm.dispose();
				SignUp.main(null);
			}
        });
        btnSignup.setBounds(200, 220, 130, 55);
        frameLoginForm.getContentPane().add(btnSignup);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("TimesRoman", Font.PLAIN, 13));
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setIcon(new javax.swing.ImageIcon(Login.class.getResource("/image/log-in (1).png")));
        btnLogin.addActionListener(new ActionListener() {   

            public void actionPerformed(ActionEvent arg0) {
                name = txtUsername.getText();
                pass = txtPassword.getText();
//                prtServer = txtPort.getText();
                boolean checkName = false;
                
                lblError.setVisible(false);
                IP = txtIP.getText();
                
                // // !IP.equals("") 
                if (check_name.matcher(name).matches() && !IP.equals("")) {
                	try {
                		File in = new File("");
                    	String currentDirectory = in.getAbsolutePath();
                    	BufferedReader fr = new BufferedReader(new FileReader(currentDirectory + "/login/account.txt"));
                        String txtInALine;
                        while((txtInALine = fr.readLine()) != null) {
                        	String[] account = txtInALine.split(" ");
                        	if (name.equals(account[0])) {
                        		checkName = true;
                        		if (pass.equals(account[1])) {
                        			fr.close();
                        			break;
                        		}
                        		lblError.setText(PASS_FAILED);
                                lblError.setVisible(true);
                                fr.close();
                                return;
                        	}
                        }
                        fr.close();
                        
                        if (!checkName) {
                        	lblError.setText(USER_FAILED);
                        	lblError.setVisible(true);
                        	return;
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                	
                    try {
                        Random rd = new Random();
                        int portUser = 10000 + rd.nextInt() % 1000;
                        InetAddress ipServer = InetAddress.getByName(IP);
                        int portServer = 9600;
                        Socket socketClient = new Socket(ipServer, portServer);

                        String msg = Encode.getCreateAccount(name, Integer.toString(portUser));
                        ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
                        serverOutputStream.writeObject(msg);
                        serverOutputStream.flush();
                        ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
                        msg = (String) serverInputStream.readObject();

                        socketClient.close();
                            if (msg.equals(Tags.SESSION_DENY_TAG)) {
                                lblError.setText(NAME_EXSIST);
                                lblError.setVisible(true);
                                return;
                            }
                        new MainGui(IP, portUser, name, msg);
//                        
                        frameLoginForm.dispose();
                    } 
                    catch (Exception e) {
                        lblError.setText(SERVER_NOT_START);
                        lblError.setVisible(true);
                        e.printStackTrace();
                    }
                }
                else {
                    lblError.setText(USER_FAILED);
                    lblError.setVisible(true);
                    lblError.setText(USER_FAILED);
                }
            }
        });
        
        btnLogin.setBounds(360, 220, 130, 55);
        frameLoginForm.getContentPane().add(btnLogin);
        lblError.setVisible(false);
    }
}