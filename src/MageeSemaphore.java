import java.util.concurrent.*;
//MageeSemaphore.java
//This is an implementation of the traditional (counting) Semaphore with P() and V() operations
class MageeSemaphore
{
    private Semaphore sem;

    public MageeSemaphore (int initialCount)
    {
        sem = new Semaphore(initialCount);
    } // end constructor

    public void P()
    {
        try {
            sem.acquire();
        } catch (InterruptedException ex) {System.out.println("Interrupted when waiting");}
    } // end P()

    public void V()
    {
        sem.release();
    } // end V()

} // end MageeSemaphore
