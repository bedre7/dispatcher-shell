package dispatchershell;

public interface IDispatcher {
    IDispatcher readFile();
    Color getRandomColor();
    void start();
    boolean processHasArrived(IProcess process);
}
