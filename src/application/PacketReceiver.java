package application;

import static application.SocketMessengerConstants.MESSAGE_SEPARATOR;
import static application.SocketMessengerConstants.MESSAGE_SIZE;
import static application.SocketMessengerConstants.MULTICAST_ADDRESS;
import static application.SocketMessengerConstants.MULTICAST_LISTENING_PORT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

public class PacketReceiver implements Runnable{
    private MessageListener listener;
    private MulticastSocket multicastSocket; // socket for receiving msg
    private InetAddress muticastGroup; // inetaddress for multicast group
    private boolean isListening = true;

    public PacketReceiver(MessageListener listener){
        this.listener = listener;

        // connect mulicast socket
        try {
            multicastSocket = new MulticastSocket(MULTICAST_LISTENING_PORT);

            muticastGroup = InetAddress.getByName(MULTICAST_ADDRESS); // get InetAddress of multicast group

            multicastSocket.joinGroup(muticastGroup); // join the group for receving msg

            multicastSocket.setSoTimeout(5000); // timeout if wait more than 5sec
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(isListening){
            byte[] buffer = new byte[MESSAGE_SIZE];

            // datagram packet for incoming msg
            DatagramPacket packet = new DatagramPacket(buffer, MESSAGE_SIZE);

            try {
                multicastSocket.receive(packet); // receives packet
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e){
                e.printStackTrace();
                break;
            }

            String msg = new String(packet.getData());
            msg = msg.trim();

            StringTokenizer tokenizer = new StringTokenizer(msg, MESSAGE_SEPARATOR);
            if(tokenizer.countTokens() == 2){
                listener.messageReceived(tokenizer.nextToken(), tokenizer.nextToken());
            }
        }

        try {
            multicastSocket.leaveGroup(muticastGroup);
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * stop listening for new msg
     */
    public void stopListening() {
        isListening = false;
    }
}
