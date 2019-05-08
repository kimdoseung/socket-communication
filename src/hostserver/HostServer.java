/*방장이나 사회자같은 느낌의 호스트서버이다 채팅서버소켓을 갖고있다
 *클라이언트가 소켓들고 접속해오면 그 ip를 받아서 소켓을생성해서 자기화면을 캡쳐해서 서버에올리는쓰레드를 돌린다*/
package hostserver;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HostServer extends JFrame {
	JPanel p_north;

	JTextField t_input;

	JTextArea area;// 채팅창
	JScrollPane scroll;
	JScrollBar bar;

	/* 서버용 */
	Thread serverThread; // 채팅을 가동하는 서버
	ChatServerThread chatServerThread;

	/* 화면전송용 */

	Thread captureThread; //화면을 캡쳐하는 쓰레드
	ConnectThread connectThread;// 화면을 서버로 보내는 쓰레드

	ServerSocket server;// 접속자 감지용 소켓

	Vector<ChatServerThread> chatList = new Vector(); // 서버측에 생성된 아바타들을 담을 객체!!
	Vector<ConnectThread> captureList = new Vector(); // 서버측에 생성된 아바타들을 담을 객체!!
	String ip;// 클라이언트에서 접속할때 그아이피를 받는것
	BufferedImage image; // 스크린샷이 저장될 버퍼공간

	public HostServer() {

		p_north = new JPanel();
		t_input = new JTextField();
		area = new JTextArea();
		scroll = new JScrollPane(area);
		bar = scroll.getVerticalScrollBar();

		serverThread = new Thread() {
			public void run() {
				hostServerRun();

			}
		};

		captureThread = new Thread() {
			public void run() {
				capture();
				System.out.println(image);
			}
		};
		serverThread.start();
		captureThread.start();

		setLayout(new BorderLayout());

		add(p_north, BorderLayout.NORTH);
		add(scroll);
		add(t_input, BorderLayout.SOUTH);

		area.setFont(new Font("돋움", Font.BOLD, 15));
		area.setEnabled(false);
		area.setDisabledTextColor(Color.BLACK);


		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					chatServerThread.send(t_input.getText());
					area.append("\n" + t_input.getText());
					t_input.setText("");
				}
			}
		});
		setBounds(0, 0, 400, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// 채팅서버 가동
	public void hostServerRun() {
		int port = 1187;
		try {
			server = new ServerSocket(port);// 서버 생성
			// area.append("서버 가동\n");

			while (true) {
				Socket client = server.accept();// 서버 가동(접속자를 기다림)
				ip = client.getInetAddress().getHostAddress();

				chatServerThread = new ChatServerThread(this, client);
				chatServerThread.start();
				chatList.add(chatServerThread);
				area.append("\n"+ip + "님 접속\n");

				area.append("\n현재 " + chatList.size() + "명 접속중\n");
				connect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void capture() {

		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		Robot r;// 스크린샷을 찍는 로봇클래스

		try {
			r = new Robot();
			while (true) {
				image = r.createScreenCapture(new Rectangle(0, 0, w, h));// 스크린샷을찍어 image에저장
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

//화면 전송하는 메서드
	public void connect() {
		System.out.println("클라이언트가 보내온" + ip);
		int port = 1188;
		Socket client;// 대화용 소켓
		// 소켓 생성시 접속이 발생!!
		try {
			client = new Socket(ip, port);
			connectThread = new ConnectThread(this, client);
			connectThread.start();
			captureList.add(connectThread);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new HostServer();
	}

}
