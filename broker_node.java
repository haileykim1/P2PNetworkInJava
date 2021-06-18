import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;


public class broker_node extends Thread implements Serializable{

	static node_info node_storage;
	static int my_port;
	static BlockChain chain;
	ServerSocket serverSocket;
	Socket socket;
	ObjectInputStream oin;
	ObjectOutputStream oout;
	static String consortiumName = "";
	static ServerThread serverThread;
	int cnt = 0;

	public void run(){
		System.out.println("Broker Node start...");



		try {




			while(true) {
				serverSocket = new ServerSocket(my_port);
				socket = serverSocket.accept();
				oin = new ObjectInputStream(socket.getInputStream());
				oout = new ObjectOutputStream(socket.getOutputStream());

				//Message format
				//enrol : MemberInfo
				//enrolConsortium : ConsotriumInfo
				//Transaction : type#sender_node_id#receiver_node_id#price
				//BLOCK : Block
				//DELETE : type#sender -> String


				//object���엯 �냼耳� �넻�떊
				Object data = oin.readObject();

				if(data instanceof MemberInfo) {
					System.out.println("Object : Member Enrollment");
					boolean flag = node_storage.enrol((MemberInfo)data);
					oout.writeObject(flag);
				}
				else if(data instanceof Block) {
					System.out.println("Object : Block");
					//Blockchain�뿉 �벑濡�

					oout.writeObject(chain.addBlock((Block)data));
				}


				else if(data instanceof TransactionInfo) {
					System.out.println("Object : TransactionInfo");

					//inner
					//public key瑜� 媛��졇�삤硫� Transaction�쓣 �깮�꽦�븯怨�
					TransactionInfo info = (TransactionInfo) data;


					serverThread.sendMessage(info);

					//inner transaction
					MemberInfo receiver;
					if((node_storage.select(info.getRcvId())) != null) {
						receiver = node_storage.node_list.get(info.getRcvId());
						Transaction tr = receiver.getWallet().sendFunds(receiver.getWallet().publicKey, info.getFundValue());
						chain.queue.get(chain.queuePos).addTransaction(tr);
						System.out.println("Transaction succeed");
						oout.writeObject(true);
					}

				}

				else if(data instanceof Transaction) {
					System.out.println("Object : Transaction");
					NetworkEndPoint i = new NetworkEndPoint();
					Transaction tr = (Transaction) data;
					String receive_node = node_storage.select(tr.recipient_id);
					if(tr.processTransaction() == false) {
						System.err.println("Broker Node : Error : transaction can not verify");
						oout.writeObject(false);
					}
					else {

						serverThread.sendMessage(tr);
						oout.writeObject(true);
					}
				}
				else if(data instanceof String) {
					String str = (String)data;
					if(data.equals("HASH")) {
						System.out.println("Membernode needs prevHash value");
						oout.writeObject(chain.getPreviousHash());
					}else if(data.equals("ID")) {
						System.out.println("Membernode needs ID value");
						oout.writeObject((String)(consortiumName + (++cnt)));
					}else if(data.equals("BLOCK")) {
						//占쏙옙占쏙옙占� 占싹쇽옙占실몌옙 占쏙옙占쏙옙 占쌍깍옙
						if(chain.blockchain.size() == 1) {
							Block block = new Block(chain.getPreviousHash());
							oout.writeObject(block);
						}
						else if(chain.queue.get(chain.queuePos).add_transaction_num == 4) {
							Block block = new Block(chain.getPreviousHash());
							block.transactions = chain.queue.get(chain.queuePos).transactions;
							block.hash = block.calculateHash();
							block.add_transaction_num = 4;
							
							oout.writeObject(block);
							chain.queuePos++;
						} else {
							oout.writeObject(false);
						}
					}
					else {

						System.out.println("Object : String -> DELETE");
						node_storage.delete((String)data);
					}
				}

				else {
					System.out.println("Received Object Unknown");
				}




				oout.close();
				oin.close();
				socket.close();
				serverSocket.close();
			}



		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//broker_node瑜� �깮�꽦�뻽�쓣 �븣遺��꽣 local message ��湲�
	broker_node(node_info node_storage, int my_port, BlockChain chain, String name) {
		this.node_storage = node_storage;
		this.my_port = my_port;
		this.chain = chain;
		this.consortiumName = name;
		this.start();
	}

	@Override
	protected void finalize() throws Throwable{
	}





}