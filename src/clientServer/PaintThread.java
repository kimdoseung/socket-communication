  /* 클라이언트가 서버의 화면을 받기위한 쓰레드다
 * */
package clientServer;


import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.imageio.ImageIO;

import hostserver.HostServer;

public class PaintThread extends Thread{
	ClientServer clientServer;
	Socket client;//대화용 소켓
	BufferedOutputStream bos=null;
	boolean flag = true;
	
	public PaintThread(ClientServer clientServer,Socket client){
		this.clientServer=clientServer;
		this.client=client;
		try {
			bos=new BufferedOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	//받은화면을 그리는 메서드
	public void Paint() {
		try {
			clientServer.p_screen.getGraphics().drawImage(ImageIO.read(ImageIO.createImageInputStream(client.getInputStream())), 0, 0, 1500, 1000, clientServer.p_screen);
		} catch (IOException e) {
			flag = false;
			//벡터에서 제거!!
			clientServer.list.remove(this);
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(flag) {
			Paint();
		}
	}
}
