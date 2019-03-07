import java.util.concurrent.atomic.*;

public class TrainTrack {

    private final String[] slots = {"[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]",
            "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]", "[..]","[..]","[..]","[..]","[..]","[..]",};

    // declare array to hold the Binary Semaphores for access to track slots (sections)
    private final MageeSemaphore slotSem[] = new MageeSemaphore[22];

    // reference to train activity record
    Activity theTrainActivity;

    // global count of trains on shared track
    AtomicInteger aUsingSharedJunction1;
    AtomicInteger bUsingSharedJunction1;
    AtomicInteger aUsingSharedJunction2;
    AtomicInteger bUsingSharedJunction2;

    // counting semaphore to limit number of trains on track
    MageeSemaphore aCountSem;
    MageeSemaphore bCountSem;

    // declare  Semaphores for mutually exclusive access to aUsingSharedTrack
    private final MageeSemaphore aMutexSem;
    // declare  Semaphores for mutually exclusive access to bUsingSharedTrack
    private final MageeSemaphore bMutexSem;

    // shared junction locks
    MageeSemaphore sharedJunctionLock1;
    MageeSemaphore sharedJunctionLock2;

    /* Constructor for TrainTrack */
    public TrainTrack() {
        // record the train activity
        theTrainActivity = new Activity(slots);
        // create the array of slotSems and set them all free (empty)
        for (int i = 0; i <= 21; i++) {
            slotSem[i] = new MageeSemaphore(1);
        }
        // create  semaphores for mutually exclusive access to global count
        aMutexSem = new MageeSemaphore(1);
        bMutexSem = new MageeSemaphore(1);
        // create global AtomicInteger count variables
        aUsingSharedJunction1 = new AtomicInteger(0);
        bUsingSharedJunction1 = new AtomicInteger(0);
        aUsingSharedJunction2 = new AtomicInteger(0);
        bUsingSharedJunction2 = new AtomicInteger(0);
        // create  semaphores for limiting number of trains on track
        aCountSem = new MageeSemaphore(4);
        bCountSem = new MageeSemaphore(4);
        // initially shared junctions are accessible
        sharedJunctionLock1 = new MageeSemaphore(1);
        sharedJunctionLock2 = new MageeSemaphore(1);
    }  // constructor

