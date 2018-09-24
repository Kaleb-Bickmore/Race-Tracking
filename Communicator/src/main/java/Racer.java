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
	 * @param message
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
	 * @param message
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

    public String getRaceDistance() {
        return raceDistance;
    }

    public String getRaceName() {
        return raceName;
    }

    public String getStatus() {
		return status;
	}
	public String getBibNumber() {
		return bibNumber;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getDistanceCovered() {
		return distanceCovered;
	}

	public String getLastUpdate() {
		return  lastUpdate;
	}

	public String getFinishTime() {
		return finishTime;
	}
    public String getFirstName() {
        return firstName;
    }



    public String getLastName() {
        return lastName;
    }


    public String getGender() {
        return gender;
    }


    public String getAge() {
        return age;
    }


}