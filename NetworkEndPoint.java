

import java.net.*;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;




//wan port : 30001
//lan port : 7070

public class NetworkEndPoint {
	//return 형식은 보낸 IP/PORT/DATA
	
	static String HeeEul = "---.---.---.---";
	static String YeIn = "---.---.---.---";
	static String SoYang = "--.---.---.---";
	
	static String IP;
	static int snd_port = 30001;
	static int rcv_port = 30001;

	
	NetworkEndPoint() throws Exception {
		System.out.println("NetworkEndPoint start...");
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
	
	@SuppressWarnings("finally")
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
	
	@SuppressWarnings("finally")
	public static Object object_receive(int port) throws Exception {
		Object o = null;
		ServerSocket serverSocket = null;
		Socket socket = null;
		ObjectInputStream in = null;
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			in = new ObjectInputStream(socket.getInputStream());
			o = in.readObject();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
				socket.close();
				serverSocket.close();
				
			} catch (Exception e) {
			}
			return o;
		}
	}
	
	public static void object_send(Object o, int port) throws Exception {
		Socket socket = null;
		ObjectOutputStream out = null;
		try {
			socket = new Socket(IP, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			
			out.writeObject(o);
			System.out.println("Socket : " + socket.getInputStream());
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			try {
			out.flush();
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
