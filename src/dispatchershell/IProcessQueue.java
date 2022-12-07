package dispatchershell;

public interface IProcessQueue {
	void add(IProcess process);
    IProcess remove(IProcess process);
    boolean isEmpty(IProcess process);
}
