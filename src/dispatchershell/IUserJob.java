package dispatchershell;

import java.io.IOException;

public interface IUserJob{
	IProcess run() throws IOException, InterruptedException;
	void distribute(IProcess process);
	public boolean hasProcess();	
	void remove(IProcess process);
}
