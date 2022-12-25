package dispatchershell;

public interface IUserJob{
	IProcess run();
	void distribute(IProcess process);
	public boolean hasProcess();	
	void remove(IProcess process);
}
