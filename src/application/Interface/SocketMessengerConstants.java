package application.Interface;

public interface SocketMessengerConstants {

    // address for multicast datagrams
    public static final String MULTICAST_ADDRESS = "239.0.0.1"; // address for testing

    // port for Socket connections to the server
    public static final int SERVER_PORT = 12345; // for testing

    // message size in bytes
    public static final int MESSAGE_SIZE = 512;

    public static final String MESSAGE_SEPARATOR = ">>>";

    public static final String DISCONNECT_STRING = "disconnect";
}
