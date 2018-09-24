import java.net.InetAddress;
import java.util.ArrayList;

public class Client{
	private ArrayList<String> bibsSubscribed;
	private InetAddress clientAddress;
	private Communicator communicator;
	private int port;

	/**
	 * 
	 * @param allRacers
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
     * @param raceMessage
     * @param allRacers
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
	 * @param racer
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
     * @param racers
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
	 * @param racer
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
         * @param racer
         */
	public void unRegister(Racer racer ,  int port) {
        if (racer == null || port != this.port) return;
        this.bibsSubscribed.remove(racer.getBibNumber());
    }

}