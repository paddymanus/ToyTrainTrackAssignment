//CDS.java - Useful class for Concurrent and Distributed Systems
public class CDS {

    public static void idle(int millisecs) {
        Thread mainThread = Thread.currentThread();
        System.out.println(mainThread.getName() + ": About to sleep");
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) { }
        System.out.println(mainThread.getName() + ": Woken up");
    } // end idle

    public static void idleQuietly(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) { }
    } // end idleQuietly

} // end CDS
