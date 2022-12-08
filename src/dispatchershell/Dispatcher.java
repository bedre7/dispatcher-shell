package dispatchershell;

public class Dispatcher implements IDispatcher{
	private static IDispatcher instance;
	private IRealTimeQueue realTimeQueue;
	private IUserJob userJob;

	private Dispatcher(String filePath)
	{
		this.realTimeQueue = new RealTimeQueue();
		this.userJob = new UserJob();
	}
	
	private static IDispatcher getInstance(String filePath)
	{
		if (instance == null) {
			return new Dispatcher(filePath);
		}
		return instance;
	}

	@Override
	public void readFile() {
		// TODO Auto-generated method stub
		
	}
}
