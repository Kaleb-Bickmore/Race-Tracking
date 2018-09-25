import org.junit.Test;

import static org.junit.Assert.*;

public class RacerTest {

    @Test
    public void update() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        Message updateMessage = new Message("DidNotStart,1,120");
        racer.update(updateMessage);
        assertEquals("120",racer.getLastUpdate());
        assertEquals("DidNotStart",racer.getStatus());
        Message updateMessage3 = new Message("Started,1,220");
        racer.update(updateMessage3);
        assertEquals("220",racer.getLastUpdate());
        assertEquals("Started",racer.getStatus());
        assertEquals("220",racer.getStartTime());
        Message updateMessage4 = new Message("DidNotFinish,1,220");
        racer.update(updateMessage4);
        assertEquals("220",racer.getLastUpdate());
        assertEquals("DidNotFinish",racer.getStatus());
    }

    @Test
    public void getRaceDistance() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("16000",racer.getRaceDistance());
    }

    @Test
    public void getRaceName() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("SomeRace",racer.getRaceName());
    }

    @Test
    public void getStatus() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("Registered",racer.getStatus());
    }

    @Test
    public void getBibNumber() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("1",racer.getBibNumber());
    }

    @Test
    public void getStartTime() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("0",racer.getStartTime());
    }

    @Test
    public void getDistanceCovered() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        Message updateMessage = new Message("OnCourse,1,120,16213");
        racer.update(updateMessage);
        assertEquals("16213",racer.getDistanceCovered());
    }

    @Test
    public void getLastUpdate() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        Message updateMessage = new Message("OnCourse,1,120,16213");
        racer.update(updateMessage);
        assertEquals("120",racer.getLastUpdate());
    }

    @Test
    public void getFinishTime() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        Message updateMessage = new Message("Finished,1,120");
        racer.update(updateMessage);
        assertEquals("120",racer.getFinishTime());

    }

    @Test
    public void getFirstName() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("racer",racer.getFirstName());
    }

    @Test
    public void getLastName() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("McRace",racer.getLastName());
    }

    @Test
    public void getGender() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("Male",racer.getGender());
    }

    @Test
    public void getAge() {
        Message message = new Message("Registered,1,0,racer,McRace,Male,43");
        Message raceMessage = new Message("Race,SomeRace,16000");
        Racer racer = new Racer(message,raceMessage);
        assertEquals("43",racer.getAge());
    }
}