package dispatchershell;

public interface IRealTimeQueue {
	void add(IProcess process);
    IProcess remove();
    boolean isEmpty();
	void run();
}