import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.reflect.Array;

public class Racer {

	private String status;
	private String bibNumber;
	private String startTime;
	private String distanceCovered;
	private String lastUpdate;
    private String firstName;
    private String lastName;
    private String gender;
    private String age;
	private String finishTime;
    private String raceName;
    private String raceDistance;

    /**
     *
     * @param message A register message for a new racer to be added
     * @param raceMessage The race message passed from the simulator at the beginning of a race
     */
	public Racer(Message message, Message raceMessage) {

        String[] field = message.getField().split(",");
        String[] raceField = raceMessage.getField().split(",");
        this.status = field[0];
        this.bibNumber = field[1];
        this.startTime = field[2];
        this.distanceCovered = "0";
        this.lastUpdate = field[2];
        this.firstName = field[3];
        this.lastName = field[4];
        this.gender = field[5];
        this.age = field[6];
        this.finishTime = "n/a";
        this.raceDistance = raceField[2];
        this.raceName = raceField[1];
    }

	/**
	 * 
	 * @param message A message sent from the simulator containing some sort of update for the specified racer
	 */

	public void update(Message message) {
        String[] field = message.getField().split(",");
        if (field[1].equals(this.bibNumber)) {
            if (field[0] .equals("DidNotStart") ) {
                this.status = field[0];
                this.lastUpdate = field[2];
            } else if (field[0].equals("Started") ) {
                this.status = field[0];
                this.lastUpdate = field[2];
                this.startTime = field[2];

            } else if (field[0].equals("OnCourse") ) {
                this.status = field[0];
                this.lastUpdate = field[2];
                this.distanceCovered = field[3];

            } else if (field[0].equals("DidNotFinish") ) {
                this.status = field[0];
                this.lastUpdate = field[2];

            } else {
                this.status = field[0];
                this.distanceCovered = this.raceDistance;
                this.lastUpdate = field[2];
                this.finishTime = field[2];
            }
        }
    }

    /**
     *
     * @return the total distance of the race
     */
    public String getRaceDistance() {
        return raceDistance;
    }

    /**
     *
     * @return the name of the race that the racer is in
     */
    public String getRaceName() {
        return raceName;
    }

    /**
     *
     * @return current status of the racer
     */
    public String getStatus() {
		return status;
	}

    /**
     *
     * @return the unique identifier of the racer
     */
	public String getBibNumber() {
		return bibNumber;
	}

    /**
     *
     * @return the time that the racer started at
     */
	public String getStartTime() {
		return startTime;
	}

    /**
     *
     * @return the total distance the racer has gone
     */
	public String getDistanceCovered() {
		return distanceCovered;
	}

    /**
     *
     * @return the last time the racer got an update
     */
	public String getLastUpdate() {
		return  lastUpdate;
	}

    /**
     *
     * @return the time that the racer finished the race
     */
	public String getFinishTime() {
		return finishTime;
	}

    /**
     *
     * @return the first name of the racer
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return the last name of the racer
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return the gender of the racer
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @return the age of the racer
     */
    public String getAge() {
        return age;
    }


}