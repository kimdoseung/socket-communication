/*
 * 접속한 클라이언트간 서로 독립적으로 메시지를 주고 받으려면
 * 하나의 프로그램내에서 독립적으로 수행가능한 실행단위인 
 * 쓰레드가 필요하다!!
 * */
package hostserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//이 클래스는 접속하는 클라이언트마다 1:1 대응하여 대화를 나눌
//아바타와 같다!! 따라서 대화에 필요한 재료인 스트림을 보유해야 하고
//이 스트림은 접속과 함께 생성되는 소켓으로 부터 얻어와야 하기때문에
//소켓도 보유해야 한다..
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
	
	//듣기 
	public void listen() {
		try {
			String msg=buffr.readLine();
			//서버에 접속한 모든 아바타의 send를 호출하자
			for(int i=0; i<hostServer.chatList.size();i++) {
				ChatServerThread cst = hostServer.chatList.get(i);
				cst.send(msg);//클라이언트에 전송
			}
			hostServer.area.append(msg+"\n");//기록 남기기!!
			hostServer.bar.setValue(hostServer.bar.getMaximum());
		} catch (IOException e) {
			System.out.println("클라이언트가 나갔습니다");
			flag = false;
			//벡터에서 제거!!
			hostServer.chatList.remove(this);
			hostServer.area.append("현재 "+hostServer.chatList.size()+"명 남았습니다");
			e.printStackTrace();
		}
	}
	
	//말하기
	public void send(String msg) {
		try {
			//서버에 접속한 모든 아바타의 write를 호출하자
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