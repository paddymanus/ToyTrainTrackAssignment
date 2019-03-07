import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
// Represents the train track activity in a thread-safe CopyOnWriteArrayList<String>
// called theActivities
// - addMovementTo(<Integer>) adds a train movement (destination) activity to the record
// - addMessage(<String>) adds a message to the record
// - printActivities() display all the activity history of the train movement
// - trackString() takes a snapshot of the traintrack (with trains) for printing
public class Activity {

    private final CopyOnWriteArrayList<String> theActivities;

    private final String[] trainTrack;

    // Constructor for objects of class Activity
    // A reference to the track is passed as a parameter
    public Activity(String[] trainTrack) {
        theActivities = new CopyOnWriteArrayList<>();
        this.trainTrack = trainTrack;
    }

    public void addMovedTo(int section) {
        // add an activity message to the activity history
        String tempString1 = "Train " + trainTrack[section] + " moving/moved to [" + section + "]";
        theActivities.add(tempString1);
        // add the current state of the track to the activity history
        theActivities.add(trackString());
    }// end addMovedTo

    public void addMessage(String message) {
        // add an activity message to the activity history
        String tempString1 = message;
        theActivities.add(tempString1);
    }// end addMessage

    public void printActivities() {
        // print all the train activity history
        System.out.println("TRAIN TRACK ACTIVITY(Tracks [0..16])");
        Iterator<String> iterator = theActivities.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }// end printActivities



    // Utility method to represent the track as a string for printing/display
    public String trackString() {
        String trackStateAsString = "[XX]" + trainTrack[0] + trainTrack[1] + trainTrack[2] + "[XX]" + "\n"
                + trainTrack[11] + "    " + "    " + "    " + trainTrack[3] + "\n"
                + trainTrack[10] + "    " + "[XX]" + trainTrack[17] + trainTrack[4] + trainTrack[18] + "[XX]" + "\n"
                + trainTrack[9] + "    " + trainTrack[16] + "    " + trainTrack[5] + "    " + trainTrack[19] + "\n"
                + "[XX]" + trainTrack[8] + trainTrack[7] + trainTrack[6] + "[XX]" + "    " + trainTrack[20] + "\n"
                + "    " + "    " + trainTrack[15] + "    " + "    " + "    " + trainTrack[21] + "\n"
                + "    " + "    " + "[XX]" +  trainTrack[14] + trainTrack[13] + trainTrack[12] + "[XX]" + "\n";
        return (trackStateAsString);
    }// end trackString


}// end Activity
