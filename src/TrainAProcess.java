/* Train type A Process class*/
class TrainAProcess extends Thread {
    // Note This process is used to emulate a train as it proceeds around the track

    String trainName;
    TrainTrack theTrack;
    //initialise (constructor)
    public TrainAProcess(String trainName, TrainTrack theTrack) {
        this.trainName = trainName;
        this.theTrack = theTrack;
    }

    @Override
    public void run() {   // start train Process
        // wait for clearance before moving on to the track
        theTrack.trainA_MoveOnToTrack(trainName); // move on to track A
        theTrack.trainA_MoveAroundToSharedJunction(trainName); // move around to first junction
        theTrack.trainA_CrossSharedJunction1(trainName); // move across shared junction 1
        theTrack.trainA_MoveAroundToEndTrack(trainName); // move to end of track
        theTrack.trainA_MoveOffTrack(trainName); // move off the track */
    } // end run

} // end trainAProcess
