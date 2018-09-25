import java.net.SocketException;


public class Main{
    /**
     * Driver function for the race Server
     * @param args Arguments passed to main
     * @throws SocketException
     */
    public static void main(String[]args) throws SocketException {
        System.out.println("Race Server Started");
        System.out.println("Listening On Port 12000");
        System.out.println("Click the exit button to exit");
        RaceServer raceServer = new RaceServer(12000);
        raceServer.run();
    }
}