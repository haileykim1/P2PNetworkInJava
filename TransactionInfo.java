
public class TransactionInfo {
	private String rcvID;
	private int fundValue;
	//�ʿ��ϸ� �߰�
	
	TransactionInfo(String id, int value){
		this.rcvID = id;
		this.fundValue = value;
	}
	
	public String getRcvId(){
		return rcvID;
	}
	
	public int getFundValue() {
		return fundValue;
	}
}
