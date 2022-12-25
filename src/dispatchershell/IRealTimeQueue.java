package dispatchershell;

public interface IRealTimeQueue {
	void add(IProcess process);
    void remove(IProcess process);
    boolean isEmpty();
	void run();
}