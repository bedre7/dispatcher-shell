package dispatchershell;

import java.io.IOException;

public interface IDispatcher {
    IDispatcher readFile();
    Color getRandomColor();
    void start() throws IOException, InterruptedException;
    boolean processHasArrived(IProcess process);
    IProcess getCurrentProcess();
    void interrupt(IProcess process);
}
