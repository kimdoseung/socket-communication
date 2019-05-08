/*서버가 클라이언트들에게 자신의 화면을 뿌리기 위한 쓰레드다
 * */
package hostserver;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.imageio.ImageIO;

//이 클래스는 접속하는 클라이언트마다 1:1 대응하여 대화를 나눌
//아바타와 같다!! 따라서 대화에 필요한 재료인 스트림을 보유해야 하고
//이 스트림은 접속과 함께 생성되는 소켓으로 부터 얻어와야 하기때문에
//소켓도 보유해야 한다..
public class ConnectThread extends Thread{
	HostServer hostServer;
	Socket client;
	BufferedOutputStream bos=null;
	boolean flag = true;
	
	public ConnectThread(HostServer hostServer,Socket client) {
		this.hostServer=hostServer;
		this.client=client;
		try {
			bos=new BufferedOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	

	//서버에 메시지 보내기 
	public void send(){
		try {
			ImageIO.write(hostServer.image, "bmp", bos);//그 이미지를 png파일로 소켓 아웃풋스트림으로 쏴줌
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void run() {
		while(flag) {
			send();
		}
	}
}