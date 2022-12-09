package dispatchershell;

public interface IUserJob{
	void run();
	void distribute(IProcess process);
	public boolean hasProcess();
	public boolean hasExceededTimeLimit(IProcess process);
	
}
