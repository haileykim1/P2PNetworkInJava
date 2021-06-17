
public class MemberInfo {
	private Wallet wallet;
	private String id;
	private String ip;
	private String name;
	
	MemberInfo(Wallet wallet){
		this.wallet = wallet;
	}
	
	public String getId() {
		return id;
	}
	
	public String getIP() {
		return ip;
	}
	
	public String getName() {
		return name;
	}
	
	//wallet.getBalance ���� �ܾ� ����(wallet ���α���? �ƴ� memberinfo ����?)
	public float getWalletBalance() {
		return wallet.getBalance();
	}
	
}
