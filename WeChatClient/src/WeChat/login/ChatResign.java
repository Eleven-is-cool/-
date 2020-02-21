package WeChat.login;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;



public class ChatResign extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JLabel lblNewLabel;

	public ChatResign() {
		setTitle("Registered study chat room\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 250, 450, 300);
		contentPane = new JPanel() {
			//private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\resign.JPG").getImage(), 0,0, getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setBounds(190, 41, 105, 20);
		textField.setOpaque(false);
		contentPane.add(textField);
		//textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setEchoChar('*');	
		passwordField.setBounds(190, 87, 105, 20);
		passwordField.setOpaque(false);
		contentPane.add(passwordField);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(190, 135, 105, 20);
		passwordField_1.setOpaque(false);
		contentPane.add(passwordField_1);

		//注册按钮
		final JButton btnNewButton_1 = new JButton();
		btnNewButton_1.setIcon(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\注册a.JPG"));
		btnNewButton_1.setBounds(330, 212, 66, 38);
		getRootPane().setDefaultButton(btnNewButton_1);// 设置按钮为其所在顶层容器组件的默认按钮
		contentPane.add(btnNewButton_1);

		//返回按钮
		final JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\返回.JPG"));
		btnNewButton.setBounds(240, 212, 66, 38);
		contentPane.add(btnNewButton);

		//提示信息
		lblNewLabel = new JLabel();
		lblNewLabel.setBounds(240, 175, 185, 20);
		lblNewLabel.setForeground(Color.red);
		contentPane.add(lblNewLabel);
		
		//返回按钮监听
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(false);
				//返回登陆界面
				ChatLogin frame = new ChatLogin();
				frame.setVisible(true);
				setVisible(false);
			}
		});
		
		//注册按钮监听
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			    
				String u_name = textField.getText();
				String u_pwd = new String(passwordField.getPassword());
				String u_pwd_ag = new String(passwordField_1.getPassword());

				// 判断用户名是否在普通用户中已存在
				if (u_name.length() != 0) {
					String user = "sa";
				    String password = "123123";
					Connection conn;
					Statement stmt;
					ResultSet rs;
				    String url = "jdbc:sqlserver://localhost:1433;DatabaseName=users;";
				    String sql = "select * from users where name='"+u_name+"'";				    
				    if (u_name.length() != 0) {
						try {							
							conn = DriverManager.getConnection(url, user, password);  
					        stmt = conn.createStatement();        
					        rs = stmt.executeQuery(sql);
					        if(rs.next()) {				        	
					            lblNewLabel.setText("用户名已存在!");
					        }
					        else{
					            isPassword(u_name, u_pwd, u_pwd_ag); 
					        }					          
						}catch (SQLException s) {
					         s.printStackTrace();
					         System.out.println("数据库连接失败");
					    }  						
					} 
				    else {
						lblNewLabel.setText("用户名不能为空！");
					}						
				}  	
			}
			
			private void isPassword(String u_name,String u_pwd, String u_pwd_ag) {
				if (u_pwd.equals(u_pwd_ag)) {
					if (u_pwd.length() != 0) {
						String user = "sa";
					    String password = "123123";
						Connection conn;
						Statement stmt;
						ResultSet rs;
					    String url = "jdbc:sqlserver://localhost:1433;DatabaseName=users;";
					    String sql = "insert into users values('"+u_name+"', '"+u_pwd_ag+"')"; 
					    try {
					         // 连接数据库
					         conn = DriverManager.getConnection(url, user, password); 
					         stmt = conn.createStatement();        
					         stmt.executeUpdate(sql);
					         System.out.println("注册成功！"); 
					         conn.close();
					    } catch (SQLException s) {
					         s.printStackTrace();
					         System.out.println("数据库连接失败");
					    } 
							
						btnNewButton_1.setEnabled(false);
						//返回登陆界面
						ChatLogin frame = new ChatLogin();
						frame.setVisible(true);
						setVisible(false);
					} else {
						lblNewLabel.setText("输入密码有误！");
					}
				} else {
					lblNewLabel.setText("输入密码不一致！");
				}
			}
		});
	}
}