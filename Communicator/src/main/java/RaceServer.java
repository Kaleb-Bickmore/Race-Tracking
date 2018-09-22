import java.net.SocketException;
import java.util.ArrayList;

public class RaceServer extends Communicator {

	private ArrayList<Racer> Racers;
	private ArrayList<Client> Clients;
	private Boolean raceStarted;
	private int port;

	public RaceServer() throws SocketException {
		super();
		raceStarted = Boolean.FALSE;
		Racers = new ArrayList<Racer>();
		Clients = new ArrayList<Client>();
		port = 12000;
	}

	/**
	 * 
	 * @param port
	 */
	public RaceServer(int port) throws SocketException{
		super(port);
		raceStarted = Boolean.FALSE;
		Racers = new ArrayList<Racer>();
		Clients = new ArrayList<Client>();
	}

	/**
	 * 
	 * @param client
	 */
	public void register(Client client) {
		this.Clients.add(client);
	}



	/**
	 * 
	 * @param client
	 */
	public void unregister(Client client) { this.Clients.remove(client); }

	public void run() {
		// TODO - implement RaceServer.run
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param message
	 */
	public void notifyClients(Message message) {
		for (Client client : this.Clients){
			client.update(message);
		}
	}

}