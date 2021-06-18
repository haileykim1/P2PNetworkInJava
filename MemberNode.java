import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;

public class MemberNode implements Serializable{
	//private Block block;
	private static Wallet wallet;
	//여기다 정보 기록하고 brokernode통해 enroll시 이거 넘겨줌.
	//memberinfo에서 wallet 상태도 접근 가능 
	private static MemberInfo memberInfo;
	private static ServerSocket serverSocket = null;
	private static Socket consortiumSocket = null;
	private static String ID = null;
	private static TransactionThread transactionThread;
	
	
	public static void main(String[] args) {

    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		System.out.println("Starting MEMBER NODE...");
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			

			wallet = new Wallet();
			memberInfo = new MemberInfo(wallet);
			
			//자신의 IP 등록
			String local = new String(InetAddress.getLocalHost().getAddress());
			memberInfo.setIP(local);
			
			//Member정보 MemberInfo에 등록
			System.out.println(">> Enter your PORT Number");
			memberInfo.setPort(Integer.parseInt(bufferedReader.readLine()));
			System.out.println(">> Enter Consortium IP");
			memberInfo.setConsortiumIp(bufferedReader.readLine());
			//Membernode가 컨소시엄과 통신하는 포트는 broker_node포트임.
			System.out.println(">> Enter Consortium Broker Port");
			memberInfo.setConsortiumPort(Integer.parseInt(bufferedReader.readLine()));
			
			//변수 둬서 나중에 생성
			//ID등록
			ID = getID();
			wallet.set_id(ID);
			memberInfo.setId(ID);
			
			//Consortium(broker node)에 자신 등록
			boolean flag = EnrolMemberNode();
			
			if(!flag) {
				System.out.println("MemberNode Enrollment Failed!");
			}
			
			transactionThread = new TransactionThread(memberInfo);
			transactionThread.start();
			while(flag) {

				//Block 채굴
				
				//이전 해시 값
				String prevHash = getPrevHash();
<<<<<<< HEAD
				int difficulty = 4;
=======

				
>>>>>>> bb610437cce899f0d5001d285346e95c4d65565a
				
				//Consortium으로부터 완성된 블록값 받아옴.
				consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
				ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
				
				out.writeObject("BLOCK");
				if(in.readObject() instanceof Boolean) {
					continue;
				}
					
				Block block = (Block)in.readObject();
<<<<<<< HEAD
=======

				int difficulty = 4;
>>>>>>> bb610437cce899f0d5001d285346e95c4d65565a
				block.mineBlock(difficulty);
				
				
				//채굴 완료 후 Consortium node(broker node)에 등록
				//블록 채굴 시 오래걸릴 수 있으므로 등록 시만 소켓 열었다 닫기
				boolean isBlockEnrolled = new MemberNode().EnrolBlock(block);
				
				if(isBlockEnrolled) {
					System.out.println("Block Successfully Enrolled!");
				}else {
					System.out.println("Block Enrollment Failed!");
				}
			}
			
			
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	


	//Transaction 보내는 함수 - 활용안했습니다! 활용할 부분에 사용하면 될듯!
	private static boolean send_Transaction(float value) {
		try {
			consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
			ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
			
			Transaction tr = wallet.sendFunds(wallet.publicKey, value);
			
			out.writeObject(tr);
			
			return (boolean)in.readObject();
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			try {
				
				consortiumSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return false;
	}

	private static String getID() {
		String ret = "";
		try {
			consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
			ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
			
			out.writeObject("ID");
			
			
			ret = (String)in.readObject();
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			try {
				
				consortiumSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
		
	}
	private static String getPrevHash() {
		String ret = "";
		
		try {
			consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
			ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
			
			out.writeObject("HASH");
			
			ret = (String)in.readObject();
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			try {
				
				consortiumSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ret;
	}
	
	private static boolean EnrolMemberNode() {
		
		try {
			consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
			ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
			
			out.writeObject(memberInfo);
			
			return (boolean)in.readObject();
			
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			try {
				
				consortiumSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return false;
	}
	
	private boolean EnrolBlock(Block block) {
		
		try {
			consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
			ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
			
			out.writeObject(block);
			
			return (boolean)in.readObject();
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			
			try {
				consortiumSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
}

