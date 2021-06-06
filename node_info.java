
public class node_info {
	static String[] node_list;
	static int MAX = 100;
	
	node_info() {
		node_list = new String[MAX];
		//Memeber_Node [] node_list = new Member_Node[MAX];
	}
	
	public static int enrol(String node /*Memeber_Node node*/) {
		for(int i = 0;i < MAX; i++) {
			if(node_list[i] == null) {
				node_list[i] = node;
				return 0;
			}
		}
		System.out.println("node_info Error : node_list is full");
		return -1;
	}
	
	public static String select(String name) {
		for(int i = 0;i < MAX; i++) {
			if(node_list[i] == name) {
				return node_list[i];
			}
		}
		System.out.println("node_info Error : no node in node_list");
		return "null";
	}
	
	public static int delete(String name) {
		for(int i = 0;i < MAX; i++) {
			if(node_list[i] == name) {
				node_list[i] = null;
				return 0;
			}
		}
		System.out.println("node_info Error : no node in node_list");
		return -1;
	}
}
