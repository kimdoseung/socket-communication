/*클라이언트의 대화용 쓰레드*/
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
	Socket client;//대화용 소켓
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
	//서버에 메시지 보내기 
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();//flush() 를 출력에만 적용한다!!
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//서버의 메세지 받기!! (입력)
	public void listen() {
		try {
			String msg=buffr.readLine();
			clientServer.area.append(msg+"\n");
			
		} catch (IOException e) {
			flag = false;
			//벡터에서 제거!!
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
