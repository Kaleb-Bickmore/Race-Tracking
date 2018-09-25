import org.junit.Test;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RaceServerTest {

    @Test
    public void testConstructor() throws SocketException {
        RaceServer raceServer = new RaceServer(12003);
        assertEquals(12003,raceServer.getLocalPort());
    }



    @Test
    public void getRacers() throws SocketException {
        RaceServer raceServer = new RaceServer();
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        raceServer.setRaceMessage(raceMessage);
        raceServer.updateRacers(message);
        assertEquals("racer",raceServer.getRacers().get(0).getFirstName());
    }

    @Test
    public void getClients() throws SocketException, UnknownHostException {
        RaceServer raceServer = new RaceServer();
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Register,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        raceServer.register(client);
        assertEquals(client,raceServer.getClients().get(0));
    }

    @Test
    public void updateRacers() throws UnknownHostException, SocketException {
        RaceServer raceServer = new RaceServer();
        Communicator comm1 = new Communicator();
        Communicator comm2 = new Communicator();
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        ArrayList<Racer> allRacers = new ArrayList<Racer>();
        Client client = new Client(allRacers,comm1,raceMessage, InetAddress.getLocalHost(),comm2.getLocalPort());
        raceServer.setRaceMessage(raceMessage);
        raceServer.register(client);
        raceServer.updateRacers(message);
        System.out.println(raceServer.getRacers());
        assertEquals(raceServer.getRacers().get(0).getStatus(),"Registered");
        Message updateMessage4 = new Message("DidNotFinish,1,220");
        raceServer.updateRacers(updateMessage4);
        assertEquals(raceServer.getRacers().get(0).getStatus(),"DidNotFinish");

    }
    @Test
    public void getRaceMessage() throws SocketException {
        RaceServer raceServer = new RaceServer();
        Message raceMessage = new Message("Race,SomeRace,16000");
        raceServer.setRaceMessage(raceMessage);
        assertEquals(raceServer.getRaceMessage(),raceMessage);
    }

}