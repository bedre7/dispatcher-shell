package dispatchershell;

public class Timer {
	private static final int ONESECOND = 1000;
	private static int currentTime = 0;
	
	public static void tick()
	{
		//sleep for one second
		try
        {
            Thread.sleep(ONESECOND);
            currentTime++;
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
