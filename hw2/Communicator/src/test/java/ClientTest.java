import org.junit.Test;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_16BE;
import static org.junit.Assert.*;

public class ClientTest {

    @Test
    public void startRace() throws SocketException, UnknownHostException {
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Register,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        client.startRace(raceMessage,allRacers);
        allRacers.add(racer);
        client.startRace(raceMessage,allRacers);
        DatagramPacket packet = null;
        try {
            packet = comm2.getMessage();
            packet = comm2.getMessage();

        } catch (Exception e) { /* ignore */ }

        String newMessage = new String(packet.getData(), 0, packet.getLength(), UTF_16BE);
        assertEquals(newMessage, raceMessage.getField());

    }

    @Test
    public void addNewRacers() throws SocketException, UnknownHostException {
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Register,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        client.addNewRacers(racer);
        DatagramPacket packet = null;
        try {
            packet = comm2.getMessage();
            packet = comm2.getMessage();

        } catch (Exception e) { /* ignore */ }

        String newMessage = new String(packet.getData(), 0, packet.getLength(), UTF_16BE);
        assertEquals(newMessage, "Athlete,1,racer,McRace,Male,43");
    }

    @Test
    public void update() throws SocketException, UnknownHostException {
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Register,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        allRacers.add(racer);
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        client.register(racer,comm2.getLocalPort());
        Message updateMessage2 = new Message("Started,1,220");
       for(Racer racers : allRacers) {
           racers.update(updateMessage2);
       }
        client.update(allRacers);
        DatagramPacket packet = null;
        try {
            packet = comm2.getMessage();
            packet = comm2.getMessage();
            packet = comm2.getMessage();
            packet = comm2.getMessage();



        } catch (Exception e) { /* ignore */ }

        String newMessage = new String(packet.getData(), 0, packet.getLength(), UTF_16BE);
        assertEquals(newMessage, "Status,1,Started,220,0,220,n/a");
    }

    @Test
    public void register() throws SocketException, UnknownHostException {
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Register,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        allRacers.add(racer);
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        client.register(racer,comm2.getLocalPort());
        DatagramPacket packet = null;
        try {
            packet = comm2.getMessage();
            packet = comm2.getMessage();
            packet = comm2.getMessage();



        } catch (Exception e) { /* ignore */ }

        String newMessage = new String(packet.getData(), 0, packet.getLength(), UTF_16BE);
        assertEquals(newMessage, "Status,1,Register,0,0,0,n/a");
    }

    @Test
    public void unRegister() throws SocketException, UnknownHostException {
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Register,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        allRacers.add(racer);
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        client.unRegister(racer,comm2.getLocalPort());
        client.unRegister(null,comm2.getLocalPort());
    }
}