
package simulator;

import java.util.*;

public class Node {
    //INITIALIZATION LIST 
    String nodeId, status;
    Random rand = new Random();
    int numOfFiles, waitTime, timer, fileSize; 
    Queue<File> files = new LinkedList<File>();
    File currentFile;
    
    //Default Constructor sets nodeID to undefined, and num of packets and packet sizes to 0
    public Node() {
        this("undefined", 0, 0);
    }
    
    //Contstructor which populates files for the node with a set size passed 
    //in by whoever is calling constructor
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
    
    // Sends packet to a gateway
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
    
    // changes the status of the node to sending
    public void changeStatus() {
        status = "sending";
    }
    
    //listens to other nodes on network to see whether they are sending or not
    // if they are not sending, it changes its status to sending
    public void listen(LinkedList<Node> nodes) {
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
    
    // checks other nodes on network to see whether the sender's packet has a 
    //lower sequence number than this.packet.sequencenumber
    public void modifiedListen(LinkedList<Node> nodes) {
//        System.out.println("MODIFIED LISTEN CALLED ON " + this.nodeId + " = THIS");
        for (Node n: nodes) {
//            System.out.println("N= " + n.nodeId);
            if (n.status == "sending" && n != this) {
                //if the sequence number of this.packet if less than the packet which 
                //is currently in sending mode
                if(n.currentFile.packets.peek() != null && this.currentFile.packets.peek() != null) {
                    if (n.currentFile.packets.peek().sequenceNumber > this.currentFile.packets.peek().sequenceNumber) {
                        //this.packet sequence number begins sending and the comparison node
                        //must go to waiting mode.
//                        System.out.println("Nodeid= " + n.nodeId + " this.currentFile.packets.peek().sequenceNumber" + n.currentFile.packets.peek().sequenceNumber);
//                        System.out.println("Nodeid= " + this.nodeId + " this.currentFile.packets.peek().sequenceNumber= " + this.currentFile.packets.peek().sequenceNumber);
                        this.status = "sending";
                        n.status = "waiting";
//                        System.out.println("This Nodeid" + this.nodeId + "status is now" + this.status);
//                        System.out.println("N Nodeid" + n.nodeId + "status is now " + n.status);
                        n.setTimer();
                    }
                    else {
                        //the currently sending node remains sending and this resets timer and continues to wait
                        this.setTimer();
                        this.status = "waiting";
//                        System.out.println("Nodeid= " + n.nodeId + " this.currentFile.packets.peek().sequenceNumber" + n.currentFile.packets.peek().sequenceNumber);
//                        System.out.println("Nodeid= " + this.nodeId + " this.currentFile.packets.peek().sequenceNumber= " + this.currentFile.packets.peek().sequenceNumber);
//                        System.out.println("Node Id :" + n.nodeId + " remains " + n.status);
//                        System.out.println("Node Id :" + this.nodeId + " remains " + this.status);
//                        System.out.println("Break Listen Function");
                        break;
                    }
                }
            }
            else {
//                System.out.println("Node Id :" + n.nodeId + " remains " + n.status);
//                System.out.println("Node Id :" + this.nodeId + " remains " + this.status);
                this.status = "sending";
                if (this != n){
                    n.status = "waiting";
                }
            }
        }
        for (Node n: nodes) {
            if (n.status ==  "sending") {
//                System.out.println("Node Id :" + n.nodeId + " is " + n.status + ". They should be sending a packing anytime now.");
            }
        }
//        System.out.println("(THIS)Node Id :" + this.nodeId + " is " + this.status);
    }
    
    //reset timer to the waitTime
    public void setTimer() {
        timer = waitTime;
    }
    
    //timer countdown
    public void decrementTime() {
        if (timer > 0) {
            --timer;
        }
    }
    
    //changes status to waiting
    public void changeStatusToWaiting() {
        status = "waiting";
        if (!this.files.isEmpty()) {
            this.currentFile = this.files.remove();
        }
        this.setTimer();
    }
    
    //exponential backoff
    public void doubleTimer() {
        timer *= 2;
    }
    
}
