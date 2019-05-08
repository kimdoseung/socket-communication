/*������ Ŭ���̾�Ʈ�鿡�� �ڽ��� ȭ���� �Ѹ��� ���� �������
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

//�� Ŭ������ �����ϴ� Ŭ���̾�Ʈ���� 1:1 �����Ͽ� ��ȭ�� ����
//�ƹ�Ÿ�� ����!! ���� ��ȭ�� �ʿ��� ����� ��Ʈ���� �����ؾ� �ϰ�
//�� ��Ʈ���� ���Ӱ� �Բ� �����Ǵ� �������� ���� ���;� �ϱ⶧����
//���ϵ� �����ؾ� �Ѵ�..
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

	

	//������ �޽��� ������ 
	public void send(){
		try {
			ImageIO.write(hostServer.image, "bmp", bos);//�� �̹����� png���Ϸ� ���� �ƿ�ǲ��Ʈ������ ����
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