
package simulator;

import java.util.*;

public class Node {
    String nodeId, status;
    Random rand = new Random();
    int numOfFiles, waitTime, timer, fileSize;
    Queue<File> files = new LinkedList<File>();
    File currentFile;
    
    public Node() {
        this("undefined", 0, 0);
    }
    
    public Node(String nodeId, int numOfFiles, int fileSize) {
        status = "waiting";
        waitTime = timer = rand.nextInt(10) + 1;
        this.nodeId = nodeId;
        this.numOfFiles = numOfFiles;
        this.fileSize = fileSize;
        
        for (int x = 0; x < numOfFiles; ++x)
        {
            files.add(new File(nodeId, String.valueOf(x), fileSize));
        }
        
        currentFile = files.remove();
    }
    
    public void sendPacket(Gateway g) {
        if (!currentFile.packets.isEmpty()) {
            g.listen(currentFile.packets.remove());        
        }
        else {
            status = "waiting";
            this.setTimer();
            if (!files.isEmpty()) {
                currentFile = files.remove();
            }
        }
    }
    
    public void changeStatus() {
        status = "sending";
    }
    
    public void listen(Node[] nodes) {
        for (Node n: nodes) {
            if (n.status == "sending" && n != this) {
                this.doubleTimer();
                this.status = "waiting";
                break;
            }
            else {
                this.status = "sending";
            }
        }
    }
    
    public void modifiedListen(Node[] nodes) {
        for (Node n: nodes) {
            if (n.status == "sending" && n != this) {
                
                if (n.currentFile.packets.peek().sequenceNumber < this.currentFile.packets.peek().sequenceNumber) {
                    this.setTimer();
                    this.status = "waiting";
                    break;
                }
            }
            else {
                this.status = "sending";
                n.status = "waiting";
            }
        }
    }
    
    public void setTimer() {
        timer = waitTime;
    }
    
    public void decrementTime() {
        if (timer > 0) {
            --timer;
        }
    }
    
    public void changeStatusToWaiting() {
        status = "waiting";
        if (!this.files.isEmpty()) {
            this.currentFile = this.files.remove();
        }
        this.setTimer();
    }
    
    public void doubleTimer() {
        timer *= 2;
    }
    
}
