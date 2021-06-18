import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Iterator;

public class TransactionThread extends Thread implements Serializable{
	String rcvID;
	int sendValue;
	static MemberInfo memberInfo;
	
	
	TransactionThread(MemberInfo memberInfo){
		System.out.println("Transaction Node Start...");
		this.memberInfo = memberInfo;
	}
	
	public void run() {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			while(true) {

				
				System.out.println(">>Enter ID of MemberNode you want to send to.");
				rcvID = bufferedReader.readLine();
				System.out.println(">>Enter How much Coins you want to send");
				sendValue = Integer.parseInt(bufferedReader.readLine());
				TransactionInfo transactionInfo = new TransactionInfo(rcvID, sendValue);
				boolean flag = send_Transaction(transactionInfo);
				if(flag)
					System.out.println("Transaction Succecced");
				else
					System.out.println("Transaction Failed");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	private static boolean send_Transaction(TransactionInfo transactionInfo) {
		try {
			Socket consortiumSocket = new Socket(memberInfo.getConsortiumIp(), memberInfo.getConsortiumPort());
			ObjectOutputStream out = new ObjectOutputStream(consortiumSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(consortiumSocket.getInputStream());
			
			out.writeObject(transactionInfo);
			
			/*Transaction tr = new Transaction(memberInfo.getId(), 
					transactionInfo.getRcvId(),
					memberInfo.getWallet().publicKey,
					transactionInfo.getFundValue());
					(PublicKey)in.readObject(),*/
					
					
			consortiumSocket.close();
			return (boolean)in.readObject();
			
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		return false;
	}
}
