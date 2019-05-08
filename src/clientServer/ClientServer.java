/*호스트서버에 접속할 클라이언트인데 얘도 화면전송에관한 서버소켓을 갖고있어서 클라이언트서버라고 지었다*/
package clientServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.prompt.PromptSupport;





public class ClientServer extends JFrame {
	JPanel p_screen, p_east;
	JPanel p_con, p_chat;

	JTextField t_ip;
	JTextField t_input;
	JButton bt;
	JTextArea area;
	JScrollPane scroll;
	ServerSocket server;// 접속자 감지용 소켓
	int port;
	Socket client;// 대화용 소켓
	
	//서버구축관련 객체 
	Thread paintServerThread; //서버측을 접속을 받아들이는 소켓서버 
	PaintThread paintThread; //화면받아온걸 그리는 쓰레드
	
	
	//클라이언트 측 관련 객체 
	Thread connectThread; //접속을 위한 쓰레드
	ChatClientThread chatClientThread;//대화용 쓰레드
	
	Vector<PaintThread> list = new Vector(); 
	Vector<ChatClientThread> chatlist = new Vector(); 
	

	
	public ClientServer() {

		p_east = new JPanel();
		p_screen = new JPanel();
		p_con = new JPanel();
		p_chat = new JPanel();

		t_ip = new JTextField(8);
		PromptSupport.setPrompt("ip입력", t_ip); 

		bt = new JButton("접속");

		area = new JTextArea();
		area.setEnabled(false);
		scroll = new JScrollPane(area);
		t_input = new JTextField();

		area.setFont(new Font("돋움", Font.BOLD, 15));
		area.setDisabledTextColor(Color.BLACK);

		p_east.setLayout(new BorderLayout());
		p_east.add(p_con, BorderLayout.NORTH);
		p_east.add(p_chat);

		p_con.add(t_ip);

		p_con.add(bt);
		p_chat.setLayout(new BorderLayout());
		p_chat.add(scroll);
		p_chat.add(t_input, BorderLayout.SOUTH);
		add(p_east, BorderLayout.EAST);
		add(p_screen);
		
		paintServerThread = new Thread() {
			public void run() {
				runPaintServer();
			}
		};
		paintServerThread.start();
		
		connectThread = new Thread() {
			@Override
			public void run() {
				connectServer();
			}
		};
		
		// 버튼과 리스너 연결
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectThread.start();
			}
		});
		
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_ENTER) {
					try {
						InetAddress inetAddress = InetAddress.getLocalHost();
						chatClientThread.send(inetAddress.getHostAddress()+": "+t_input.getText());
						t_input.setText("");
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
		p_east.setPreferredSize(new Dimension(400, 1000));
		p_screen.setPreferredSize(new Dimension(1500, 1000));
		setBounds(0, 0, 1900, 1000);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/*-------------------------------------------------------------------------
	 화면 전송을 위한 서버 시작 메서드 
	-------------------------------------------------------------------------*/
	public void runPaintServer() {

		try {
			server = new ServerSocket(1188);// 서버 생성

			while (true) {
				Socket client = server.accept();// 서버 가동(접속자를 기다림)
				PaintThread paintThread = new PaintThread(this, client);
				list.add(paintThread);
				paintThread.start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*-------------------------------------------------------------------------
	채팅을 위하 서버 접속 메서드 
	-------------------------------------------------------------------------*/
	public void connectServer() {
		
		port = 1187;
		String ip = t_ip.getText();
		// 소켓 생성시 접속이 발생!!
		try {
			client = new Socket(ip, port);
			// 대화용 쓰레드 생성!!!
			chatClientThread= new ChatClientThread(this, client);
			chatlist.add(chatClientThread);
			chatClientThread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ClientServer();
	}
}
