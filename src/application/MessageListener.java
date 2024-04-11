package application;

public interface MessageListener {
    public void messageReceived(String sender, String msg, int multicast_send_port, int multicast_listen_port);
}
