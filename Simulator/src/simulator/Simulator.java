
package simulator;

public class Simulator {
    
    public static Node[] prepareSimulationEnvironment() {
        int numOfNodes = 10;
        Node[] nodes =  new Node[numOfNodes];
        for (int x = 0; x < numOfNodes; ++x) {
            nodes[x] = new Node(String.valueOf(x), 10, 10);
        }      
//        for (Node n: nodes) {
//            for (File f: n.files) {
//                f.printFile();
//            }
//        }
        return nodes;
    }
    
    public static void simulateOriginalAlgorithm(Node[] nodes, Gateway g, int time) {
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
    }
    
        public static void simulateModifiedAlgorithm(Node[] nodes, Gateway g, int time) {
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
    }

    public static void main(String[] args) {
        String simulation = "original";
        Gateway g = new Gateway();
        int time = 0;
        Node[] nodes = prepareSimulationEnvironment();
        
        for (Node n: nodes) {
            System.out.println(n.nodeId + ": " + n.timer);
        }
        
        while (g.packets.size() < 1000) {
            
            //System.out.println("time: " +  time);
            if (simulation == "original") {
                simulateOriginalAlgorithm(nodes, g, time);
            }
            
            if (simulation == "modified") {
                simulateModifiedAlgorithm(nodes, g, time);
            }
            
            
                
            ++time;
        }
        
        System.out.println("Gateway Packets");
        //g.printPackets();
        
    }
    
}
