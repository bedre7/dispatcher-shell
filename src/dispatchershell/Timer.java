package dispatchershell;

//A class used to control time throughout the execution of this program
public class Timer {
	private static final int ONESECOND = 0;
	private static int currentTime = 0;
	
	public static void tick()
	{
		//sleep for one second
		try
        {
            Thread.sleep(ONESECOND);
            currentTime++;
            /*
                For every second that passes, the waiting list of processes is checked
                This is done to terminates any process that has exceeded maximum waiting time
            */

            Dispatcher.checkPendingProcesses();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
	}
	
	public static int getCurrentTime() {
		return currentTime;
	}
}
