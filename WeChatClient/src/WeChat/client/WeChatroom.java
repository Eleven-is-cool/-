package WeChat.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import WeChat.function.ChatBean;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.ResourceUtil;
import com.iflytek.cloud.speech.Setting;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.util.DebugLog;
import com.iflytek.util.DrawableUtils;
import com.iflytek.util.JsonParser;
import com.iflytek.util.Version;


class CellRenderer extends JLabel implements ListCellRenderer {
	CellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));// ������Ϊ1�Ŀհױ߿�

		if (value != null) {
			setText(value.toString());
			setIcon(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\student.JPG"));
		}
		// ����ѡȡ��ȡ��ѡȡ��ǰ���뱳����ɫ.
		if (isSelected) {
			setBackground(new Color(255, 255, 153));// ���ñ���ɫ
			setForeground(Color.black);//��������ɫ
		} else {
			setBackground(Color.white); // ���ñ���ɫ
			setForeground(Color.black);//��������ɫ
		}
		setEnabled(list.isEnabled());
		setFont(new Font("sdf", Font.ROMAN_BASELINE, 13));
		setOpaque(true);
		return this;
	}
}


class UUListModel extends AbstractListModel{
	
	private Vector vs;
	
	public UUListModel(Vector vs){
		this.vs = vs;
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return vs.get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return vs.size();
	}
	
}


public class WeChatroom extends JFrame {
	// ������д����
	private SpeechRecognizer mIat;
	
	JScrollPane scrollPane;
	final JTextArea textArea_1;
	final JButton btnNewButton;
	JButton btnNewButton_2;
	JButton btnNewButton_1;
	
	private Map<String, String> mParamMap = new HashMap<String, String>();
	private JPanel mMainJpanel;
	private static JPanel contentPane;
	private static Socket clientSocket;
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;
	private static String name;
	private static JTextArea textArea;
	private static AbstractListModel listmodel;
	private static JList list;
	private static String filePath;
	private static JLabel lblNewLabel;
	private static JProgressBar progressBar;
	private static Vector onlines;
	private static boolean isSendFile = false;
	private static boolean isReceiveFile = false;

	// ����
	private static File file, file2;
	private static URL cb, cb2;
	private static AudioClip aau, aau2;
	private static class DefaultValue{
		public static final String ENG_TYPE = SpeechConstant.TYPE_CLOUD;
		public static final String SPEECH_TIMEOUT = "60000";
		public static final String NET_TIMEOUT = "20000";
		public static final String LANGUAGE = "zh_cn";
		
		public static final String ACCENT = "mandarin";
		public static final String DOMAIN = "iat";
		public static final String VAD_BOS = "5000";
		public static final String VAD_EOS = "1800";
		
		public static final String RATE = "16000";
		public static final String NBEST = "1";
		public static final String WBEST = "1";
		public static final String PTT = "1";
		
		public static final String RESULT_TYPE = "json";
		public static final String SAVE = "0";
	}

	private static final String DEF_FONT_NAME = "����";
	private static final int DEF_FONT_STYLE = Font.BOLD;
	private static final int DEF_FONT_SIZE = 30;
	private static final int TEXT_COUNT = 100;
	/**
	 * Create the frame.
	 */