    public void trainA_MoveOnToTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        aCountSem.P(); // limit  number of trains on track to avoid deadlock
        // record the train activity
        slotSem[0].P();// wait for slot 0 to be free
        slots[0] = "[" + trainName + "]"; // move train type A on to slot zero
        theTrainActivity.addMovedTo(0); // record the train activity
    }// end trainA_movedOnToTrack

    public void trainB_MoveOnToTrack(String trainName) {
        // record the train activity
        bCountSem.P();  // limit  number of trains on track to avoid deadlock
        CDS.idleQuietly((int) (Math.random() * 100));
        slotSem[12].P();// wait for slot 12 to be free
        slots[12] = "[" + trainName + "]"; // move train type B on to slot twelve
        theTrainActivity.addMovedTo(12); // record the train activity
    }// end trainB_movedOnToTrack

    public void trainA_MoveAroundToSharedJunction(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        int currentPosition = 0;
        do {
            // wait until the position ahead is empty and then move into it
            slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
            slots[currentPosition + 1] = slots[currentPosition]; // move train forward one position
            slots[currentPosition] = "[..]"; // clear the slot the train vacated
            theTrainActivity.addMovedTo(currentPosition + 1); // record the train activity
            slotSem[currentPosition].V(); // signal slot you are leaving
            currentPosition++;
        } while (currentPosition < 3);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainA_MoveAroundToSharedTrack

    public void trainB_MoveAroundToSharedJunction(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        int currentPosition = 12;
        do {
            /* wait until the position ahead is empty and then move into it*/
            slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
            slots[currentPosition + 1] = slots[currentPosition]; // move train forward
            slots[currentPosition] = "[..]"; //clear the slot the train vacated
            theTrainActivity.addMovedTo(currentPosition + 1); //record the train activity
            slotSem[currentPosition].V(); //signal slot you are leaving
            currentPosition++;
        } while (currentPosition < 15 && currentPosition >= 12);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainB_MoveAroundToSharedTrack

    public void trainA_CrossSharedJunction1(String trainName) {
        // wait for the necessary conditions to get access to shared junction
        aMutexSem.P(); // obtain mutually exclusive access to global variable aUsingSharedJunction1
        if (aUsingSharedJunction1.incrementAndGet() == 1)// if first A train cross shared junction
        {
            sharedJunctionLock1.P();  // grab lock to shared junction
        }
        aMutexSem.V(); // release mutually exclusive access to global variable aUsingSharedJunction1
        // cross shared junction
        slotSem[5].P();
        slots[5] = slots[3];
        slots[3] = "[..]";
        slotSem[3].V(); //move from slot[3] to slot[5]
        theTrainActivity.addMovedTo(5);  //record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[6].P();
        slots[6] = slots[5];
        slots[5] = "[..]";
        slotSem[5].V(); //move from slot[5] to slot[6]
        theTrainActivity.addMovedTo(6); // record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        aMutexSem.P(); // obtain mutually exclusive access to global variable aUsingSharedJunction1
        if (aUsingSharedJunction1.decrementAndGet() == 0) // if last A train crossed sharedJunction
        {
            sharedJunctionLock1.V(); // release lock to shared junction
        }
        aMutexSem.V(); // release mutually exclusive access to global variable aUsingSharedJunction1
        CDS.idleQuietly((int) (Math.random() * 10));
    }// end   trainA_CrossSharedJunction1

    public void trainB_CrossSharedJunction1(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // wait for the necessary conditions to get access to shared junction
        bMutexSem.P(); // obtain mutually exclusive access to global variable bUsingSharedJunction1
        if (bUsingSharedJunction1.incrementAndGet() == 1)// if first B train joining shared junction
        {
            sharedJunctionLock1.P();  // grab lock to shared junction
        }
        bMutexSem.V(); // release mutually exclusive access to global variable bUsingSharedJunction1
        CDS.idleQuietly((int) (Math.random() * 10));
        // cross shared junction
        slotSem[16].P();
        slots[16] = slots[15];
        slots[15] = "[..]";
        slotSem[15].V(); //move from slot[15] to slot[16]
        theTrainActivity.addMovedTo(16);  //record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        slotSem[17].P();
        slots[17] = slots[16];
        slots[16] = "[..]";
        slotSem[16].V(); //move from slot[16] to slot[17]
        theTrainActivity.addMovedTo(17); // record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        bMutexSem.P(); // obtain mutually exclusive access to global variable bUsingSharedJunction1
        if (bUsingSharedJunction1.decrementAndGet() == 0) // if last B train crossed shared junction
        {
            sharedJunctionLock1.V(); // release lock to shared junction
        }
        bMutexSem.V(); // release mutually exclusive access to global variable bUsingSharedJunction1
        CDS.idleQuietly((int) (Math.random() * 10));
    }// end   trainB_CrossSharedJunction1

    public void trainA_CrossSharedJunction2(String trainName) {
        // wait for the necessary conditions to get access to shared junction
        aMutexSem.P(); // obtain mutually exclusive access to global variable aUsingSharedJunction2
        if (aUsingSharedJunction2.incrementAndGet() == 1)// if first A train joining shared junction
        {
            sharedJunctionLock2.P();  // grab lock to shared junction
        }
        aMutexSem.V(); // release mutually exclusive access to global variable aUsingSharedJunction2
        // cross shared junction
        slotSem[8].P();
        slots[8] = slots[6];
        slots[6] = "[..]";
        slotSem[6].V(); //move from slot[6] to slot[8]
        theTrainActivity.addMovedTo(8); // record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        aMutexSem.P(); // obtain mutually exclusive access to global variable aUsingSharedJunction2
        if (aUsingSharedJunction2.decrementAndGet() == 0) // if last A train crossed shared junction
        {
            sharedJunctionLock2.V(); // release lock to shared junction
        }
        aMutexSem.V(); // release mutually exclusive access to global variable aUsingSharedJunction2
        CDS.idleQuietly((int) (Math.random() * 10));
    }// end   trainA_CrossSharedJunction2

    public void trainB_CrossSharedJunction2(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // wait for the necessary conditions to get access to shared junction
        bMutexSem.P(); // obtain mutually exclusive access to global variable bUsingSharedJunction2
        if (bUsingSharedJunction2.incrementAndGet() == 1)// if first B train joining shared junction
        {
            sharedJunctionLock2.P();  // grab lock to shared junction
        }
        bMutexSem.V(); // release mutually exclusive access to global variable bUsingSharedJunction2
        CDS.idleQuietly((int) (Math.random() * 10));
        // cross shared junction
        slotSem[18].P();
        slots[18] = slots[17];
        slots[17] = "[..]";
        slotSem[17].V(); //move from slot[17] to slot[18]
        theTrainActivity.addMovedTo(18); // record the train activity
        CDS.idleQuietly((int) (Math.random() * 10));
        bMutexSem.P(); // obtain mutually exclusive access to global variable bUsingSharedJunction2
        if (bUsingSharedJunction2.decrementAndGet() == 0) // if last B train crossed shared junction
        {
            sharedJunctionLock2.V(); // release lock to shared junction
        }
        bMutexSem.V(); // release mutually exclusive access to global variable bUsingSharedJunction2
        CDS.idleQuietly((int) (Math.random() * 10));
    }// end   trainB_CrossSharedJunction2


    public void trainA_MoveAroundToEndTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        int currentPosition = 8;
        do {
            // wait until the position ahead is empty and then move into it
            slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
            slots[currentPosition + 1] = slots[currentPosition]; // move train forward one position
            slots[currentPosition] = "[..]"; // clear the slot the train vacated
            theTrainActivity.addMovedTo(currentPosition + 1); // record the train activity
            slotSem[currentPosition].V(); // signal slot you are leaving
            currentPosition++;
        } while (currentPosition < 11 && currentPosition >= 8);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainA_MoveAroundToEndTrack

    public void trainB_MoveAroundToEndTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 100));
        int currentPosition = 18;
        do {
            /* wait until the position ahead is empty and then move into it*/
            slotSem[currentPosition + 1].P(); // wait for the slot ahead to be free
            slots[currentPosition + 1] = slots[currentPosition]; // move train forward
            slots[currentPosition] = "[..]"; //clear the slot the train vacated
            theTrainActivity.addMovedTo(currentPosition + 1); //record the train activity
            slotSem[currentPosition].V(); //signal slot you are leaving
            currentPosition++;
        } while (currentPosition < 21 && currentPosition >= 18);
        CDS.idleQuietly((int) (Math.random() * 100));
    } // end trainB_MoveAroundToEndTrack

    public void trainA_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // record the train activity
        theTrainActivity.addMessage("Train " + trainName + " is leaving the A loop at section 11");
        slots[11] = "[..]"; // move train type A off slot eleven
        slotSem[11].V();// signal slot 11 to be free
        CDS.idleQuietly((int) (Math.random() * 10));
        aCountSem.V(); // signal space for another A train
    }// end trainA_movedOffTrack

    public void trainB_MoveOffTrack(String trainName) {
        CDS.idleQuietly((int) (Math.random() * 10));
        // record the train activity
        theTrainActivity.addMessage("Train " + trainName + " is leaving the B loop at section 21");
        slots[21] = "[..]"; // move train type B off slot 21
        slotSem[21].V();// signal slot 21 to be free
        CDS.idleQuietly((int) (Math.random() * 10));
        bCountSem.V(); // signal space for another B train
    }// end trainB_movedOffTrack

} // end Train track
