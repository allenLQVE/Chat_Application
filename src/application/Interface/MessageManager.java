package application.Interface;

import java.io.IOException;

public interface MessageManager {
    /**
     * Connect to the server and route the incoming messages to the listener
     * 
     * @param listener
     * @param listen_port
     * @throws IOException 
     */
    public void connect(MessageListener listener, int listen_port) throws IOException;

    /**
     * disconnect from the server and stop routing
     * 
     * @param listener
     */
    public void disconnect(MessageListener listener);

    /**
     * Send the msg to the server, and msg pushed from server to users
     * 
     * @param from
     * @param msg
     * @param multicast_send_port
     * @param multicast_listen_port
     */
    public void sendMessage(String from, String msg, int multicast_send_port, int multicast_listen_port);
}
