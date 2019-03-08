/* Train type B Process class*/
class TrainBProcess extends Thread {
    // Note This process is used to emulate a train as it proceeds around the track

    String trainName;
    TrainTrack theTrack;
    //initialise (constructor)
    public TrainBProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() {   // start train Process
        // wait for clearance before moving on to the track
        theTrack.trainB_MoveOnToTrack(trainName); // move on to track B
        theTrack.trainB_MoveAroundToSharedJunction(trainName); // move around to first junction
        theTrack.trainB_CrossSharedJunction1(trainName); // move across first shared junction
        theTrack.trainB_MoveAroundToEndTrack(trainName); // move around to end of track
        theTrack.trainB_MoveOffTrack(trainName); // move off the track
    } // end run
} // end trainBProcess
