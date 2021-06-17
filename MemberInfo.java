
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
	
	//wallet.getBalance 통해 잔액 접근(wallet 내부구현? 아님 memberinfo 구현?)
	public float getWalletBalance() {
		return wallet.getBalance();
	}
	
}
