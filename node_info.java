import java.util.HashMap;

public class node_info {
	//id�� ������ ip�� ��ȯ
	static HashMap<String,MemberInfo> node_list = new HashMap();
	
			
	node_info() {
		
		System.out.println("node_info Start..");
		//MemeberNode [] node_list = new MemberNode[MAX];
	}
	
	/*public static void enrolConsortium(String id, String ip) {
		if(node_list.get(id) == null) {
			node_list.put(id, ip);
			System.out.println("node_info : enrol succuess");
			
		}
		else {
			System.out.println("node_info : enrol fail : id reduplication");
		}
	}*/
	
	public static void enrol(MemberInfo memberInfo) {

		//Membernode�� ���� ���� �ʿ�. -> �̸�, �ܾ� �� mem
		
		if(node_list.get(memberInfo.getId()) == null) {
			node_list.put(memberInfo.getId(), memberInfo);
			System.out.println("node_info : enrol succuess");
			
		}
		else {
			System.out.println("node_info : enrol fail : id reduplication");
		}
		
	}
	
	public static String select(String id) {
		
		MemberInfo mem = node_list.get(id);
		if(mem == null) {

			System.out.println("node_info : select fail : this id has no ip");
			return null;
		}
		System.out.println("node_info : select succuess : ");
		return mem.getIP();
	}
	
	public static int delete(String id) {

		MemberInfo ip = node_list.get(id);
		if(ip == null) {

			System.out.println("node_info : select fail : this id has no ip");
			return -1;
		}
		node_list.remove(id);
		System.out.println("node_info : delete succuess : " + id);
		return 0;
	}
}
