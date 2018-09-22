import java.net.InetAddress;

public interface IMessageProcessor {
    void process(String message, InetAddress address, int port);
}
