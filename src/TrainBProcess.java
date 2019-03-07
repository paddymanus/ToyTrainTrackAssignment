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
        theTrack.trainB_MoveAroundToSharedJunction(trainName); // move around B loop
        theTrack.trainB_CrossSharedJunction1(trainName); // move along shared track
        theTrack.trainB_CrossSharedJunction2(trainName); // move along shared track
        theTrack.trainB_MoveAroundToEndTrack(trainName);
        theTrack.trainB_MoveOffTrack(trainName); // move off the track
    } // end run
} // end trainBProcess
