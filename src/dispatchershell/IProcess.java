package dispatchershell;

import java.io.IOException;

public interface IProcess {
	int getId() ;
	int getArrivalTime() ;
	Color getColor() ;
	int getBurstTime();
	int getElapsedTime() ;
	State getState();
	Priority getPriority();
	int getWaitingTime();
	void setState(State state);
	void setPriority(Priority priority) ;
	void reducePriority();
	State execute(int quantum) throws IOException, InterruptedException;
	boolean isRealTime();
	boolean isOver();
	boolean hasHigherPriority(IProcess other);
}
