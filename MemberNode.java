import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MemberNode {
	//private Block block;
	private static Wallet wallet;
	//여기다 정보 기록하고 brokernode통해 enroll시 이거 넘겨줌.
	//memberinfo에서 wallet 상태도 접근 가능 
	private static MemberInfo memberInfo;
	private static ServerSocket serverSocket = null;
	private static Socket consortiumSocket = null;
	
	
	
	
	public static void main(String[] args) {

		System.out.println("Starting MEMBER NODE...");
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			wallet = new Wallet();
			memberInfo = new MemberInfo(wallet);
			
			//Member정보 MemberInfo에 등록
			System.out.println(">> Enter your PORT Number");
			memberInfo.setPort(Integer.parseInt(bufferedReader.readLine()));
			System.out.println(">> Enter Consortium IP");
			memberInfo.setConsortiumIp(bufferedReader.readLine());
			//Membernode가 컨소시엄과 통신하는 포트는 broker_node포트임.
			System.out.println(">> Enter Consortium Broker Port");
			memberInfo.setConsortiumPort(Integer.parseInt(bufferedReader.readLine()));
			
			//Consortium(broker node)에 자신 등록
			boolean flag = EnrolMemberNode();
			
			if(!flag) {
				System.out.println("MemberNode Enrollment Failed!");
			}
			
			while(flag) {

				//Block 채굴
				
				//이전 해시 값
				String prevHash = "prev";
				int difficulty = 4;
				Block block = new Block(prevHash);
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