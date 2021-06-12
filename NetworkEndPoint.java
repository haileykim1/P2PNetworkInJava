

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;




//wan port : 30001
//lan port : 7070

public class NetworkEndPoint {
	//return 형식은 보낸 IP/PORT/DATA
	
	static String HeeEul = "175.208.245.129";
	static String YeIn = "221.167.222.167";
	static String SoYang = "59.13.228.230";
	
	static String IP;
	static int snd_port = 30001;
	static int rcv_port = 30001;

	
	NetworkEndPoint() throws Exception {
		System.out.println("Connection start...");
	}
	
	NetworkEndPoint(String ip){
		IP = ip;
	}
	
	private void msg_receive() {
		
		try {
			
			while(true) {

				System.out.println("--Receive");
				String s = wan_receive(rcv_port);
				System.out.println(s);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void set_IP(String s) {
		IP = s;
	}
	
	
	
	private void msg_send(String str) {
		
		try {
			System.out.println("--Send");
			wan_send(str,snd_port);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			socket = new Socket(IP/*"localhost"*/, port);
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
