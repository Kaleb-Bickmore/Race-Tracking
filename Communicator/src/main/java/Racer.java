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
	public Racer(Message message) throws Exception {

        String[] field = message.getField().split(",");
        if (field[0] == "Registered") {
            this.status = field[0];
            this.bibNumber = field[1];
            this.startTime = field[2];
            this.distanceCovered = "0";
            this.lastUpdate = field[2];
            this.firstName = field[3];
            this.lastName = field[4];
            this.gender = field[5];
            this.age = field[6];
            this.finishTime = "-";
            this.raceDistance = "0";
            this.raceName = "-";
        }
        else{
            throw new Exception("Cannot make class without Registered message from server.");
        }
    }

	/**
	 * 
	 * @param message
	 */

	public void update(Message message) {
        String[] field = message.getField().split(",");
        if (field[0] == "Race") {
            this.raceName = field[1];
            this.raceDistance = field[2];
        }
        if (field[1] == this.bibNumber) {
            if (field[0] == "DidNotStart") {
                this.status = field[0];
                this.lastUpdate = field[2];
            } else if (field[0] == "Started") {
                this.status = field[0];
                this.lastUpdate = field[2];
                this.startTime = field[2];

            } else if (field[0] == "OnCourse") {
                this.status = field[0];
                this.lastUpdate = field[2];
                this.distanceCovered = field[3];

            } else if (field[0] == "DidNotFinish") {
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

	public Boolean getFinished() {
		if(finishTime != "-"){
		    return true;
        }
        else{
            return false;
        }
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