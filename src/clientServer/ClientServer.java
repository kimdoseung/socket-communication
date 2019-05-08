/*ȣ��Ʈ������ ������ Ŭ���̾�Ʈ�ε� �굵 ȭ�����ۿ����� ���������� �����־ Ŭ���̾�Ʈ������� ������*/
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
	ServerSocket server;// ������ ������ ����
	int port;
	Socket client;// ��ȭ�� ����
	
	//����������� ��ü 
	Thread paintServerThread; //�������� ������ �޾Ƶ��̴� ���ϼ��� 
	PaintThread paintThread; //ȭ��޾ƿ°� �׸��� ������
	
	
	//Ŭ���̾�Ʈ �� ���� ��ü 
	Thread connectThread; //������ ���� ������
	ChatClientThread chatClientThread;//��ȭ�� ������
	
	Vector<PaintThread> list = new Vector(); 
	Vector<ChatClientThread> chatlist = new Vector(); 
	

	
	public ClientServer() {

		p_east = new JPanel();
		p_screen = new JPanel();
		p_con = new JPanel();
		p_chat = new JPanel();

		t_ip = new JTextField(8);
		PromptSupport.setPrompt("ip�Է�", t_ip); 

		bt = new JButton("����");

		area = new JTextArea();
		area.setEnabled(false);
		scroll = new JScrollPane(area);
		t_input = new JTextField();

		area.setFont(new Font("����", Font.BOLD, 15));
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
		
		// ��ư�� ������ ����
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
	 ȭ�� ������ ���� ���� ���� �޼��� 
	-------------------------------------------------------------------------*/
	public void runPaintServer() {

		try {
			server = new ServerSocket(1188);// ���� ����

			while (true) {
				Socket client = server.accept();// ���� ����(�����ڸ� ��ٸ�)
				PaintThread paintThread = new PaintThread(this, client);
				list.add(paintThread);
				paintThread.start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*-------------------------------------------------------------------------
	ä���� ���� ���� ���� �޼��� 
	-------------------------------------------------------------------------*/
	public void connectServer() {
		
		port = 1187;
		String ip = t_ip.getText();
		// ���� ������ ������ �߻�!!
		try {
			client = new Socket(ip, port);
			// ��ȭ�� ������ ����!!!
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
