
public class main_receive  extends Thread {
	public void run() {
		NetworkEndPoint i = null;
		try {
			i = new NetworkEndPoint();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			while(true) {
				//Thread.sleep(10);
				String s = i.wan_receive(7070);
				System.out.println("--Receive");
				System.out.println(s);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args) throws Exception  {
		// TODO Auto-generated method stub
		main_receive th = new main_receive();
		th.start();
		
		/*NetworkEndPoint i = new NetworkEndPoint();
		while(true) {
			String s = i.receive();
			System.out.println("--Receive");
			System.out.println(s);
	
			}*/
	}

}
