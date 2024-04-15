package application;

public interface MessageListener {
    /**
     * Receives and handle message
     * @param sender source of message
     * @param msg content of the message
     * @param multicast_send_port the sending port for the muticast group
     * @param multicast_listen_port the listening port for the muticast group
     */
    public void messageReceived(String sender, String msg, int multicast_send_port, int multicast_listen_port);
}
