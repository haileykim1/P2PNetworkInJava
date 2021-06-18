
public class MemberInfo {
	private Wallet wallet;
	private String id;
	private String ip;
	private String name;
	private int port;
	private String consortiumIp;
	private int consortiumPort;
	
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
	
	
	public int getPort() {
		return port;
	}
	
	public String getConsortiumIp() {
		return consortiumIp;
	}
	
	public int getConsortiumPort() {
		return consortiumPort;
	}
	
	//필요하면 Wallet getter더 구현하기
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setConsortiumIp(String consortiumIp) {
		this.consortiumIp = consortiumIp;
	}
	
	public void setConsortiumPort(int consortiumPort) {
		this.consortiumPort = consortiumPort;
	}
	
}
