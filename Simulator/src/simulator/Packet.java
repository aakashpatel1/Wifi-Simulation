
package simulator;

/**
*  Packet - stores string data, fileId, and nodeId. 
*  default values:
*      fileId = ""
*      nodeId = ""
*      data = "fileId:0 nodeId:0 true"   
* 
*  NOTE: true is concatenated at the end to simulate an ACK from the gateway. 
*        If a NACK needs to be simulated, change value to false.
*/
public class Packet {
    
    public String data, fileId, nodeId;
    public int sequenceNumber;
    
    public Packet() {
        this("undefined", "undefined", -1);
        
    }
    
    public Packet(String fileIdVal, String nodeIdVal, int sequenceNumberVal) {
        fileId = fileIdVal;
        nodeId = nodeIdVal;
        sequenceNumber = sequenceNumberVal;
        data = "fileId:" + fileId + " nodeId:" + nodeId + " sequenceNumber:" + sequenceNumber + " true" ;
    }
    
}
