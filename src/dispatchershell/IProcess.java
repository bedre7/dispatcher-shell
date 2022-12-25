package dispatchershell;

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
	State execute(int quantum, int maxExecutionTime);
	boolean isRealTime();
	boolean isOver();
	boolean hasExceededTimeLimit(int limit);
	boolean hasHigherPriority(IProcess other);
}
