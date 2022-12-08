package dispatchershell;

public interface IUserJob{
	void run();
	void distribute(IProcess process);
	boolean hasProcess();
}
