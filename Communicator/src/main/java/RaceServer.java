import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_16BE;

public class RaceServer {
    private Communicator communicator;
    private ArrayList<Racer> racers;
    private ArrayList<Client> clients;
    private Message raceMessage;
    private Boolean done;

    public RaceServer() throws SocketException {
        this.racers = new ArrayList<Racer>();
        this.clients = new ArrayList<Client>();
        this.communicator = new Communicator();
        done = false;
    }
    public RaceServer(int port) throws SocketException {
        this.racers = new ArrayList<Racer>();
        this.clients = new ArrayList<Client>();
        this.communicator = new Communicator(port);
        done = false;
    }
    public ArrayList<Racer> getRacers() {
        return racers;
    }

    public Message getRaceMessage() { return raceMessage; }

    public void setRaceMessage(Message raceMessage) {
        this.raceMessage = raceMessage;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }
    public int getLocalPort(){
        return this.communicator.getLocalPort(); }

    /**
     * @param client
     */
    public void register(Client client) {
        this.clients.add(client);
    }





    /**
     * @param message
     */
    private void notifyClients(Message message) {

        for (Client client : this.clients) {
            client.update(this.racers);
        }
    }

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
            if(messageToParse.getType().equals("Race")){
                this.raceMessage = messageToParse;
                for (Client client : this.clients){
                    client.startRace(raceMessage,this.racers);
                }

            }
            else if (message.equals("Hello")) {
                Client client = new Client(this.racers, this.communicator, this.raceMessage, senderAddress,senderPort);
                this.register(client);
            }
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
            else {
                this.updateRacers(messageToParse);
                this.notifyClients(messageToParse);

            }
        }
    }

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