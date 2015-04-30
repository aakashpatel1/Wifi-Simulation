package Simulator.src.simulator;

/**
 * Simulator is used to simulate the flow of packets coming from different nodes. 
 **/
public class Simulator {
    
    /**
     * creates and return a list of nodes. Eventually, the user should be able 
     * to input the number of nodes and the file size distribution. 
     **/    
    public static LinkedList<Node> prepareSimulationEnvironment() {
        int numOfNodes = 10;
        LinkedList<Node> nodes = new LinkedList<Node>();
        for (int x = 0; x < numOfNodes; ++x) {
            nodes.add(new Node(String.valueOf(x), 11, 10));
        }      
        for (Node n: nodes) {
            System.out.println("Node:" + n.nodeId + "NumOfFilesLeft " + n.files.size());
        }
//        for (Node n: nodes) {
//            for (File f: n.files) {
//                f.printFile();
//            }
//        }
        return nodes;
    }
    
    /**
     * SIMULATIONS
     * - first for loop decrements the timer of all nodes or listens to other nodes
     *   if the timer is up.
     * - second for loop determines which packet is going to send if any. 
     **/
    
    /**
     * Original Algorithm uses origin listen function - if someone is already using the channel, perform
     * an exponential backoff by doubling the timer. 
     **/
    public static LinkedList<Node> simulateOriginalAlgorithm(LinkedList<Node> nodes, Gateway g, int time) {
        for (Node n: nodes) {
            if (n.timer != 0) {
                n.decrementTime();
            }
            else {
                n.listen(nodes);
            }
        }
            
        for (Node n: nodes) {
            if (n.status == "sending") {
                if (n.currentFile.packets.isEmpty()) {
                    n.changeStatusToWaiting();
                }
                else {
                    System.out.println("Time: " + time + " Packet: " + n.currentFile.packets.peek().data);
                    n.sendPacket(g);
                    n.status = "sending";
                }
            }
        }
        
        // Removes node after it has nothing to send.
        Queue<Node> finishedNodes = new LinkedList<Node>();
        for (Node n: nodes) {
            if (n.files.isEmpty()) {
                finishedNodes.add(n);
            }
        }
        for (Node n: finishedNodes) {
            nodes.remove(n);
        }
        return nodes;
    }
    
    /**
     * Modified Algorithm
     * uses modified listen - nodes find which packet has the lowest sequence number
     **/
    public static LinkedList<Node> simulateModifiedAlgorithm(LinkedList<Node> nodes, Gateway g, int time) {
        for (Node n: nodes) {
            if (n.timer != 0) {
                n.decrementTime();
            }
            else {
                n.modifiedListen(nodes);
            }
        }
            
        for (Node n: nodes) {
            if (n.status == "sending") {
                if (n.currentFile.packets.isEmpty()) {
                    n.changeStatusToWaiting();
                }
                else {
                    System.out.println("Time: " + time + "Packet: " + n.currentFile.packets.peek().data);
                    n.sendPacket(g);
                    n.status = "sending";
                }
            }
        }
        
        // Removes node after it has nothing to send.
        Queue<Node> finishedNodes = new LinkedList<Node>();
        for (Node n: nodes) {
            if (n.files.isEmpty()) {
                finishedNodes.add(n);
            }
        }
        for (Node n: finishedNodes) {
            nodes.remove(n);
        }
        return nodes;
    }

    public static void main(String[] args) {
        String simulation = "modified"; // determine which algorithm will be used.
        Gateway g = new Gateway();
        int time = 0;
        LinkedList<Node> nodes = prepareSimulationEnvironment();
        
        for (Node n: nodes) {
            System.out.println(n.nodeId + ": " + n.timer);
        }
        
        // while loop simulates one slot of time. It doesn't 
        //stop until 1000 packets have been sent to the
        //gateway. 
        while (g.packets.size() < 1000) {
//            System.out.println("time: " +  time);
            if (simulation == "original") {
                nodes = simulateOriginalAlgorithm(nodes, g, time);
            }
            
            if (simulation == "modified") {
                nodes = simulateModifiedAlgorithm(nodes, g, time);
            }
            
            
                
            ++time;
        }
        
        System.out.println("Gateway Packets");
        g.printPackets();
        
    }
    
}
