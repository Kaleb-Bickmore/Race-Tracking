import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import static java.nio.charset.StandardCharsets.UTF_16BE;

/**
 * Users A communicator to speak with the simulator and multiple clients.
 */
public class RaceServer {
    private Communicator communicator;
    private ArrayList<Racer> racers;
    private ArrayList<Client> clients;
    private Message raceMessage;
    private Boolean done;

    /**
     * Constructor
     * @throws SocketException
     */
    public RaceServer() throws SocketException {
        this.racers = new ArrayList<Racer>();
        this.clients = new ArrayList<Client>();
        this.communicator = new Communicator();
        done = false;
    }

    /**
     * Overloaded Constructor for setting the port
     * @param port the port that the communicator will be established at
     * @throws SocketException
     */
    public RaceServer(int port) throws SocketException {
        this.racers = new ArrayList<Racer>();
        this.clients = new ArrayList<Client>();
        this.communicator = new Communicator(port);
        done = false;
    }

    /**
     *
     * @return the racers that have been registered
     */
    public ArrayList<Racer> getRacers() {
        return racers;
    }

    /**
     *
     * @return the message the simulator sends at the beginning of a race
     */
    public Message getRaceMessage() { return raceMessage; }

    /**
     *
     * @param raceMessage the message the simulator sends at the beginning of a race
     */
    public void setRaceMessage(Message raceMessage) {
        this.raceMessage = raceMessage;
    }

    /**
     *
     * @return the clients that have registered to the race server
     */
    public ArrayList<Client> getClients() {
        return clients;
    }

    /**
     *
     * @return the port that the communicator is established on
     */
    public int getLocalPort(){
        return this.communicator.getLocalPort(); }

    /**
     * @param client A client that has sent a hello message and wishes to track the race
     */
    public void register(Client client) {
        this.clients.add(client);
    }


    /**
     * notifies the clients of changes within the race
     */
    private void notifyClients() {

        for (Client client : this.clients) {
            client.update(this.racers);
        }
    }

    /**
     * this function continues to run endlessly watching for messages sent to the communicators port.
     * Depending on the message sent, we can add clients, start the race, subscribe or unsubscribe
     * a client to a racer, or update the clients on their current subscribed racers.
     */
    public void run() {
        while (!done) {
            DatagramPacket packet = null;
            try {
                packet = this.communicator.getMessage();
            } catch (Exception e) { /* ignore */ }
            if (packet == null) continue;
            String message = new String(packet.getData(), 0, packet.getLength(), UTF_16BE);
            System.out.println(message);
            int senderPort = packet.getPort();
            InetAddress senderAddress = packet.getAddress();
            Message messageToParse = new Message(message);
            // Start the race
            if(messageToParse.getType().equals("Race")){
                this.raceMessage = messageToParse;
                for (Client client : this.clients){
                    client.startRace(raceMessage,this.racers);
                }

            }
            // add a new client
            else if (message.equals("Hello")) {
                Client client = new Client(this.racers, this.communicator, this.raceMessage, senderAddress,senderPort);
                this.register(client);
            }
            // Subscribe a client to a particular racer
            else if (messageToParse.getType().equals("Subscribe")) {
                Racer racerToAdd = null;
                for (Racer racer : this.racers) {
                    if (racer.getBibNumber().equals(messageToParse.getField().split(",")[1])) {
                        racerToAdd = racer;
                    }

                }
                for (Client client : this.clients) {
                    client.register(racerToAdd, senderPort);
                }
            }
            //Unsubscribe a client from a particular racer
            else if (messageToParse.getType().equals("Unsubscribe")) {
                Racer racerToAdd = null;
                for (Racer racer : this.racers) {
                    if (racer.getBibNumber() == messageToParse.getField().split(",")[1]) {
                        racerToAdd = racer;
                    }

                }
                for (Client client : this.clients) {
                    client.unRegister(racerToAdd, senderPort);
                }
            }
            // Update racer information and update the clients on their subscribed racers
            else {
                this.updateRacers(messageToParse);
                this.notifyClients();

            }
        }
    }

    /**
     *
     * @param message a register message to add a new racer to the race
     */
    public void updateRacers(Message message) {
        if (message.getField().split(",")[0].equals("Registered")) {
            Racer newRacer = new Racer(message,this.raceMessage);
            this.racers.add(newRacer);
            for (Client client : this.clients) {
                client.addNewRacers(newRacer);
            }

        }
        else {
            for (Racer racer : this.racers) {
                racer.update(message);
            }
        }

    }
}