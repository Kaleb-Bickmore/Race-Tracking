import java.net.SocketException;

public class Main{
    public static void main(String[]args) throws SocketException {
        System.out.println("Race Server Started");
        System.out.println("Listening On Port 12000");
        RaceServer raceServer = new RaceServer(12000);
        raceServer.run();
    }
}