

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//내 공인 IP : 175.208.245.129
//221.167.222.167
//59.13.228.230

public class NetworkEndPoint {
	//return 형식은 보낸 IP/PORT/DATA
	static String MyWanIP;
	
	NetworkEndPoint() throws Exception {
		System.out.println("Connection start...");
	}
	
	public static String wan_receive(int port) throws Exception {
			String msg = null;
			ServerSocket serverSocket = null;
			Socket socket = null;
			try {
				serverSocket = new ServerSocket(port);
				socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				byte arr[] = new byte[100];
				in.read(arr);
				msg = new String(arr);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			finally {
				try {
					socket.close();
					serverSocket.close();
				} catch (Exception e) {
					
				}
				return msg;
			}
			
						
			
	}
	
	//byte 형태로 주고 받음
	public static void wan_send(String s, int port) throws Exception {
		Socket socket = null;
		//System.out.println("1");
		try {
			socket = new Socket(MyWanIP/*"localhost"*/, port);
			OutputStream out = socket.getOutputStream();
			out.write(s.getBytes());
			System.out.println("Socket : " + socket.getInputStream());
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
			socket.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	
	
	public static void pullFromConsortium() {
		
	}
	
	public static void pushToConsortium() {
		
	}
}
