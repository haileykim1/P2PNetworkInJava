import java.util.HashMap;

public class node_info {
	//id를 넣으면 ip를 반환
	static HashMap<String,String> node_list = new HashMap();
			
	node_info() {
		
		System.out.println("node_info Start..");
		//MemeberNode [] node_list = new MemberNode[MAX];
	}
	
	public static void enrol(String id, String ip /*Memeber_Node node*/) {

		if(node_list.get(id) == null) {
			node_list.put(id, ip);
			System.out.println("node_info : enrol succuess");

		}
		else {
			System.out.println("node_info : enrol fail : id reduplication");
		}
		
	}
	
	public static String select(String id) {
		
		String ip = node_list.get(id);
		if(ip == null) {

			System.out.println("node_info : select fail : this id has no ip");
			return null;
		}
		System.out.println("node_info : select succuess : " + ip);
		return ip;
	}
	
	public static int delete(String id) {

		String ip = node_list.get(id);
		if(ip == null) {

			System.out.println("node_info : select fail : this id has no ip");
			return -1;
		}
		node_list.remove(id);
		System.out.println("node_info : delete succuess : " + id);
		return 0;
	}
}
