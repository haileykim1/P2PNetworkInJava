import java.io.Serializable;

public class TransactionInfo implements Serializable{
	private String rcvID;
	private float fundValue;
	//�ʿ��ϸ� �߰�
	
	TransactionInfo(String id, float value){
		this.rcvID = id;
		this.fundValue = value;
	}
	
	public String getRcvId(){
		return rcvID;
	}
	
	public float getFundValue() {
		return fundValue;
	}
}
