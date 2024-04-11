package application;

public interface MessageManager {
    /**
     * Connect to the server and route the incoming messages to the listener
     * 
     * @param listener
     */
    public void connect(MessageListener listener, int listen_port);

    /**
     * disconnect from the server and stop routing
     * 
     * @param listener
     */
    public void disconnect(MessageListener listener);

    /**
     * Send the msg to the server
     * 
     * @param from
     * @param msg
     */
    public void sendMessage(String from, String msg, int multicast_send_port, int multicast_listen_port);
}
