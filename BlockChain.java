import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;

public class BlockChain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
    public static float minimumTransaction = 0.1f;
    public static int difficulty = 3;
    public static Transaction genesisTransaction;


    public BlockChain(){
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            KeyPairGenerator keyGen2 = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random2 = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec2 = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec2, random2); //256
            KeyPair keyPair2 = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            PrivateKey privateKey2 = keyPair.getPrivate();
            PublicKey publicKey2 = keyPair.getPublic();

            genesisTransaction = new Transaction(publicKey, publicKey2, 100f, null);
            genesisTransaction.generateSignature(privateKey);
            genesisTransaction.transactionId = "0";
            genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId));
            UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
            
            /*String timeStr = Long.toString(new Date().getTime());
            if(timeStr.length() > 9) {
            	timeStr = timeStr.substring(0, 9);
            }*/
            Block genesisBlock = new Block("0", 1623486340);
            genesisBlock.addTransaction(genesisTransaction);
            addBlock(genesisBlock);

        }catch(Exception e){
            throw new RuntimeException(e);
        }


    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public static boolean addBlock(Block newBlock) {
        //newBlock.mineBlock(difficulty);
        if(blockchain.get(-1).nonce == newBlock.nonce) {
        	return false;
        }else {
        	blockchain.add(newBlock);
        	return true;
        }
    }

    public static String getPreviousHash(){
        return blockchain.get(-1).hash;
    }
    public static void setDifficulty(int difficulty){
        difficulty = difficulty;
    }

}
