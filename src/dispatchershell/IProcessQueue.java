package dispatchershell;

public interface IProcessQueue {
	void add(IProcess process);
    IProcess remove();
    boolean isEmpty();
}
