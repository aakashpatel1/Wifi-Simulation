
package simulator;

import java.util.*;

public class Gateway {
        public Queue<Packet> packets = new LinkedList<Packet>();
        
        public Gateway() {}
        
        public void listen(Packet p) {
            packets.add(p);
        }
        
        public void printPackets() {
            for (Packet p: packets) {
                System.out.println(p.data);
            }
        }
    
}
