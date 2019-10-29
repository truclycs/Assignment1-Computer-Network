package login;

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

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.Font;
import javax.swing.UIManager;

public class SignUp {
    private static String NAME_FAILED = "THIS NAME CONTAINS INVALID CHARACTER. PLEASE TRY AGAIN";
    private static String NAME_EXSIST = "THIS NAME IS ALREADY USED. PLEASE TRY AGAIN";
    private static String SERVER_NOT_START = "SERVER HAVE NOT YET READY";
    private static String PASS_FAILED = "PASSWORD MUST HAVE AT LEAST 6 CHARACTER!";
    private static String PASS_ERROR = "TYPE WRONG CONFIRM PASSWORD !";
    private static String PASS_INVALID = "THIS PASSWORD HAVE INVALID CHARACTER. TRY AGAIN";

    private Pattern check_name = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
    private Pattern check_pass = Pattern.compile("[_a-zA-Z0-9]+");

    private JFrame frameSignUpForm;
    private JTextField txtPort;
    private JLabel lblError;
    private String name = "", IP = "", pass = "", confirm = "", prtServer = "9600";
    private JTextField txtIP;	
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirm;
    private JButton btnSignUp;
    
    private Boolean sex = true;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SignUp window = new SignUp();
                    window.frameSignUpForm.setVisible(true);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SignUp() {
        initialize();
    }

