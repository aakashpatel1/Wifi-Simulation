
package simulator;

import java.util.*;
/**
 * File - stores a collection of Packets as well as fileId and fileSize.
 */
public class File {
    
    String fileId;
    String nodeId;
    int fileSize;
    public Queue<Packet> packets = new LinkedList<Packet>();
    
    public File() {
        this("undefined", "undefined", 0);
    }
    
    public File(String nodeId, String fileId, int fileSize) {
        this.fileId = fileId;
        this.nodeId = nodeId;
        this.fileSize = fileSize;
        
        for (int x = 0; x < fileSize; ++x) {
            packets.add(new Packet(fileId, nodeId, x));
        }
    }
    
    public void printFile() {
        for (Packet p: packets) {
            System.out.println(p.data);
        }
    }
    
}