	public WeChatroom(String u_name, Socket client)  {
		// ��ֵ
		name = u_name;
		clientSocket = client;
		onlines = new Vector();
		
		SwingUtilities.updateComponentTreeUI(this);

		try {//����Windows���
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
}
	
		setTitle(name);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(200, 100, 688, 510);
		contentPane = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("E:\\code\\Java\\StudySystem\\WeChatClient\\src\\images\\WeChatroom.JPG").getImage(), 0, 0,
						getWidth(), getHeight(), null);
			}

		};
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ������Ϣ��ʾ����
		scrollPane = new JScrollPane();
		scrollPane.setBounds(252, 15, 410, 300);
		getContentPane().add(scrollPane);//��getContentPane()�������JFrame��������壬�ٶ���������

		// ��������
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(252, 345, 410, 95);
		getContentPane().add(scrollPane_1);

		textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);//�����Զ����й��� 
		textArea_1.setWrapStyleWord(true);//������в����ֹ��� 
		scrollPane_1.setViewportView(textArea_1);

		// �رհ�ť
		btnNewButton = new JButton("\u5173\u95ED");
		btnNewButton.setBounds(530, 440, 60, 30);
		getContentPane().add(btnNewButton);

		// ���Ͱ�ť
		btnNewButton_1 = new JButton("\u53D1\u9001");
		btnNewButton_1.setBounds(600, 440, 60, 30);
		getRootPane().setDefaultButton(btnNewButton_1);
		getContentPane().add(btnNewButton_1);

		// ����ѧ���б�
		listmodel = new UUListModel(onlines) ;
		list = new JList(listmodel);
		list.setCellRenderer(new CellRenderer());
		list.setOpaque(false);
		Border etch = BorderFactory.createEtchedBorder();
		list.setBorder(BorderFactory.createTitledBorder(etch, "����ѧ��:", TitledBorder.LEADING, TitledBorder.TOP, new Font(
				"sdf", Font.BOLD, 15), Color.blue));

		JScrollPane scrollPane_2 = new JScrollPane(list);
		scrollPane_2.setBounds(10, 10, 230, 430);
		scrollPane_2.setOpaque(false);
		scrollPane_2.getViewport().setOpaque(false);
		getContentPane().add(scrollPane_2);
		
		StringBuffer param = new StringBuffer();
		param.append( "appid=" + Version.getAppid() );
		SpeechUtility.createUtility( param.toString() );
		
		
		btnNewButton_2 = new JButton("\u8BED\u97F3\u8BC6\u522B");
		btnNewButton_2.setFont(new Font("����", Font.PLAIN, 12));
		
		btnNewButton_2.setBounds(252, 316, 81, 26);
		contentPane.add(btnNewButton_2);
		
		textArea = new JTextArea();
		textArea.setBounds(254, 16, 408, 299);
		contentPane.add(textArea);
		textArea.setEditable(false);
		textArea.setLineWrap(true);//�����Զ����й��� 
		textArea.setWrapStyleWord(true);//������в����ֹ��� 
		textArea.setFont(new Font("sdf", Font.BOLD, 13));
		
		JButton quxiao = new JButton("\u505C\u6B62");

		quxiao.setFont(new Font("����", Font.PLAIN, 12));
		quxiao.setBounds(343, 316, 57, 26);
		contentPane.add(quxiao);

		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			// ��¼���߿ͻ�����Ϣ��catbean�У������͸�������
			ChatBean bean = new ChatBean();
			bean.setType(0);
			bean.setName(name);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			bean.setTimer(sdf.format(new Date()));
			
			oos.writeObject(bean);
			oos.flush();

			// ��Ϣ��ʾ����
			file = new File("sounds\\s1.wav");
			cb = file.toURL();
			aau = Applet.newAudioClip(cb);
			// ������ʾ����
			file2 = new File("sounds\\s2.wav");
			cb2 = file2.toURL();
			aau2 = Applet.newAudioClip(cb2);

			// �����ͻ������߳�
			new ClientInputThread().start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��ʼ����д����
		mIat=SpeechRecognizer.createRecognizer();
		initParamMap();
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				setting();
				textArea_1.setText( "" );
				if (!mIat.isListening())
					mIat.startListening(recognizerListener);
				else
					mIat.stopListening();
			
				
			}
			
		});
		quxiao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mIat.stopListening();
				iatSpeechInitUI();
				
			}
			/**
			 * ��д�������ָ���ʼ״̬
			 */
			public void iatSpeechInitUI() {	
				btnNewButton_2.setEnabled(true);
				//((JLabel) btnNewButton_2.getComponent(0)).setText("��ʼ��д");
			}
		});
		// ���Ͱ�ť
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String info = textArea_1.getText();
				List to = list.getSelectedValuesList();

				if (to.size() < 1) {
					JOptionPane.showMessageDialog(getContentPane(), "��ѡ���������");
					return;
				}
				if (to.toString().contains(name+"(��)")) {
					JOptionPane
							.showMessageDialog(getContentPane(), "�������Լ�������Ϣ");
					return;
				}
				if (info.equals("")) {
					JOptionPane.showMessageDialog(getContentPane(), "���ܷ��Ϳ���Ϣ");
					return;
				}

				ChatBean clientBean = new ChatBean();
				clientBean.setType(1);
				clientBean.setName(name);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");							
				
				String time = sdf.format(new Date());
				clientBean.setTimer(time);
				clientBean.setInfo(info);//��Ϣ����
				HashSet set = new HashSet();
				set.addAll(to);
				clientBean.setClients(set);

				// �Լ���������ҲҪ��ʵ���Լ�����Ļ����
				textArea.append(time + " �Ҷ�" + to + "˵:\r\n" + info + "\r\n");

				sendMessage(clientBean);
				textArea_1.setText(null);
				textArea_1.requestFocus();
			}
		});

		// �رհ�ť
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				btnNewButton.setEnabled(false);
				ChatBean clientBean = new ChatBean();
				clientBean.setType(-1);
				clientBean.setName(name);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
				
				clientBean.setTimer(sdf.format(new Date()));
				sendMessage(clientBean);
				
			}
		});

		// �뿪
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
				int result = JOptionPane.showConfirmDialog(getContentPane(),
						"��ȷ��Ҫ�뿪������");
				if (result == 0) {
					ChatBean clientBean = new ChatBean();
					clientBean.setType(-1);
					clientBean.setName(name);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
					clientBean.setTimer(sdf.format(new Date()));
					sendMessage(clientBean);
				}
				
			}
		});

		// �б����
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				

			}
		});

	}
	/***
	 * ������ʵ��. ��ť���¶���ʵ��
	 */


	class ClientInputThread extends Thread {

		@Override
		public void run() {
			try {
				// ��ͣ�Ĵӷ�����������Ϣ
				while (true) {
					ois = new ObjectInputStream(clientSocket.getInputStream());
					final ChatBean  bean = (ChatBean) ois.readObject();
					switch (bean.getType()) {
					case 0: {
						// �����б�
						onlines.clear();
						HashSet<String> clients = bean.getClients();
						Iterator<String> it = clients.iterator();
						while (it.hasNext()) {
							String ele = it.next();
							if (name.equals(ele)) {
								onlines.add(ele + "(��)");
							} else {
								onlines.add(ele);
							}
						}

						listmodel = new UUListModel(onlines);
						list.setModel(listmodel);
						aau2.play();
						textArea.append(bean.getInfo() + "\r\n");
						textArea.selectAll();
						break;
					}
					case -1: {
						
						return;
					}
					case 1: {
						
						String info = bean.getTimer() + "  " + bean.getName()
								+ " �� " + bean.getClients() + "˵:\r\n";
						if (info.contains(name) ) {
							info = info.replace(name, "��");
						}
						aau.play();
						textArea.append(info+bean.getInfo() + "\r\n");
						textArea.selectAll();
						break;
					}
					case 4: {
						textArea.append(bean.getInfo() + "\r\n");
						break;
					}
					default: {
						break;
					}
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		}
	}
	
	
	/**
	 * ��д�������ָ���ʼ״̬
	 */
	public void iatSpeechInitUI() {
		btnNewButton_2.setEnabled(true);
		((JLabel) btnNewButton_2.getComponent(0)).setText("��ʼ��д");
	}
	/**
	 * ��д������
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			DebugLog.Log( "onBeginOfSpeech enter" );
			//((JLabel) btnNewButton_2.getComponent(0)).setText("��д��...");
			btnNewButton_2.setEnabled(false);
		}

		@Override
		public void onEndOfSpeech() {
			DebugLog.Log( "onEndOfSpeech enter" );
		}

		/**
		 * ��ȡ��д���. ��ȡRecognizerResult���͵�ʶ���������Խ�������ۼӣ���ʾ��Area��
		 */
		@Override
		public void onResult(RecognizerResult results, boolean islast) {
			DebugLog.Log( "onResult enter" );
			
			//���Ҫ����json������뿼����Ŀʾ���� com.iflytek.util.JsonParser��
			//���÷���json����ķ���
			String text = JsonParser.parseIatResult(results.getResultString());
			//String text = results.getResultString();
			textArea_1.append(text);
			text = textArea_1.getText();
			if( null!=text ){
				int n = text.length() / TEXT_COUNT + 1;
				int fontSize = Math.max( 10, DEF_FONT_SIZE - 2*n );
				DebugLog.Log( "onResult new font size="+fontSize );
				int style = n>1 ? Font.PLAIN : DEF_FONT_SIZE;
				Font newFont = new Font( DEF_FONT_NAME, style, fontSize );
				textArea_1.setFont( newFont );
			}

			if( islast ){
				iatSpeechInitUI();
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			DebugLog.Log( "onVolumeChanged enter" );
			if (volume == 0)
				volume = 1;
			else if (volume >= 6)
				volume = 6;
		}

		@Override
		public void onError(SpeechError error) {
			DebugLog.Log( "onError enter" );
			if (null != error){
				DebugLog.Log("onError Code��" + error.getErrorCode());
				textArea_1.setText( error.getErrorDescription(true) );
				iatSpeechInitUI();
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int agr2, String msg) {
			DebugLog.Log( "onEvent enter" );
			//���´������ڵ��ԣ��������������Խ�sid�ṩ��Ѷ�ɿ����ߣ��������ⶨλ�Ų�
			/*if(eventType == SpeechEvent.EVENT_SESSION_ID) {
				DebugLog.Log("sid=="+msg);
			}*/
		}
		/**
		 * ��д�������ָ���ʼ״̬
		 */
		public void iatSpeechInitUI() {	
			btnNewButton_2.setEnabled(true);
			//((JLabel) btnNewButton_2.getComponent(0)).setText("��ʼ��д");
			
		}			
			
	};
	private void initParamMap(){
		this.mParamMap.put( SpeechConstant.ENGINE_TYPE, DefaultValue.ENG_TYPE );
		this.mParamMap.put( SpeechConstant.SAMPLE_RATE, DefaultValue.RATE );
		this.mParamMap.put( SpeechConstant.NET_TIMEOUT, DefaultValue.NET_TIMEOUT );
		this.mParamMap.put( SpeechConstant.KEY_SPEECH_TIMEOUT, DefaultValue.SPEECH_TIMEOUT );
		
		this.mParamMap.put( SpeechConstant.LANGUAGE, DefaultValue.LANGUAGE );
		this.mParamMap.put( SpeechConstant.ACCENT, DefaultValue.ACCENT );
		this.mParamMap.put( SpeechConstant.DOMAIN, DefaultValue.DOMAIN );
		this.mParamMap.put( SpeechConstant.VAD_BOS, DefaultValue.VAD_BOS );
		
		this.mParamMap.put( SpeechConstant.VAD_EOS, DefaultValue.VAD_EOS );
		this.mParamMap.put( SpeechConstant.ASR_NBEST, DefaultValue.NBEST );
		this.mParamMap.put( SpeechConstant.ASR_WBEST, DefaultValue.WBEST );
		this.mParamMap.put( SpeechConstant.ASR_PTT, DefaultValue.PTT );
		
		this.mParamMap.put( SpeechConstant.RESULT_TYPE, DefaultValue.RESULT_TYPE );
		this.mParamMap.put( SpeechConstant.ASR_AUDIO_PATH, null );
	}
	private void sendMessage(ChatBean clientBean) {
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			oos.writeObject(clientBean);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void setting(){
		final String engType = this.mParamMap.get(SpeechConstant.ENGINE_TYPE);
		
		for( Entry<String, String> entry : this.mParamMap.entrySet() ){
			mIat.setParameter( entry.getKey(), entry.getValue() );
		}
		
		//����ʶ��ʱ������Դ������������
		if( SpeechConstant.TYPE_LOCAL.equals(engType) ){
			//�����ϳ�����
			mIat.setParameter( ResourceUtil.ENGINE_START, SpeechConstant.ENG_ASR );
			
			//������Դ·��
			final String rate = this.mParamMap.get( SpeechConstant.SAMPLE_RATE );
			final String tag = rate.equals("16000") ? "16k" : "8k";
			String curPath = System.getProperty("user.dir");
			DebugLog.Log( "Current path="+curPath );
			String resPath = ResourceUtil.generateResourcePath( curPath+"/asr/common.jet" )
					+ ";" + ResourceUtil.generateResourcePath( curPath+"/asr/src_"+tag+".jet" );
			System.out.println( "resPath="+resPath );
			mIat.setParameter( ResourceUtil.ASR_RES_PATH, resPath );
		}// end of if is TYPE_LOCAL
	}

}
