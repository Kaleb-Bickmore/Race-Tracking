import java.net.InetAddress;
import java.util.ArrayList;

public class Client{
	private ArrayList<String> bibsSubscribed;
	private InetAddress clientAddress;
	private Communicator communicator;
	private int port;

    /**
     *
     * @param allRacers List of all the registered racers
     * @param communicator The communicator that is used to send messages to the client
     * @param raceMessage the message the simulator sends at the beginning of a race
     * @param clientAddress The address of the client sending a message to the race server
     * @param port the port of the client sending a message to the race server
     */
	public Client(ArrayList<Racer> allRacers, Communicator communicator, Message raceMessage,InetAddress clientAddress, int port) {
	    this.bibsSubscribed = new ArrayList<String>();
		this.clientAddress = clientAddress;
		this.communicator = communicator;
		this.port = port;
		try {
            this.communicator.send(raceMessage.getField(), clientAddress, this.port);
        }
        catch(Exception e){
		    System.out.println("Race not started");
        }
		for(Racer racer : allRacers){
		    try {
                this.communicator.send("Athlete," + racer.getBibNumber() + ","
                        + racer.getFirstName() + "," + racer.getLastName() + "," + racer.getGender() + "," + racer.getAge(), clientAddress, port);
            }
            catch (Exception e){
		        System.out.println(e.getMessage());
            }
		 }
	}

    /**
     *
     * @param raceMessage This is a message that the Simulation sends at the beginning of a race
     * @param allRacers These are all the registered racers
     */
    public void startRace(Message raceMessage, ArrayList<Racer> allRacers){
        try {
            this.communicator.send(raceMessage.getField(), clientAddress, this.port);
        }
        catch(Exception e){
            System.out.println("Race not started");
        }
        for(Racer racer : allRacers){
            try {
                this.communicator.send("Athlete," + racer.getBibNumber() + ","
                        + racer.getFirstName() + "," + racer.getLastName() + "," + racer.getGender() + "," + racer.getAge(), clientAddress, port);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
	/**
	 * 
	 * @param racer This adds a new racer to the Clients list of un-tracked racers
	 */
	public void addNewRacers(Racer racer){
            try {
                this.communicator.send("Athlete," + racer.getBibNumber() + ","
                        + racer.getFirstName() + "," + racer.getLastName() + "," + racer.getGender() + "," + racer.getAge(), clientAddress, port);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    /**
     *
     * @param racers The list of racers that we will use to update all the racers the user is subscribed to
     */
	public void update(ArrayList <Racer> racers) {
        for (String bibs : this.bibsSubscribed){
            for(Racer racer : racers){
                if(racer.getBibNumber().equals(bibs) ){

                    String sendMessage = "Status"+","+bibs+","+racer.getStatus()+","+racer.getStartTime()
                            +","+racer.getDistanceCovered()+","+racer.getLastUpdate()+","+ racer.getFinishTime();
                   try{
                       this.communicator.send(sendMessage,this.clientAddress,this.port);

                   }catch(Exception e){ }
                }
          }

        }
	}

	/**
	 * 
	 * @param racer the racer we are going to give a status update for
     * @param port The port of the Client we want to send a message to
	 */
	public void register(Racer racer , int port) {
        if (racer == null || port != this.port) return;
        bibsSubscribed.add(racer.getBibNumber());
        String sendMessage = "Status" + "," + racer.getBibNumber() + "," + racer.getStatus() + "," + racer.getStartTime()
                + "," + racer.getDistanceCovered()+ "," + racer.getLastUpdate() + "," + racer.getFinishTime();
        try {
            this.communicator.send(sendMessage, this.clientAddress, port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @param racer the racer that the client wishes to stop tracking
     * @param port the port of the client we are looking at
     */
	public void unRegister(Racer racer ,  int port) {
        if (racer == null || port != this.port) return;
        this.bibsSubscribed.remove(racer.getBibNumber());
    }

}