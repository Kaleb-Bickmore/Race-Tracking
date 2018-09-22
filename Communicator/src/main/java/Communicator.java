import java.net.DatagramSocket;
import java.net.*;

import static java.nio.charset.StandardCharsets.UTF_16BE;

public class Communicator implements Runnable {

    private DatagramSocket datagramSocket;
    private boolean _keepGoing;
    private IMessageProcessor _processor;

    /**
     * Constructor, which opens an UDP socket on any available port.
     *
     * @throws SocketException  Exception thrown if there is a problem creating the UDP socket
     */
    public Communicator() throws SocketException
    {
        datagramSocket = new DatagramSocket();
    }

    /**
     * Constructor, which opens an UDP socket on a given port.  If that port is being used by another process
     * on the computer, this constructor will throw an exception.
     *
     * @param localPort         Local port to which the UDP socket should be bound
     * @throws SocketException  Exception thrown if there is a problem creating the UDP socket
     */
    public Communicator(int localPort) throws SocketException
    {
        datagramSocket = new DatagramSocket(localPort);
    }

    /**
     * @return                  Get the processor that Communicator is using
     */
    public IMessageProcessor getProcessor() { return _processor; }

    /**
     * @param processor         Set the Communicator's processor
     */
    public void setProcessor(IMessageProcessor processor) { _processor = processor; }

    /**
     * Get the local port to which the Communicator's socket is bound
     * @return                  Returns the port to which the Commuicator's UDP socket is bound. It will return
     *                          0 if there no UDP socket
     */
    public int getLocalPort() {
        return (datagramSocket!=null) ? datagramSocket.getLocalPort() : 0;
    }

    /**
     * Send a message to a target process
     *
     * @param message           Message to send
     * @param targetAddress     IPv4 address of end point where the message needs to be sent
     * @param targetPort        Port of end point where the message needs to be sent
     * @throws Exception        Exception throw is the parameters are invalid or there is socket error
     */
    public void send(String message, InetAddress targetAddress, int targetPort ) throws Exception {
        if (datagramSocket==null) return;

        if (message==null)
            throw new Exception("Cannot send an empty message");

        if (targetAddress ==null)
            throw new Exception("Invalid target address");

        if (targetPort <= 0)
            throw new Exception("Invalid target port");

        byte[] sendData = message.getBytes(UTF_16BE);
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, targetAddress, targetPort);
        datagramSocket.send(packet);
    }

    /**
     * Get a message with 100 ms
     *
     * @return                  A datagram package, which include the received data, the senders address,
     *                          and sender's point
     */
    public DatagramPacket getMessage() throws Exception {
        return getMessage(100);
    }

    /**
     * Get a message (within the specified timeout)
     *
     * @param timeout           Maximum number of milliseconds to wait for a timeout
     * @return                  A datagram package, which include the received data, the senders address,
     *                          and sender's point
     */
    public DatagramPacket getMessage(int timeout) throws Exception
    {
        if (datagramSocket==null) return null;

        datagramSocket.setSoTimeout(timeout);

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = null;
        try
        {
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket.receive(receivePacket);
        }
        catch (SocketTimeoutException err)
        {
            receivePacket = null;
        }

        return receivePacket;
    }

    /**
     * Start this Communicator as an active object.  This create a thread on which the Run methods is executed
     */
    public void start()
    {
        if (_keepGoing) return;

        _keepGoing = true;

        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the Communicator, gracefully
     */
    public void stop() {
        _keepGoing = false;
    }

    /**
     * Close any resources used by this communicator, namely the UDP socket.  After this method is called, the
     * Communicator cannot be used again.
     */
    public void close()
    {
        datagramSocket.close();
        datagramSocket=null;
    }

    /**
     * Run loop for runnable
     */
    public void run()
    {
        while (_keepGoing && datagramSocket!=null)
        {
            DatagramPacket packet = null;
            try { packet = getMessage(); }
            catch (Exception e) { /* ignore */ }
            if (packet == null) continue;

            String message = new String( packet.getData(), 0, packet.getLength(), UTF_16BE);
            InetAddress senderAddress = packet.getAddress();
            int senderPort = packet.getPort();

            if (_processor!=null)
                _processor.process(message, senderAddress, senderPort);
        }
    }

}
