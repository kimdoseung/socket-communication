/*�����̳� ��ȸ�ڰ��� ������ ȣ��Ʈ�����̴� ä�ü��������� �����ִ�
 *Ŭ���̾�Ʈ�� ���ϵ�� �����ؿ��� �� ip�� �޾Ƽ� �����������ؼ� �ڱ�ȭ���� ĸ���ؼ� �������ø��¾����带 ������*/
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

	JTextArea area;// ä��â
	JScrollPane scroll;
	JScrollBar bar;

	/* ������ */
	Thread serverThread; // ä���� �����ϴ� ����
	ChatServerThread chatServerThread;

	/* ȭ�����ۿ� */

	Thread captureThread; //ȭ���� ĸ���ϴ� ������
	ConnectThread connectThread;// ȭ���� ������ ������ ������

	ServerSocket server;// ������ ������ ����

	Vector<ChatServerThread> chatList = new Vector(); // �������� ������ �ƹ�Ÿ���� ���� ��ü!!
	Vector<ConnectThread> captureList = new Vector(); // �������� ������ �ƹ�Ÿ���� ���� ��ü!!
	String ip;// Ŭ���̾�Ʈ���� �����Ҷ� �׾����Ǹ� �޴°�
	BufferedImage image; // ��ũ������ ����� ���۰���

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

		area.setFont(new Font("����", Font.BOLD, 15));
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

	// ä�ü��� ����
	public void hostServerRun() {
		int port = 1187;
		try {
			server = new ServerSocket(port);// ���� ����
			// area.append("���� ����\n");

			while (true) {
				Socket client = server.accept();// ���� ����(�����ڸ� ��ٸ�)
				ip = client.getInetAddress().getHostAddress();

				chatServerThread = new ChatServerThread(this, client);
				chatServerThread.start();
				chatList.add(chatServerThread);
				area.append("\n"+ip + "�� ����\n");

				area.append("\n���� " + chatList.size() + "�� ������\n");
				connect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void capture() {

		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		Robot r;// ��ũ������ ��� �κ�Ŭ����

		try {
			r = new Robot();
			while (true) {
				image = r.createScreenCapture(new Rectangle(0, 0, w, h));// ��ũ��������� image������
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

//ȭ�� �����ϴ� �޼���
	public void connect() {
		System.out.println("Ŭ���̾�Ʈ�� ������" + ip);
		int port = 1188;
		Socket client;// ��ȭ�� ����
		// ���� ������ ������ �߻�!!
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
