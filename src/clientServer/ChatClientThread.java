/*Ŭ���̾�Ʈ�� ��ȭ�� ������*/
package clientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import hostserver.HostServer;



public class ChatClientThread extends Thread{
	ClientServer clientServer;
	Socket client;//��ȭ�� ����
	BufferedReader buffr=null;
	BufferedWriter buffw=null;
	boolean flag = true;
	public ChatClientThread(ClientServer clientServer,Socket client){
		this.clientServer = clientServer;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	//������ �޽��� ������ 
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();//flush() �� ��¿��� �����Ѵ�!!
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//������ �޼��� �ޱ�!! (�Է�)
	public void listen() {
		try {
			String msg=buffr.readLine();
			clientServer.area.append(msg+"\n");
			
		} catch (IOException e) {
			flag = false;
			//���Ϳ��� ����!!
			clientServer.chatlist.remove(this);
			e.printStackTrace();
		}
	}	
	public void run() {
		while(flag) {
			listen();
		}
	}
}
