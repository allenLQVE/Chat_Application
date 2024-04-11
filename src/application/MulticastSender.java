package application;

import static application.SocketMessengerConstants.MULTICAST_ADDRESS;
// import static application.SocketMessengerConstants.MULTICAST_LISTENING_PORT;
// import static application.SocketMessengerConstants.MULTICAST_SENDING_PORT;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// the class that send message to all users
public class MulticastSender implements Runnable{

    private byte[] msgData;
    private int send_port;
    private int listen_port;

    public MulticastSender(byte[] bytes, int multicast_send_port, int multicast_listen_port){
        msgData = bytes;
        send_port = multicast_send_port;
        listen_port = multicast_listen_port;
    }

    @Override
    public void run() {
        try {
            // create the socket for sending the msg
            DatagramSocket socket = new DatagramSocket(send_port);
            // DatagramSocket socket = new DatagramSocket(MULTICAST_SENDING_PORT);

            // used the preset InetAddress
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

            // create the packet that contains the msg
            DatagramPacket packet = new DatagramPacket(msgData, msgData.length, group, listen_port);
            // DatagramPacket packet = new DatagramPacket(msgData, msgData.length, group, MULTICAST_LISTENING_PORT);

            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
