package WeChat.login;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import WeChat.client.WeChatroom;
import WeChat.function.ChatBean;
import WeChat.function.ClientBean;

public class ChatLogin extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;	
	//public static HashMap<String, ClientBean> onlines;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {//这个方法调用完毕后，它会被销毁，因为匿名内部类是作为临时变量存在的，给它分配的内存在此时会被释放。
			public void run() {
				try {
					// 启动登陆界面
					ChatLogin frame = new ChatLogin();
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
	public ChatLogin() {
		setTitle("Landing study chat room\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗口是可关闭的
		setBounds(350, 250, 450, 300);
		contentPane = new JPanel() {
			//private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);//父类JPanel里的方法,会把整个面板用背景色重画一遍,起到清屏的作用
				g.drawImage(new ImageIcon(
						"E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\login.JPG").getImage(), 0,
						0, getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(138, 157, 105, 20);
		textField.setOpaque(false);
		contentPane.add(textField);
		//textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setForeground(Color.BLACK);
		passwordField.setEchoChar('*');
		passwordField.setOpaque(false);
		passwordField.setBounds(138, 195, 105, 20);
		contentPane.add(passwordField);

		final JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\登录.JPG"));
		btnNewButton.setBounds(250, 220, 66, 34);
		getRootPane().setDefaultButton(btnNewButton);
		
		contentPane.add(btnNewButton);

		final JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setIcon(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\注册b.JPG"));
		btnNewButton_1.setBounds(340, 220, 66, 34);
		contentPane.add(btnNewButton_1);

		// 提示信息
		final JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(55, 230, 150, 20);
		lblNewLabel.setForeground(Color.red);
		getContentPane().add(lblNewLabel);

		// 监听登陆按钮
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String u_name = textField.getText();
				if (u_name.length() != 0) {
					String u_pwd = new String(passwordField.getPassword());
					if (u_pwd.length() != 0) {
						String user = "sa";
					    String password = "123123";
						Connection conn;
						Statement stmt;
						ResultSet rs;
					    String url = "jdbc:sqlserver://localhost:1433;DatabaseName=users;";
					    String sql = "select password from users where name='"+u_name+"'";
					    try {
					         // 连接数据库
					         conn = DriverManager.getConnection(url, user, password);  
					         stmt = conn.createStatement();        
					         rs = stmt.executeQuery(sql);
					         String res=null;
					         while(rs.next()) {
					              res=rs.getString(1);
//					              System.out.println(res);
					         }					         
//					         System.out.println(rs);
					         conn.close();
					         
					         if (u_pwd.equals(res)) {
									try {
										Socket client = new Socket("127.0.0.1", 8500);//可以更换成同一局域网下客户端的IP
										btnNewButton.setEnabled(false);
										WeChatroom frame = new WeChatroom(u_name,
												client);
										frame.setVisible(true);// 显示聊天界面
										setVisible(false);// 隐藏掉登陆界面

									} catch (UnknownHostException e1) {
										// TODO Auto-generated catch block
										errorTip("The connection with the server is interrupted, please login again");
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										errorTip("The connection with the server is interrupted, please login again");
									}

								} else {
									lblNewLabel.setText("密码错误！");
									textField.setText("");
									passwordField.setText("");
									textField.requestFocus();
								}					        		         
					    } catch (SQLException s) {
					         s.printStackTrace();
					         System.out.println("数据库连接失败");
					    } 

					} else {
						lblNewLabel.setText("用户名不存在！");
						textField.setText("");
						passwordField.setText("");
						textField.requestFocus();
					}
				} else {
					lblNewLabel.setText("用户名不存在！");
					textField.setText("");
					passwordField.setText("");
					textField.requestFocus();
				}
			}
		});

		//注册按钮监听
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton_1.setEnabled(false);
				ChatResign frame = new ChatResign();
				frame.setVisible(true);// 显示注册界面
				setVisible(false);// 隐藏掉登陆界面
			}
		});
	}

	protected void errorTip(String str) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(contentPane, str, "Error Message",
				JOptionPane.ERROR_MESSAGE);
		textField.setText("");
		passwordField.setText("");
		textField.requestFocus();
	}
}