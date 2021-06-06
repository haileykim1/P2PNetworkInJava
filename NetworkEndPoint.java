

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//내 공인 IP : 175.208.245.129

public class NetworkEndPoint {
	//return 형식은 보낸 IP/PORT/DATA
	static String MyWanIP;
	
	NetworkEndPoint() throws Exception {
		try {

            String url = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=%EB%82%B4+%EA%B3%B5%EC%9D%B8+ip&oquery=%EB%82%B4+ip&tqi=h7VLTlp0J14ssdCf%2BZ4ssssss4l-262799";
            		
            Connection conn = Jsoup.connect(url);
            Document html = conn.get();
            Elements fileblocks = html.getElementsByClass("ip_chk_box"); //ip_chk_wrap
            for(Element fileblock : fileblocks) {
            	String text = fileblock.text();
            	MyWanIP = text;
            }
            
            
            System.out.println("MyWanIP : " + MyWanIP);

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
			
				
				
			/*DatagramSocket receive_Socket = new DatagramSocket(7070);
			System.out.println("Waiting for Receive Port" + receive_Socket);
			
			DatagramPacket receive_Packet = new DatagramPacket(new byte[1024], 1024);
			receive_Socket.receive(receive_Packet);
			InetAddress IPAddress = receive_Packet.getAddress();
			int receive_Port = receive_Packet.getPort();
			String data = new String(receive_Packet.getData());
			System.out.println("IP:  " + IPAddress + ", PORT: " + receive_Port);
			System.out.println("Message : " + data);
			
			String message = IPAddress + "/" + receive_Port + "/" + data;
			receive_Socket.close();
			
			return message;*/
			
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
		/*
		byte[] sendData = s.getBytes();
		DatagramSocket send_Socket = new DatagramSocket();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(MyWanIP), 7070);
		
		send_Socket.send(sendPacket);
		send_Socket.close();
		*/
	}
	
	public static void lan_send(String s, int port) throws Exception {
		Socket socket = null;
		//System.out.println("1");
		try {
			socket = new Socket("localhost", port);
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
		
		//send_Socket.send(sendPacket);
		//send_Socket.close();
	}
	
	public static void pullFromConsortium() {
		
	}
	
	public static void pushToConsortium() {
		
	}
}
