package dispatchershell;

//Zamani yonetebilmek icin kullanilan Zamanlayici sinifi
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
            //her saniyede zamani gecen proses varsa Dagitici kontrol ettirilir
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
