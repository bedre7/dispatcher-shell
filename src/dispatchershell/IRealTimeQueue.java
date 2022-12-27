package dispatchershell;

import java.io.IOException;

public interface IRealTimeQueue {
	void add(IProcess process);
    void remove(IProcess process);
    boolean isEmpty();
	void run() throws IOException, InterruptedException;
}