    private void initialize() {
        frameSignUpForm = new JFrame();
        frameSignUpForm.setTitle("SignUp");
        frameSignUpForm.setResizable(false);
        frameSignUpForm.setBounds(200, 200, 550, 350);
        frameSignUpForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameSignUpForm.getContentPane().setLayout(null);
        frameSignUpForm.setLocationRelativeTo(null);

        JLabel lblWelcome = new JLabel("Connect to Server\r\n");
        lblWelcome.setForeground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblWelcome.setBounds(27, 13, 312, 48);
        frameSignUpForm.getContentPane().add(lblWelcome);

        JLabel lblHostServer = new JLabel("IP Server");
        lblHostServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblHostServer.setBounds(47, 74, 86, 20);
        frameSignUpForm.getContentPane().add(lblHostServer);

        JLabel lblPortServer = new JLabel("Port Server");
        lblPortServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPortServer.setBounds(349, 77, 79, 14);
        frameSignUpForm.getContentPane().add(lblPortServer);

        txtPort = new JTextField();
        txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPort.setText(prtServer);
//        txtPort.setEditable(false);
        txtPort.setColumns(10);
        txtPort.setBounds(429, 70, 65, 28);
        frameSignUpForm.getContentPane().add(txtPort);

        JLabel lblUserName = new JLabel("User Name");
        lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUserName.setBounds(10, 134, 106, 38);
        frameSignUpForm.getContentPane().add(lblUserName);
        lblUserName.setIcon(new javax.swing.ImageIcon(SignUp.class.getResource("/image/boy.png")));
        
        JLabel lblPassword = new JLabel("Passwords");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setBounds(46, 170, 106, 38);
        frameSignUpForm.getContentPane().add(lblPassword);
        
        JRadioButton maleBtn = new JRadioButton("male");
        maleBtn.setMnemonic(KeyEvent.VK_B);
        maleBtn.setActionCommand("male");
        maleBtn.setSelected(true);
        maleBtn.setBounds(10, 230, 80, 25);
        maleBtn.setVisible(true);
        frameSignUpForm.getContentPane().add(maleBtn);
        
        JRadioButton femaleBtn = new JRadioButton("female");
        femaleBtn.setMnemonic(KeyEvent.VK_C);
        femaleBtn.setActionCommand("female");
        femaleBtn.setBounds(90, 230, 80, 25);
        femaleBtn.setVisible(true);
        frameSignUpForm.getContentPane().add(femaleBtn);
        maleBtn.addActionListener(new ActionListener() {   

            public void actionPerformed(ActionEvent arg0) {
            	lblUserName.setIcon(new javax.swing.ImageIcon(SignUp.class.getResource("/image/boy.png")));
            	femaleBtn.setSelected(false);
            	sex = true;
            }
        });
        
        
        femaleBtn.addActionListener(new ActionListener() {   

            public void actionPerformed(ActionEvent arg0) {
            	lblUserName.setIcon(new javax.swing.ImageIcon(SignUp.class.getResource("/image/girl.png")));
            	maleBtn.setSelected(false);
            	sex = false;
            }
        });
        
        lblError = new JLabel("");
        lblError.setBounds(66, 287, 399, 20);
        frameSignUpForm.getContentPane().add(lblError);

        txtIP = new JTextField();
        txtIP.setBounds(128, 70, 185, 28);
        frameSignUpForm.getContentPane().add(txtIP);
        txtIP.setColumns(10);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setColumns(10);
        txtUsername.setBounds(128, 138, 366, 30);
        frameSignUpForm.getContentPane().add(txtUsername);
        
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setColumns(10);
        txtPassword.setBounds(128, 175, 180, 30);
        frameSignUpForm.getContentPane().add(txtPassword);

        txtConfirm = new JPasswordField();
        txtConfirm.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtConfirm.setColumns(10);
        txtConfirm.setBounds(314, 175, 180, 30);
        frameSignUpForm.getContentPane().add(txtConfirm);
        
        btnSignUp = new JButton("Create Account and Login");
        btnSignUp.setFont(new Font("TimesRoman", Font.PLAIN, 13));
        btnSignUp.setIcon(new javax.swing.ImageIcon(SignUp.class.getResource("/image/login.png")));
        btnSignUp.addActionListener(new ActionListener() {   

            public void actionPerformed(ActionEvent arg0) {
                name = txtUsername.getText();
                pass = txtPassword.getText();
                confirm = txtConfirm.getText();
                prtServer = txtPort.getText();
                
                lblError.setVisible(false);
                IP = txtIP.getText();
                
                //must edit here
                if (check_name.matcher(name).matches() && !IP.equals("")) {
                	try {
                		File in = new File("");
                    	String currentDirectory = in.getAbsolutePath();
                    	BufferedReader fr = new BufferedReader(new FileReader(currentDirectory + "/login/account.txt"));
                        String txtInALine;
                        while((txtInALine = fr.readLine()) != null) {
                        	String[] account = txtInALine.split(" ");
                        	if (name.equals(account[0])) {
                        		lblError.setText(NAME_EXSIST);
                                lblError.setVisible(true);
                                fr.close();
                                return;
                        	}
                        }
                        fr.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                	
                	
                	if (check_pass.matcher(pass).matches()) {
                		if (pass.length() >= 6) {
                			if (pass.equals(confirm)) {
                				try {
                                    Random rd = new Random();
                                    int portUser = 10000 + rd.nextInt() % 1000;
                                    InetAddress ipServer = InetAddress.getByName(IP);
                                    int portServer = Integer.parseInt(prtServer);
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
                                    
                                    try {
                                    	File file = new File("");
                                    	String currentDirectory = file.getAbsolutePath();
                                        FileWriter fw = new FileWriter(currentDirectory + "/login/account.txt", true);
                                        fw.write(name + " " + pass + " " +Integer.toString((sex)?1:0) + "\n");
                                        fw.close();
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
//                                    
                                    frameSignUpForm.dispose();
                                } 
                                catch (Exception e) {
                                    lblError.setText(SERVER_NOT_START);
                                    lblError.setVisible(true);
                                    e.printStackTrace();
                                }
                			}
                			else {
                				lblError.setText(PASS_ERROR);
                				lblError.setVisible(true);
                				lblError.setText(PASS_ERROR);
                			}
                		}
                		else {
                			lblError.setText(PASS_FAILED);
                			lblError.setVisible(true);
                			lblError.setText(PASS_FAILED);
                		}
                	}
                	else {
                		lblError.setText(PASS_INVALID);
                		lblError.setVisible(true);
                		lblError.setText(PASS_INVALID);
                	}
                    
                }
                else {
                    lblError.setText(NAME_FAILED);
                    lblError.setVisible(true);
                    lblError.setText(NAME_FAILED);
                }
            }
        });
        
        btnSignUp.setBounds(265, 220, 230, 55);
        frameSignUpForm.getContentPane().add(btnSignUp);
        lblError.setVisible(false);
    }
}