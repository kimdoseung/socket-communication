/*
 * ������ Ŭ���̾�Ʈ�� ���� ���������� �޽����� �ְ� ��������
 * �ϳ��� ���α׷������� ���������� ���డ���� ��������� 
 * �����尡 �ʿ��ϴ�!!
 * */
package hostserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//�� Ŭ������ �����ϴ� Ŭ���̾�Ʈ���� 1:1 �����Ͽ� ��ȭ�� ����
//�ƹ�Ÿ�� ����!! ���� ��ȭ�� �ʿ��� ����� ��Ʈ���� �����ؾ� �ϰ�
//�� ��Ʈ���� ���Ӱ� �Բ� �����Ǵ� �������� ���� ���;� �ϱ⶧����
//���ϵ� �����ؾ� �Ѵ�..
public class ChatServerThread extends Thread{
	HostServer hostServer;
	Socket client;
	BufferedReader buffr;
	BufferedWriter buffw;
	boolean flag = true;
	
	public ChatServerThread(HostServer hostServer,Socket client) {
		this.hostServer=hostServer;
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	//��� 
	public void listen() {
		try {
			String msg=buffr.readLine();
			//������ ������ ��� �ƹ�Ÿ�� send�� ȣ������
			for(int i=0; i<hostServer.chatList.size();i++) {
				ChatServerThread cst = hostServer.chatList.get(i);
				cst.send(msg);//Ŭ���̾�Ʈ�� ����
			}
			hostServer.area.append(msg+"\n");//��� �����!!
			hostServer.bar.setValue(hostServer.bar.getMaximum());
		} catch (IOException e) {
			System.out.println("Ŭ���̾�Ʈ�� �������ϴ�");
			flag = false;
			//���Ϳ��� ����!!
			hostServer.chatList.remove(this);
			hostServer.area.append("���� "+hostServer.chatList.size()+"�� ���ҽ��ϴ�");
			e.printStackTrace();
		}
	}
	
	//���ϱ�
	public void send(String msg) {
		try {
			//������ ������ ��� �ƹ�Ÿ�� write�� ȣ������
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		while(flag) {
			listen();
		}
	}
}