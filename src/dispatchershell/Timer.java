package dispatchershell;

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
